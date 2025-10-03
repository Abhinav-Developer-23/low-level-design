package org.example.appenders;

import org.example.enums.OverflowStrategy;
import org.example.interfaces.LogAppender;
import org.example.model.LogMessage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Asynchronous appender that wraps any LogAppender and processes log messages
 * in a background thread using a queue.
 * 
 * Features:
 * - Non-blocking log operations (except when queue is full and BLOCK strategy is used)
 * - Configurable queue capacity
 * - Multiple overflow strategies
 * - Graceful shutdown with message flushing
 * - Performance monitoring (dropped messages, queue size)
 * 
 * Design Patterns:
 * - Decorator Pattern: Wraps existing appenders to add async behavior
 * - Producer-Consumer Pattern: Main thread produces, worker thread consumes
 */
public class AsyncAppender extends AbstractLogAppender {
    private final LogAppender delegate;
    private final BlockingQueue<LogMessage> queue;
    private final OverflowStrategy overflowStrategy;
    private final Thread workerThread;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AtomicLong droppedMessages = new AtomicLong(0);
    private final String name;

    // Poison pill to signal shutdown
    private static final LogMessage POISON_PILL = new LogMessage.Builder()
            .level(org.example.enums.LogLevel.INFO)
            .message("SHUTDOWN")
            .className("AsyncAppender")
            .build();

    /**
     * Creates an async appender with default settings.
     * 
     * @param delegate The appender to wrap
     */
    public AsyncAppender(LogAppender delegate) {
        this(delegate, 10000, OverflowStrategy.BLOCK, "AsyncAppender");
    }

    /**
     * Creates an async appender with custom settings.
     * 
     * @param delegate The appender to wrap
     * @param queueCapacity Maximum queue size
     * @param overflowStrategy Strategy when queue is full
     * @param name Name for the worker thread
     */
    public AsyncAppender(LogAppender delegate, int queueCapacity, 
                        OverflowStrategy overflowStrategy, String name) {
        super();
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate appender cannot be null");
        }
        if (queueCapacity <= 0) {
            throw new IllegalArgumentException("Queue capacity must be positive");
        }
        
        this.delegate = delegate;
        this.queue = new ArrayBlockingQueue<>(queueCapacity);
        this.overflowStrategy = overflowStrategy;
        this.name = name;
        
        // Start background worker thread
        this.workerThread = new Thread(this::processQueue, name + "-Worker");
        this.workerThread.setDaemon(false); // Non-daemon to ensure message processing
        this.workerThread.start();
    }

    @Override
    protected void doAppend(LogMessage logMessage) {
        if (!running.get()) {
            System.err.println("AsyncAppender is shutting down, message dropped: " + 
                             logMessage.getMessage());
            return;
        }

        boolean added = false;
        
        switch (overflowStrategy) {
            case BLOCK:
                try {
                    queue.put(logMessage); // Blocks if queue is full
                    added = true;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Interrupted while adding log message: " + e.getMessage());
                }
                break;

            case DROP_OLDEST:
                added = queue.offer(logMessage);
                if (!added) {
                    // Remove oldest and try again
                    queue.poll();
                    added = queue.offer(logMessage);
                    droppedMessages.incrementAndGet();
                }
                break;

            case DROP_NEWEST:
                added = queue.offer(logMessage);
                if (!added) {
                    droppedMessages.incrementAndGet();
                }
                break;

            case LOG_AND_DROP:
                added = queue.offer(logMessage);
                if (!added) {
                    droppedMessages.incrementAndGet();
                    System.err.println("AsyncAppender queue full. Message dropped: " + 
                                     logMessage.getMessage() + 
                                     " (Total dropped: " + droppedMessages.get() + ")");
                }
                break;
        }
    }

    /**
     * Background thread that processes queued log messages.
     */
    private void processQueue() {
        while (running.get() || !queue.isEmpty()) {
            try {
                LogMessage message = queue.poll(100, TimeUnit.MILLISECONDS);
                
                if (message == null) {
                    continue; // Timeout, check running flag again
                }
                
                if (message == POISON_PILL) {
                    break; // Shutdown signal
                }
                
                // Delegate to wrapped appender
                try {
                    delegate.append(message);
                } catch (Exception e) {
                    System.err.println("Error in delegate appender: " + e.getMessage());
                }
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    protected void doClose() {
        System.out.println("AsyncAppender shutting down: " + name);
        
        // Signal shutdown
        running.set(false);
        
        // Send poison pill
        try {
            queue.offer(POISON_PILL, 1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Wait for worker thread to finish processing
        try {
            workerThread.join(5000); // Wait up to 5 seconds
            
            if (workerThread.isAlive()) {
                System.err.println("AsyncAppender worker thread did not terminate gracefully");
                workerThread.interrupt();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Report statistics
        long dropped = droppedMessages.get();
        int remaining = queue.size();
        
        System.out.println("AsyncAppender shutdown complete:");
        System.out.println("  - Dropped messages: " + dropped);
        System.out.println("  - Remaining in queue: " + remaining);
        
        // Close delegate appender
        try {
            delegate.close();
        } catch (Exception e) {
            System.err.println("Error closing delegate appender: " + e.getMessage());
        }
    }

    /**
     * Gets the current queue size.
     */
    public int getQueueSize() {
        return queue.size();
    }

    /**
     * Gets the queue capacity.
     */
    public int getQueueCapacity() {
        return queue.remainingCapacity() + queue.size();
    }

    /**
     * Gets the number of dropped messages.
     */
    public long getDroppedMessages() {
        return droppedMessages.get();
    }

    /**
     * Gets the overflow strategy.
     */
    public OverflowStrategy getOverflowStrategy() {
        return overflowStrategy;
    }

    /**
     * Checks if the worker thread is running.
     */
    public boolean isRunning() {
        return running.get() && workerThread.isAlive();
    }

    /**
     * Gets the wrapped delegate appender.
     */
    public LogAppender getDelegate() {
        return delegate;
    }
}

