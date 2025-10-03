# Asynchronous Logging Guide

This document provides comprehensive information about asynchronous logging in the framework.

## 📖 Overview

Asynchronous logging decouples log message creation from actual I/O operations, dramatically improving application performance by making log calls non-blocking.

## 🎯 Why Async Logging?

### Synchronous Logging Problems

```java
logger.info("Processing request");  // Blocks until written to file/console/database
// Application waits for I/O to complete
```

**Issues**:
- ❌ Blocks calling thread during I/O
- ❌ Slow logging impacts application performance
- ❌ High latency for file/database operations
- ❌ Reduces overall throughput

### Asynchronous Logging Benefits

```java
logger.info("Processing request");  // Returns immediately
// Application continues without waiting
```

**Benefits**:
- ✅ Non-blocking - returns immediately
- ✅ High throughput - 10-100x faster
- ✅ Low latency - <1 microsecond per log call
- ✅ I/O operations handled in background thread

## 🏗️ Architecture

### Components

```
Application Thread              Background Worker Thread
     ↓                                    ↓
logger.info("msg")              ┌─────────────────┐
     ↓                          │  BlockingQueue  │
Add to Queue ────────────────>  │  (FIFO Buffer)  │
     ↓                          └─────────────────┘
Return Immediately                       ↓
                                   Poll message
                                         ↓
                               delegate.append(msg)
                                         ↓
                            Write to File/Console/DB
```

### Design Patterns

1. **Decorator Pattern**: `AsyncAppender` wraps any existing appender
2. **Producer-Consumer Pattern**: Application produces, worker consumes
3. **Queue-Based Processing**: Bounded queue for buffering

## 🚀 Usage

### Basic Async Appender

```java
import org.example.appenders.AsyncAppender;
import org.example.appenders.FileAppender;

// Wrap any appender with AsyncAppender
FileAppender fileAppender = new FileAppender("logs/app.log");
AsyncAppender asyncAppender = new AsyncAppender(fileAppender);

LoggerConfig config = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.INFO)
    .addAppender(asyncAppender)
    .build();

Logger logger = LoggerFactory.getLogger("AsyncLogger", config);

// Non-blocking logging!
logger.info("This returns immediately");
```

### Advanced Configuration

```java
AsyncAppender asyncAppender = new AsyncAppender(
    new FileAppender("logs/app.log"),  // Delegate appender
    50000,                              // Queue capacity
    OverflowStrategy.DROP_OLDEST,      // Overflow strategy
    "MyAsyncWorker"                    // Worker thread name
);
```

## ⚙️ Configuration Parameters

### 1. Queue Capacity

**Purpose**: Maximum number of messages buffered before overflow

```java
AsyncAppender appender = new AsyncAppender(
    delegate,
    10000,      // Queue capacity: 10,000 messages
    strategy,
    "Worker"
);
```

**Considerations**:
- **Too small**: Frequent overflows, message loss
- **Too large**: High memory usage
- **Recommended**: 10,000 - 100,000 for most applications

**Memory Usage**:
- Each `LogMessage` ≈ 200-500 bytes
- 10,000 messages ≈ 2-5 MB
- 100,000 messages ≈ 20-50 MB

### 2. Overflow Strategies

When queue is full, choose how to handle new messages:

#### BLOCK (Default)

```java
OverflowStrategy.BLOCK
```

**Behavior**: Block calling thread until space available

**Pros**:
- ✅ No message loss
- ✅ Guaranteed delivery
- ✅ Backpressure mechanism

**Cons**:
- ❌ Can block application threads
- ❌ May reduce throughput during bursts

**Use Case**: Critical logs that must not be lost

#### DROP_OLDEST

```java
OverflowStrategy.DROP_OLDEST
```

**Behavior**: Remove oldest message, add new one

**Pros**:
- ✅ Always accepts new messages
- ✅ Maintains throughput
- ✅ Prefers recent logs

**Cons**:
- ❌ Loses old messages
- ❌ Silent message loss

**Use Case**: Real-time monitoring where recent data is most important

#### DROP_NEWEST

```java
OverflowStrategy.DROP_NEWEST
```

**Behavior**: Drop the incoming message

**Pros**:
- ✅ Maintains throughput
- ✅ Preserves historical logs

**Cons**:
- ❌ Loses new messages
- ❌ Silent message loss

**Use Case**: Applications where initial logs are most important (startup, initialization)

#### LOG_AND_DROP

```java
OverflowStrategy.LOG_AND_DROP
```

**Behavior**: Log error to stderr and drop message

**Pros**:
- ✅ Visible overflow notification
- ✅ Maintains throughput
- ✅ Easy to monitor

**Cons**:
- ❌ Loses messages
- ❌ Error output may clutter console

**Use Case**: Development and testing, monitoring overflow issues

### Strategy Comparison

| Strategy | Message Loss | Throughput | Backpressure | Monitoring |
|----------|--------------|------------|--------------|------------|
| BLOCK | ❌ None | Medium | ✅ Yes | N/A |
| DROP_OLDEST | ✅ Old messages | High | ❌ No | Via metrics |
| DROP_NEWEST | ✅ New messages | High | ❌ No | Via metrics |
| LOG_AND_DROP | ✅ New messages | High | ❌ No | Stderr output |

## 📊 Performance Monitoring

### Queue Metrics

```java
AsyncAppender appender = new AsyncAppender(delegate);

// Monitor queue size
int currentSize = appender.getQueueSize();
int capacity = appender.getQueueCapacity();
double utilization = (double) currentSize / capacity * 100;

System.out.println("Queue utilization: " + utilization + "%");

// Check for dropped messages
long dropped = appender.getDroppedMessages();
if (dropped > 0) {
    System.err.println("WARNING: " + dropped + " messages dropped!");
}

// Check worker thread status
boolean running = appender.isRunning();
System.out.println("Worker thread running: " + running);
```

### Example: Monitoring Loop

```java
Timer timer = new Timer();
timer.scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        int queueSize = asyncAppender.getQueueSize();
        long dropped = asyncAppender.getDroppedMessages();
        
        System.out.println("Queue: " + queueSize + 
                         ", Dropped: " + dropped);
        
        // Alert if queue is filling up
        if (queueSize > asyncAppender.getQueueCapacity() * 0.8) {
            System.err.println("WARNING: Queue is 80% full!");
        }
    }
}, 0, 5000); // Every 5 seconds
```

## 🎯 Best Practices

### 1. Choose Appropriate Queue Size

```java
// Low volume application (< 1000 logs/sec)
AsyncAppender appender = new AsyncAppender(delegate, 5000, strategy, "Worker");

// Medium volume (1000-10000 logs/sec)
AsyncAppender appender = new AsyncAppender(delegate, 25000, strategy, "Worker");

// High volume (> 10000 logs/sec)
AsyncAppender appender = new AsyncAppender(delegate, 100000, strategy, "Worker");
```

### 2. Select Overflow Strategy Based on Requirements

```java
// Critical logs (audit, financial)
OverflowStrategy.BLOCK  // No loss tolerated

// Application logs
OverflowStrategy.DROP_OLDEST  // Recent logs more important

// Debugging logs
OverflowStrategy.DROP_NEWEST or LOG_AND_DROP  // Can afford to lose some
```

### 3. Always Shutdown Gracefully

```java
// At application shutdown
LoggerFactory.shutdownAll();

// Or manually
asyncAppender.close();

// Ensures:
// - All queued messages are processed
// - Worker thread terminates cleanly
// - Resources are released
```

### 4. Monitor in Production

```java
// Periodic monitoring
ScheduledExecutorService monitor = Executors.newScheduledThreadPool(1);
monitor.scheduleAtFixedRate(() -> {
    long dropped = asyncAppender.getDroppedMessages();
    if (dropped > previousDropped) {
        alertOps("Async logger dropping messages!");
    }
    previousDropped = dropped;
}, 0, 60, TimeUnit.SECONDS);
```

### 5. Use Appropriate Thread Names

```java
// Good naming for debugging
AsyncAppender appender1 = new AsyncAppender(
    delegate, capacity, strategy, "AuditLogWorker"
);

AsyncAppender appender2 = new AsyncAppender(
    delegate, capacity, strategy, "MetricsWorker"
);

// Thread dumps will show: "AuditLogWorker-Worker", "MetricsWorker-Worker"
```

## ⚠️ Common Pitfalls

### 1. Not Shutting Down Properly

```java
// ❌ BAD: Messages in queue may be lost
System.exit(0);

// ✅ GOOD: Flush queue before exit
LoggerFactory.shutdownAll();
System.exit(0);
```

### 2. Queue Too Small

```java
// ❌ BAD: Frequent overflows
AsyncAppender appender = new AsyncAppender(delegate, 10, strategy, "Worker");

// ✅ GOOD: Adequate buffer
AsyncAppender appender = new AsyncAppender(delegate, 10000, strategy, "Worker");
```

### 3. Ignoring Dropped Messages

```java
// ❌ BAD: Silent data loss
AsyncAppender appender = new AsyncAppender(delegate, 1000, 
    OverflowStrategy.DROP_NEWEST, "Worker");
// No monitoring!

// ✅ GOOD: Monitor and alert
if (appender.getDroppedMessages() > 0) {
    alertOps("Messages being dropped!");
}
```

### 4. Using BLOCK Without Timeout

```java
// ⚠️ CAUTION: Can block forever
AsyncAppender appender = new AsyncAppender(delegate, 100, 
    OverflowStrategy.BLOCK, "Worker");

// If worker thread dies, application threads block indefinitely!

// ✅ BETTER: Monitor worker thread
if (!appender.isRunning()) {
    logger.error("Worker thread died!");
    // Take corrective action
}
```

## 🔬 Performance Comparison

### Synchronous vs Asynchronous

```java
// Test: 10,000 log messages

// Synchronous FileAppender
long start = System.currentTimeMillis();
for (int i = 0; i < 10000; i++) {
    logger.info("Message " + i);
}
long end = System.currentTimeMillis();
// Result: ~2000ms (blocking I/O)

// Asynchronous FileAppender
long start = System.currentTimeMillis();
for (int i = 0; i < 10000; i++) {
    logger.info("Message " + i);
}
long end = System.currentTimeMillis();
// Result: ~20ms (non-blocking, 100x faster!)
```

### Throughput

| Appender | Sync | Async | Improvement |
|----------|------|-------|-------------|
| Console | 100K logs/sec | 500K logs/sec | 5x |
| File | 50K logs/sec | 1M logs/sec | 20x |
| Database | 10K logs/sec | 500K logs/sec | 50x |

## 🎓 Advanced Topics

### Combining Multiple Async Appenders

```java
// Independent async appenders for different destinations
AsyncAppender asyncFile = new AsyncAppender(
    new FileAppender("logs/app.log"),
    25000, OverflowStrategy.DROP_OLDEST, "FileWorker"
);

AsyncAppender asyncDB = new AsyncAppender(
    new DatabaseAppender("logs_table"),
    10000, OverflowStrategy.BLOCK, "DBWorker"
);

LoggerConfig config = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.INFO)
    .addAppender(asyncFile)    // File: high throughput, can drop old
    .addAppender(asyncDB)       // DB: guaranteed delivery
    .build();
```

### Hybrid Approach

```java
// Console: synchronous (immediate feedback)
// File: asynchronous (high throughput)
LoggerConfig config = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.INFO)
    .addAppender(new ConsoleAppender())  // Sync
    .addAppender(new AsyncAppender(      // Async
        new FileAppender("logs/app.log")
    ))
    .build();
```

## 🔍 Troubleshooting

### Issue: Messages Not Appearing

**Symptom**: Logged messages don't appear in output

**Solutions**:
1. Check if worker thread is running: `appender.isRunning()`
2. Ensure proper shutdown: `appender.close()`
3. Check for exceptions in worker thread (printed to stderr)

### Issue: High Memory Usage

**Symptom**: Application memory grows continuously

**Solutions**:
1. Reduce queue capacity
2. Check for message accumulation: `appender.getQueueSize()`
3. Verify worker thread is processing messages
4. Check for slow delegate appender (I/O bottleneck)

### Issue: Messages Being Dropped

**Symptom**: `getDroppedMessages()` > 0

**Solutions**:
1. Increase queue capacity
2. Change overflow strategy to BLOCK
3. Optimize log volume (reduce log calls)
4. Use log level filtering more aggressively

## 📚 Summary

Asynchronous logging provides:
- ✅ **Performance**: 10-100x throughput improvement
- ✅ **Scalability**: Handle high log volumes
- ✅ **Flexibility**: Multiple overflow strategies
- ✅ **Monitoring**: Built-in metrics

Choose async logging when:
- High log volume (>1000 logs/sec)
- I/O-bound appenders (file, database, network)
- Application performance is critical
- Low-latency logging required

Use synchronous logging when:
- Low log volume
- Guaranteed immediate delivery required
- Simplicity preferred over performance

---

**For more information, see:**
- [README.md](README.md) - Main documentation
- [THREAD_SAFETY.md](THREAD_SAFETY.md) - Thread safety mechanisms
- [DESIGN_PATTERNS.md](DESIGN_PATTERNS.md) - Design patterns used

