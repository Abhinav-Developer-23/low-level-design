package org.example;

import org.example.appenders.*;
import org.example.config.LoggerConfig;
import org.example.enums.LogLevel;
import org.example.enums.OverflowStrategy;
import org.example.formatters.*;
import org.example.logger.Logger;
import org.example.logger.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Comprehensive demo class showcasing ALL logging framework features.
 * 
 * Demonstrates:
 * 1. Multiple log levels (TRACE, DEBUG, INFO, WARNING, ERROR, FATAL)
 * 2. Multiple appenders (Console, File, Database)
 * 3. Custom formatters (Simple, Detailed, JSON, Pattern)
 * 4. Configuration mechanism
 * 5. Thread-safety with concurrent logging
 * 6. Asynchronous logging with different overflow strategies
 * 7. Dynamic configuration updates
 * 8. Extensibility
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║  LOGGING FRAMEWORK - COMPLETE DEMO    ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Demo 1: Basic logging with different levels
        demo1BasicLogging();
        pause();

        // Demo 2: Multiple appenders
        demo2MultipleAppenders();
        pause();

        // Demo 3: Custom formatters
        demo3CustomFormatters();
        pause();

        // Demo 4: Log level filtering
        demo4LogLevelFiltering();
        pause();

        // Demo 5: Thread-safe concurrent logging
        demo5ConcurrentLogging();
        pause();

        // Demo 6: Asynchronous logging
        demo6AsyncLogging();
        pause();

        // Demo 7: Async with overflow strategies
        demo7AsyncOverflowStrategies();
        pause();

        // Demo 8: Dynamic configuration
        demo8DynamicConfiguration();
        pause();

        // Cleanup
        LoggerFactory.shutdownAll();
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║  ALL DEMOS COMPLETED SUCCESSFULLY ✓   ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    /**
     * Demo 1: Basic logging with all log levels
     */
    private static void demo1BasicLogging() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ Demo 1: Basic Logging (All Log Levels) │");
        System.out.println("└─────────────────────────────────────────┘");

        LoggerConfig config = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.TRACE)
                .addAppender(new ConsoleAppender())
                .build();

        LoggerFactory.setDefaultConfig(config);
        Logger logger = LoggerFactory.getLogger("BasicLogger");

        logger.trace("This is a TRACE message - most detailed");
        logger.debug("This is a DEBUG message - for debugging");
        logger.info("This is an INFO message - general information");
        logger.warning("This is a WARNING message - potential issues");
        logger.error("This is an ERROR message - error occurred");
        logger.fatal("This is a FATAL message - critical failure");

        System.out.println();
    }

    /**
     * Demo 2: Logging to multiple destinations simultaneously
     */
    private static void demo2MultipleAppenders() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ Demo 2: Multiple Appenders             │");
        System.out.println("└─────────────────────────────────────────┘");

        LoggerConfig config = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(new ConsoleAppender())
                .addAppender(new FileAppender("logs/application.log"))
                .addAppender(new DatabaseAppender("logs_table"))
                .build();

        Logger logger = LoggerFactory.getLogger("MultiAppenderLogger", config);

        logger.info("✓ Message logged to CONSOLE, FILE, and DATABASE simultaneously");
        logger.error("✓ Error logged to all three destinations");

        System.out.println();
    }

    /**
     * Demo 3: Custom formatters for different output formats
     */
    private static void demo3CustomFormatters() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ Demo 3: Custom Formatters              │");
        System.out.println("└─────────────────────────────────────────┘");

        // Simple formatter
        System.out.println("Simple Formatter:");
        LoggerConfig simpleConfig = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(new ConsoleAppender(new SimpleFormatter()))
                .build();
        Logger simpleLogger = LoggerFactory.getLogger("SimpleLogger", simpleConfig);
        simpleLogger.info("Simple format message");

        // Detailed formatter
        System.out.println("\nDetailed Formatter (with method and line):");
        LoggerConfig detailedConfig = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(new ConsoleAppender(new DetailedFormatter()))
                .build();
        Logger detailedLogger = LoggerFactory.getLogger("DetailedLogger", detailedConfig);
        detailedLogger.info("Detailed format message");

        // JSON formatter
        System.out.println("\nJSON Formatter:");
        LoggerConfig jsonConfig = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(new ConsoleAppender(new JsonFormatter()))
                .build();
        Logger jsonLogger = LoggerFactory.getLogger("JsonLogger", jsonConfig);
        jsonLogger.info("JSON format message");

        // Pattern formatter
        System.out.println("\nPattern Formatter (custom pattern):");
        PatternFormatter patternFormatter = new PatternFormatter(
            "%d{HH:mm:ss} | %level | %class.%method:%line | %msg"
        );
        LoggerConfig patternConfig = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(new ConsoleAppender(patternFormatter))
                .build();
        Logger patternLogger = LoggerFactory.getLogger("PatternLogger", patternConfig);
        patternLogger.info("Custom pattern message");

        System.out.println();
    }

    /**
     * Demo 4: Log level filtering
     */
    private static void demo4LogLevelFiltering() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ Demo 4: Log Level Filtering            │");
        System.out.println("└─────────────────────────────────────────┘");

        LoggerConfig config = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.WARNING)
                .addAppender(new ConsoleAppender())
                .build();

        Logger logger = LoggerFactory.getLogger("FilterLogger", config);

        System.out.println("Min level: WARNING (TRACE, DEBUG, INFO filtered out)\n");
        
        logger.trace("✗ TRACE - filtered out");
        logger.debug("✗ DEBUG - filtered out");
        logger.info("✗ INFO - filtered out");
        logger.warning("✓ WARNING - appears");
        logger.error("✓ ERROR - appears");
        logger.fatal("✓ FATAL - appears");

        System.out.println();
    }

    /**
     * Demo 5: Thread-safe concurrent logging from multiple threads
     */
    private static void demo5ConcurrentLogging() throws InterruptedException {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ Demo 5: Concurrent Logging (10 threads)│");
        System.out.println("└─────────────────────────────────────────┘");

        LoggerConfig config = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(new ConsoleAppender())
                .addAppender(new FileAppender("logs/concurrent.log"))
                .build();

        Logger logger = LoggerFactory.getLogger("ConcurrentLogger", config);

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 1; i <= 20; i++) {
            final int taskId = i;
            executor.submit(() -> {
                logger.info("Message from task " + taskId + " in thread " + 
                          Thread.currentThread().getName());
            });
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("✓ All 20 concurrent logging tasks completed safely\n");
    }

    /**
     * Demo 6: Asynchronous logging for better performance
     */
    private static void demo6AsyncLogging() throws InterruptedException {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ Demo 6: Asynchronous Logging           │");
        System.out.println("└─────────────────────────────────────────┘");

        // Wrap file appender with async appender
        FileAppender fileAppender = new FileAppender("logs/async.log");
        AsyncAppender asyncAppender = new AsyncAppender(
            fileAppender,
            1000,
            OverflowStrategy.BLOCK,
            "AsyncDemo"
        );

        LoggerConfig config = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(new ConsoleAppender())
                .addAppender(asyncAppender)
                .build();

        Logger logger = LoggerFactory.getLogger("AsyncLogger", config);

        System.out.println("Logging 100 messages asynchronously...");
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 1; i <= 100; i++) {
            logger.info("Async message #" + i);
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("✓ Non-blocking log calls completed in " + 
                         (endTime - startTime) + "ms");
        System.out.println("  Queue size: " + asyncAppender.getQueueSize());
        System.out.println("  Messages dropped: " + asyncAppender.getDroppedMessages());
        
        // Give time for async processing
        Thread.sleep(500);
        System.out.println();
    }

    /**
     * Demo 7: Async logging with different overflow strategies
     */
    private static void demo7AsyncOverflowStrategies() throws InterruptedException {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ Demo 7: Async Overflow Strategies      │");
        System.out.println("└─────────────────────────────────────────┘");

        // Small queue to trigger overflow
        int queueSize = 10;
        int messageCount = 20;

        // Strategy 1: DROP_NEWEST
        System.out.println("Strategy 1: DROP_NEWEST (queue size: " + queueSize + ")");
        AsyncAppender dropNewestAppender = new AsyncAppender(
            new ConsoleAppender(new SimpleFormatter()),
            queueSize,
            OverflowStrategy.DROP_NEWEST,
            "DropNewest"
        );

        LoggerConfig dropNewestConfig = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(dropNewestAppender)
                .build();

        Logger dropNewestLogger = LoggerFactory.getLogger("DropNewestLogger", dropNewestConfig);

        // Flood with messages
        for (int i = 1; i <= messageCount; i++) {
            dropNewestLogger.info("DROP_NEWEST message #" + i);
            Thread.sleep(1); // Small delay to show async behavior
        }

        Thread.sleep(200);
        System.out.println("  Dropped messages: " + dropNewestAppender.getDroppedMessages());
        dropNewestAppender.close();

        System.out.println("\nStrategy 2: LOG_AND_DROP");
        AsyncAppender logAndDropAppender = new AsyncAppender(
            new FileAppender("logs/log_and_drop.log"),
            queueSize,
            OverflowStrategy.LOG_AND_DROP,
            "LogAndDrop"
        );

        LoggerConfig logAndDropConfig = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(logAndDropAppender)
                .build();

        Logger logAndDropLogger = LoggerFactory.getLogger("LogAndDropLogger", logAndDropConfig);

        for (int i = 1; i <= messageCount; i++) {
            logAndDropLogger.info("LOG_AND_DROP message #" + i);
            Thread.sleep(1);
        }

        Thread.sleep(200);
        System.out.println("  Dropped messages: " + logAndDropAppender.getDroppedMessages());
        logAndDropAppender.close();

        System.out.println();
    }

    /**
     * Demo 8: Dynamic configuration updates at runtime
     */
    private static void demo8DynamicConfiguration() {
        System.out.println("┌─────────────────────────────────────────┐");
        System.out.println("│ Demo 8: Dynamic Configuration Update   │");
        System.out.println("└─────────────────────────────────────────┘");

        // Initial configuration: INFO level
        LoggerConfig initialConfig = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.INFO)
                .addAppender(new ConsoleAppender())
                .build();

        Logger logger = LoggerFactory.getLogger("DynamicLogger", initialConfig);

        System.out.println("Initial config - Min level: INFO");
        logger.debug("✗ DEBUG - filtered out");
        logger.info("✓ INFO - visible");

        // Update to DEBUG level
        System.out.println("\nUpdating configuration to DEBUG level...");
        LoggerConfig updatedConfig = new LoggerConfig.Builder()
                .setMinLogLevel(LogLevel.DEBUG)
                .addAppender(new ConsoleAppender())
                .build();

        logger.updateConfig(updatedConfig);

        System.out.println("Updated config - Min level: DEBUG");
        logger.debug("✓ DEBUG - now visible");
        logger.info("✓ INFO - still visible");

        System.out.println();
    }

    private static void pause() throws InterruptedException {
        Thread.sleep(1000);
    }
}
