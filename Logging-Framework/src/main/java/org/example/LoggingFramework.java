package org.example;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lightweight Logging Framework - Single File Implementation
 * 
 * Design Patterns Used:
 * 1. Singleton Pattern - LoggerFactory
 * 2. Strategy Pattern - Different log destinations (LogAppender)
 * 3. Factory Pattern - Logger creation
 * 4. Builder Pattern - LoggerConfig
 * 
 * SOLID Principles:
 * - Single Responsibility: Each class has one reason to change
 * - Open/Closed: Easy to extend with new appenders without modifying existing code
 * - Liskov Substitution: LogAppender implementations are interchangeable
 * - Interface Segregation: Small, focused interfaces
 * - Dependency Inversion: Logger depends on LogAppender abstraction
 */

// ============================================================================
// ENUMS
// ============================================================================

/**
 * Log levels supported by the framework
 */
enum LogLevel {
    DEBUG(1),
    INFO(2),
    WARN(3),
    ERROR(4);

    private final int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public boolean isEnabled(LogLevel configuredLevel) {
        return this.priority >= configuredLevel.priority;
    }
}

// ============================================================================
// INTERFACES
// ============================================================================

/**
 * Interface for log message formatting
 */
interface LogFormatter {
    String format(LogLevel level, String message, String loggerName);
}

/**
 * Interface for log destinations (Strategy Pattern)
 */
interface LogAppender {
    void append(String formattedMessage);
    void close();
}

// ============================================================================
// MODELS
// ============================================================================

/**
 * Represents a log message with metadata
 */
class LogMessage {
    private final LocalDateTime timestamp;
    private final LogLevel level;
    private final String message;
    private final String threadName;
    private final String loggerName;

    public LogMessage(LogLevel level, String message, String loggerName) {
        this.timestamp = LocalDateTime.now();
        this.level = level;
        this.message = message;
        this.threadName = Thread.currentThread().getName();
        this.loggerName = loggerName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getLoggerName() {
        return loggerName;
    }
}

/**
 * Configuration for Logger instances (Builder Pattern)
 */
class LoggerConfig {
    private LogLevel logLevel;
    private LogFormatter formatter;
    private List<LogAppender> appenders;

    private LoggerConfig(Builder builder) {
        this.logLevel = builder.logLevel;
        this.formatter = builder.formatter;
        this.appenders = new ArrayList<>(builder.appenders);
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public LogFormatter getFormatter() {
        return formatter;
    }

    public List<LogAppender> getAppenders() {
        return appenders;
    }

    public static class Builder {
        private LogLevel logLevel = LogLevel.INFO;
        private LogFormatter formatter = new DefaultLogFormatter();
        private List<LogAppender> appenders = new ArrayList<>();

        public Builder setLogLevel(LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public Builder setFormatter(LogFormatter formatter) {
            this.formatter = formatter;
            return this;
        }

        public Builder addAppender(LogAppender appender) {
            this.appenders.add(appender);
            return this;
        }

        public LoggerConfig build() {
            if (appenders.isEmpty()) {
                appenders.add(new ConsoleAppender());
            }
            return new LoggerConfig(this);
        }
    }
}

// ============================================================================
// FORMATTERS
// ============================================================================

/**
 * Default implementation of LogFormatter
 * Format: [2025-10-07 14:23:11] [INFO] [Thread-1] - Application started successfully.
 */
class DefaultLogFormatter implements LogFormatter {
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogLevel level, String message, String loggerName) {
        LogMessage logMessage = new LogMessage(level, message, loggerName);
        return String.format("[%s] [%s] [%s] - %s",
            logMessage.getTimestamp().format(DATE_FORMATTER),
            logMessage.getLevel(),
            logMessage.getThreadName(),
            logMessage.getMessage()
        );
    }
}

/**
 * Detailed log formatter with logger name
 */
class DetailedLogFormatter implements LogFormatter {
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(LogLevel level, String message, String loggerName) {
        LogMessage logMessage = new LogMessage(level, message, loggerName);
        return String.format("[%s] [%s] [%s] [%s] - %s",
            logMessage.getTimestamp().format(DATE_FORMATTER),
            logMessage.getLevel(),
            logMessage.getThreadName(),
            loggerName,
            logMessage.getMessage()
        );
    }
}

// ============================================================================
// APPENDERS (Strategy Pattern)
// ============================================================================

/**
 * Abstract base class for appenders with thread-safety
 */
abstract class AbstractLogAppender implements LogAppender {
    protected final Lock lock = new ReentrantLock();

    @Override
    public void append(String formattedMessage) {
        lock.lock();
        try {
            doAppend(formattedMessage);
        } finally {
            lock.unlock();
        }
    }

    protected abstract void doAppend(String formattedMessage);
}

/**
 * Console appender - writes logs to System.out
 */
class ConsoleAppender extends AbstractLogAppender {
    @Override
    protected void doAppend(String formattedMessage) {
        System.out.println(formattedMessage);
    }

    @Override
    public void close() {
        // Nothing to close for console
    }
}

/**
 * File appender - writes logs to a file
 * Thread-safe with proper resource management
 */
class FileAppender extends AbstractLogAppender {
    private BufferedWriter writer;
    private boolean closed = false;

    public FileAppender(String filePath) {
        try {
            // Create parent directories if they don't exist
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            this.writer = new BufferedWriter(new FileWriter(filePath, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize FileAppender for: " + filePath, e);
        }
    }

    @Override
    protected void doAppend(String formattedMessage) {
        if (closed) {
            throw new IllegalStateException("Cannot write to closed appender");
        }
        try {
            writer.write(formattedMessage);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Failed to write log to file: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        lock.lock();
        try {
            if (!closed && writer != null) {
                writer.close();
                closed = true;
            }
        } catch (IOException e) {
            System.err.println("Failed to close FileAppender: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}

/**
 * Asynchronous appender - delegates to another appender in a background thread
 * Useful for performance-critical applications
 */
class AsyncAppender extends AbstractLogAppender {
    private final LogAppender delegate;
    private final Queue<String> messageQueue;
    private final Thread workerThread;
    private volatile boolean running = true;

    public AsyncAppender(LogAppender delegate) {
        this.delegate = delegate;
        this.messageQueue = new java.util.concurrent.ConcurrentLinkedQueue<>();
        this.workerThread = new Thread(this::processMessages, "AsyncAppender-Worker");
        this.workerThread.setDaemon(true);
        this.workerThread.start();
    }

    @Override
    protected void doAppend(String formattedMessage) {
        messageQueue.offer(formattedMessage);
    }

    private void processMessages() {
        while (running || !messageQueue.isEmpty()) {
            String message = messageQueue.poll();
            if (message != null) {
                delegate.append(message);
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    @Override
    public void close() {
        running = false;
        try {
            workerThread.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        delegate.close();
    }
}

// ============================================================================
// LOGGER
// ============================================================================

/**
 * Main Logger class
 * Thread-safe and supports multiple log levels and destinations
 */
class Logger {
    private final String name;
    private final LoggerConfig config;

    Logger(String name, LoggerConfig config) {
        this.name = name;
        this.config = config;
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void error(String message, Throwable throwable) {
        log(LogLevel.ERROR, message + " - Exception: " + throwable.getMessage());
    }

    private void log(LogLevel level, String message) {
        if (level.isEnabled(config.getLogLevel())) {
            String formattedMessage = config.getFormatter().format(level, message, name);
            
            for (LogAppender appender : config.getAppenders()) {
                appender.append(formattedMessage);
            }
        }
    }

    public String getName() {
        return name;
    }

    public LogLevel getLogLevel() {
        return config.getLogLevel();
    }

    LoggerConfig getConfig() {
        return config;
    }
}

// ============================================================================
// LOGGER FACTORY (Singleton Pattern)
// ============================================================================

/**
 * Factory for creating and managing Logger instances
 * Implements Singleton pattern for global access
 */
class LoggerFactory {
    private static final LoggerFactory INSTANCE = new LoggerFactory();
    private final Map<String, Logger> loggers = new ConcurrentHashMap<>();
    private LoggerConfig defaultConfig;

    private LoggerFactory() {
        // Default configuration
        this.defaultConfig = new LoggerConfig.Builder()
            .setLogLevel(LogLevel.INFO)
            .setFormatter(new DefaultLogFormatter())
            .addAppender(new ConsoleAppender())
            .build();
    }

    public static LoggerFactory getInstance() {
        return INSTANCE;
    }

    public Logger getLogger(String name) {
        return loggers.computeIfAbsent(name, n -> new Logger(n, defaultConfig));
    }

    public Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getSimpleName());
    }

    public Logger getLogger(String name, LoggerConfig config) {
        return loggers.computeIfAbsent(name, n -> new Logger(n, config));
    }

    public void setDefaultConfig(LoggerConfig config) {
        this.defaultConfig = config;
    }

    public void shutdown() {
        for (Logger logger : loggers.values()) {
            shutdown(logger);
        }
        loggers.clear();
    }

    private void shutdown(Logger logger) {
        for (LogAppender appender : logger.getConfig().getAppenders()) {
            appender.close();
        }
    }
}

// ============================================================================
// MAIN - DEMONSTRATION
// ============================================================================

/**
 * Demonstration of the Logging Framework
 */
public class LoggingFramework {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Logging Framework Demo ===\n");

        // Demo 1: Basic console logging with default configuration
        demo1BasicLogging();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demo 2: File logging
        demo2FileLogging();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demo 3: Multiple appenders (console + file)
        demo3MultipleAppenders();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demo 4: Different log levels
        demo4LogLevels();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demo 5: Thread safety demonstration
        demo5ThreadSafety();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demo 6: Async logging
        demo6AsyncLogging();

        System.out.println("\n" + "=".repeat(60) + "\n");

        // Demo 7: Custom formatter
        demo7CustomFormatter();

        // Cleanup
        LoggerFactory.getInstance().shutdown();
    }

    private static void demo1BasicLogging() {
        System.out.println("Demo 1: Basic Console Logging");
        System.out.println("-".repeat(40));

        Logger logger = LoggerFactory.getInstance().getLogger("BasicLogger");
        
        logger.debug("This is a debug message");
        logger.info("Application started successfully");
        logger.warn("Low memory warning");
        logger.error("Failed to connect to database");
    }

    private static void demo2FileLogging() {
        System.out.println("Demo 2: File Logging");
        System.out.println("-".repeat(40));

        LoggerConfig config = new LoggerConfig.Builder()
            .setLogLevel(LogLevel.DEBUG)
            .addAppender(new FileAppender("logs/application.log"))
            .build();

        Logger logger = LoggerFactory.getInstance().getLogger("FileLogger", config);
        
        logger.debug("Debug message to file");
        logger.info("Info message to file");
        logger.warn("Warning message to file");
        logger.error("Error message to file");

        System.out.println("Logs written to: logs/application.log");
    }

    private static void demo3MultipleAppenders() {
        System.out.println("Demo 3: Multiple Appenders (Console + File)");
        System.out.println("-".repeat(40));

        LoggerConfig config = new LoggerConfig.Builder()
            .setLogLevel(LogLevel.INFO)
            .addAppender(new ConsoleAppender())
            .addAppender(new FileAppender("logs/concurrent.log"))
            .build();

        Logger logger = LoggerFactory.getInstance().getLogger("MultiAppenderLogger", config);
        
        logger.info("This message goes to both console and file");
        logger.warn("Warning logged to multiple destinations");
        logger.error("Error logged everywhere");

        System.out.println("\n(Also written to logs/concurrent.log)");
    }

    private static void demo4LogLevels() {
        System.out.println("Demo 4: Different Log Levels");
        System.out.println("-".repeat(40));

        // Logger with ERROR level - only ERROR messages will be logged
        LoggerConfig errorConfig = new LoggerConfig.Builder()
            .setLogLevel(LogLevel.ERROR)
            .build();

        Logger errorLogger = LoggerFactory.getInstance().getLogger("ErrorOnlyLogger", errorConfig);
        
        System.out.println("Logger configured with ERROR level:");
        errorLogger.debug("This DEBUG won't show");
        errorLogger.info("This INFO won't show");
        errorLogger.warn("This WARN won't show");
        errorLogger.error("This ERROR will show");

        System.out.println();

        // Logger with DEBUG level - all messages will be logged
        LoggerConfig debugConfig = new LoggerConfig.Builder()
            .setLogLevel(LogLevel.DEBUG)
            .build();

        Logger debugLogger = LoggerFactory.getInstance().getLogger("DebugLogger", debugConfig);
        
        System.out.println("Logger configured with DEBUG level:");
        debugLogger.debug("This DEBUG will show");
        debugLogger.info("This INFO will show");
        debugLogger.warn("This WARN will show");
        debugLogger.error("This ERROR will show");
    }

    private static void demo5ThreadSafety() throws InterruptedException {
        System.out.println("Demo 5: Thread Safety (10 concurrent threads)");
        System.out.println("-".repeat(40));

        LoggerConfig config = new LoggerConfig.Builder()
            .setLogLevel(LogLevel.INFO)
            .addAppender(new ConsoleAppender())
            .addAppender(new FileAppender("logs/concurrent.log"))
            .build();

        Logger logger = LoggerFactory.getInstance().getLogger("ThreadSafeLogger", config);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int threadNum = i;
            Thread thread = new Thread(() -> {
                logger.info("Message from thread " + threadNum);
                logger.warn("Warning from thread " + threadNum);
            }, "Worker-" + i);
            threads.add(thread);
        }

        // Start all threads
        for (Thread thread : threads) {
            thread.start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("\n✓ All threads completed - logs should not be interleaved");
    }

    private static void demo6AsyncLogging() throws InterruptedException {
        System.out.println("Demo 6: Asynchronous Logging");
        System.out.println("-".repeat(40));

        FileAppender fileAppender = new FileAppender("logs/async.log");
        AsyncAppender asyncAppender = new AsyncAppender(fileAppender);

        LoggerConfig config = new LoggerConfig.Builder()
            .setLogLevel(LogLevel.INFO)
            .addAppender(asyncAppender)
            .build();

        Logger logger = LoggerFactory.getInstance().getLogger("AsyncLogger", config);
        
        for (int i = 0; i < 5; i++) {
            logger.info("Async message " + (i + 1));
        }

        System.out.println("✓ Messages queued for async writing");
        System.out.println("Check logs/async.log after a moment");

        // Give async appender time to process
        Thread.sleep(100);
        asyncAppender.close();
    }

    private static void demo7CustomFormatter() {
        System.out.println("Demo 7: Custom Detailed Formatter");
        System.out.println("-".repeat(40));

        LoggerConfig config = new LoggerConfig.Builder()
            .setLogLevel(LogLevel.INFO)
            .setFormatter(new DetailedLogFormatter())
            .build();

        Logger logger = LoggerFactory.getInstance().getLogger("CustomFormatterLogger", config);
        
        logger.info("Message with custom detailed format");
        logger.warn("Warning with timestamp and logger name");
        logger.error("Error with millisecond precision");
    }
}

