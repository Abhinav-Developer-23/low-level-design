# Quick Start Guide

Get up and running with the Logging Framework in 5 minutes!

## 🚀 5-Minute Quick Start

### Step 1: Basic Setup

```java
import org.example.appenders.ConsoleAppender;
import org.example.config.LoggerConfig;
import org.example.enums.LogLevel;
import org.example.logger.Logger;
import org.example.logger.LoggerFactory;

// Configure (do this once at application startup)
LoggerConfig config = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.INFO)
    .addAppender(new ConsoleAppender())
    .build();

LoggerFactory.setDefaultConfig(config);

// Get logger
Logger logger = LoggerFactory.getLogger("MyApp");

// Start logging!
logger.info("Application started");
logger.error("An error occurred");
logger.warning("Low disk space");
```

**Output:**
```
[2024-10-03 14:23:45.123] [INFO] [main] [MyApp] - Application started
[2024-10-03 14:23:45.124] [ERROR] [main] [MyApp] - An error occurred
[2024-10-03 14:23:45.125] [WARNING] [main] [MyApp] - Low disk space
```

### Step 2: Log to File

```java
import org.example.appenders.FileAppender;

LoggerConfig config = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.INFO)
    .addAppender(new FileAppender("logs/application.log"))
    .build();

Logger logger = LoggerFactory.getLogger("FileLogger", config);
logger.info("This is saved to file");
```

### Step 3: Multiple Destinations

```java
LoggerConfig config = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.INFO)
    .addAppender(new ConsoleAppender())        // Console
    .addAppender(new FileAppender("app.log"))  // File
    .addAppender(new DatabaseAppender("logs")) // Database
    .build();

Logger logger = LoggerFactory.getLogger("MultiLogger", config);
logger.info("Logged everywhere!");
```

### Step 4: Asynchronous Logging 🔥

```java
import org.example.appenders.AsyncAppender;
import org.example.enums.OverflowStrategy;

// Wrap any appender for async processing
AsyncAppender asyncAppender = new AsyncAppender(
    new FileAppender("logs/async.log"),
    10000,                          // Queue size
    OverflowStrategy.BLOCK,         // Overflow strategy
    "AsyncWorker"                   // Thread name
);

LoggerConfig config = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.INFO)
    .addAppender(asyncAppender)
    .build();

Logger logger = LoggerFactory.getLogger("AsyncLogger", config);

// Non-blocking, high-performance logging!
logger.info("This returns immediately");
```

## 📝 All Log Levels

```java
logger.trace("Most detailed - for tracing execution");
logger.debug("Debugging information");
logger.info("General information");
logger.warning("Warning - potential issue");
logger.error("Error occurred");
logger.fatal("Critical failure");
```

## 🎨 Custom Formatters

### Simple Format
```java
import org.example.formatters.SimpleFormatter;

new ConsoleAppender(new SimpleFormatter());
```
Output: `[2024-10-03 14:23:45.123] [INFO] [main] [MyApp] - Message`

### JSON Format
```java
import org.example.formatters.JsonFormatter;

new ConsoleAppender(new JsonFormatter());
```
Output: `{"timestamp":"2024-10-03T14:23:45.123","level":"INFO","message":"..."}`

### Pattern Format
```java
import org.example.formatters.PatternFormatter;

PatternFormatter formatter = new PatternFormatter(
    "%d{HH:mm:ss} | %level | %msg"
);
new ConsoleAppender(formatter);
```
Output: `14:23:45 | INFO | Message`

## 🎯 Common Scenarios

### Development Configuration
```java
LoggerConfig devConfig = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.DEBUG)     // Show everything
    .addAppender(new ConsoleAppender()) // Console only
    .build();
```

### Production Configuration
```java
LoggerConfig prodConfig = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.WARNING)   // Only warnings+
    .addAppender(new AsyncAppender(     // Async for performance
        new FileAppender("logs/production.log")
    ))
    .addAppender(new AsyncAppender(     // Database for critical logs
        new DatabaseAppender("logs_table")
    ))
    .build();
```

### High-Performance Configuration
```java
AsyncAppender asyncConsole = new AsyncAppender(
    new ConsoleAppender(),
    50000,                          // Large queue
    OverflowStrategy.DROP_OLDEST,   // Prefer recent logs
    "ConsoleWorker"
);

AsyncAppender asyncFile = new AsyncAppender(
    new FileAppender("logs/app.log"),
    100000,                         // Very large queue
    OverflowStrategy.DROP_OLDEST,
    "FileWorker"
);

LoggerConfig config = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.INFO)
    .addAppender(asyncConsole)
    .addAppender(asyncFile)
    .build();

// Can handle 1M+ logs/sec!
```

## 🧵 Thread-Safe Usage

```java
// Safe to use from multiple threads
Logger logger = LoggerFactory.getLogger("ThreadSafe");

ExecutorService executor = Executors.newFixedThreadPool(100);
for (int i = 0; i < 10000; i++) {
    final int taskId = i;
    executor.submit(() -> {
        logger.info("Task " + taskId + " completed");
    });
}
executor.shutdown();
```

## 📊 Monitoring Async Performance

```java
AsyncAppender appender = new AsyncAppender(/* ... */);

// Check queue status
System.out.println("Queue size: " + appender.getQueueSize());
System.out.println("Queue capacity: " + appender.getQueueCapacity());
System.out.println("Dropped messages: " + appender.getDroppedMessages());
System.out.println("Worker running: " + appender.isRunning());

// Alert if queue is filling up
if (appender.getQueueSize() > appender.getQueueCapacity() * 0.8) {
    System.err.println("WARNING: Queue is 80% full!");
}
```

## 🔄 Dynamic Configuration

```java
Logger logger = LoggerFactory.getLogger("Dynamic");

// Start with INFO level
logger.debug("Not visible");
logger.info("Visible");

// Update to DEBUG level at runtime
LoggerConfig newConfig = new LoggerConfig.Builder()
    .setMinLogLevel(LogLevel.DEBUG)
    .addAppender(new ConsoleAppender())
    .build();

logger.updateConfig(newConfig);

// Now debug messages appear
logger.debug("Now visible!");
```

## 🛑 Cleanup

**Always shutdown before application exit:**

```java
// At application shutdown
LoggerFactory.shutdownAll();

// Ensures:
// - All async queues are flushed
// - All files are closed
// - All resources are released
```

## ⚙️ Overflow Strategies

| Strategy | Behavior | Best For |
|----------|----------|----------|
| `BLOCK` | Wait for space | Critical logs (no loss) |
| `DROP_OLDEST` | Remove old, add new | Recent logs important |
| `DROP_NEWEST` | Drop new message | Historical logs important |
| `LOG_AND_DROP` | Log error, drop | Development/monitoring |

```java
// Example: Critical logs that must not be lost
AsyncAppender critical = new AsyncAppender(
    delegate,
    10000,
    OverflowStrategy.BLOCK,  // Wait, don't drop
    "CriticalWorker"
);

// Example: High-volume metrics where recent is important
AsyncAppender metrics = new AsyncAppender(
    delegate,
    100000,
    OverflowStrategy.DROP_OLDEST,  // Keep recent
    "MetricsWorker"
);
```

## 📖 Complete Example

```java
package com.example;

import org.example.appenders.*;
import org.example.config.LoggerConfig;
import org.example.enums.LogLevel;
import org.example.enums.OverflowStrategy;
import org.example.formatters.JsonFormatter;
import org.example.logger.Logger;
import org.example.logger.LoggerFactory;

public class MyApplication {
    private static final Logger logger;
    
    static {
        // Configuration
        AsyncAppender asyncFile = new AsyncAppender(
            new FileAppender("logs/app.log"),
            25000,
            OverflowStrategy.DROP_OLDEST,
            "FileWorker"
        );
        
        AsyncAppender asyncDB = new AsyncAppender(
            new DatabaseAppender("logs_table"),
            10000,
            OverflowStrategy.BLOCK,
            "DBWorker"
        );
        
        LoggerConfig config = new LoggerConfig.Builder()
            .setMinLogLevel(LogLevel.INFO)
            .addAppender(new ConsoleAppender())
            .addAppender(asyncFile)
            .addAppender(asyncDB)
            .build();
        
        LoggerFactory.setDefaultConfig(config);
        logger = LoggerFactory.getLogger(MyApplication.class);
    }
    
    public static void main(String[] args) {
        logger.info("Application starting...");
        
        try {
            processData();
            logger.info("Data processed successfully");
        } catch (Exception e) {
            logger.error("Failed to process data: " + e.getMessage());
        } finally {
            logger.info("Application shutting down...");
            LoggerFactory.shutdownAll();
        }
    }
    
    private static void processData() {
        logger.debug("Processing data...");
        // Business logic
    }
}
```

## 🎓 Best Practices

1. ✅ **Configure once** at application startup
2. ✅ **Use class-based loggers**: `LoggerFactory.getLogger(MyClass.class)`
3. ✅ **Use appropriate log levels**: DEBUG for dev, INFO+ for production
4. ✅ **Use async for high volume**: 10-100x performance improvement
5. ✅ **Always shutdown gracefully**: `LoggerFactory.shutdownAll()`
6. ✅ **Monitor async queues**: Check queue size and dropped messages
7. ✅ **Choose right overflow strategy**: BLOCK for critical, DROP_OLDEST for high volume

## 🚨 Troubleshooting

### Messages not appearing?
```java
// Check log level
logger.getMinLogLevel();  // Are you below this level?

// Check async queue
asyncAppender.getQueueSize();  // Messages waiting?
asyncAppender.isRunning();     // Worker thread alive?
```

### High memory usage?
```java
// Reduce queue capacity
new AsyncAppender(delegate, 5000, ...);  // Smaller queue

// Check for message buildup
if (appender.getQueueSize() > 10000) {
    logger.warning("Queue backing up!");
}
```

### Messages being dropped?
```java
// Check dropped count
if (appender.getDroppedMessages() > 0) {
    // Increase queue size OR change strategy to BLOCK
}
```

## 📚 Next Steps

- Read [README.md](README.md) for complete documentation
- See [ASYNC_LOGGING.md](ASYNC_LOGGING.md) for async deep dive
- Review [Main.java](src/main/java/org/example/Main.java) for 8 demo scenarios
- Check [IMPLEMENTATION_SUMMARY.md](IMPLEMENTATION_SUMMARY.md) for overview

---

**Happy Logging! 🚀**

