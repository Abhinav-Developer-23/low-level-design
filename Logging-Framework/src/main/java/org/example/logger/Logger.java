package org.example.logger;

import org.example.config.LoggerConfig;
import org.example.enums.LogLevel;
import org.example.filters.LogLevelFilter;
import org.example.interfaces.LogAppender;
import org.example.interfaces.LogFilter;
import org.example.model.LogMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Main Logger class that manages log message processing.
 * Thread-safe using ReentrantReadWriteLock for better concurrent performance.
 * Follows Singleton pattern (via LoggerFactory).
 * Implements Observer pattern by notifying multiple appenders.
 */
public class Logger {
    private final String name;
    private LoggerConfig config;
    private final List<LogFilter> filters;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Logger(String name, LoggerConfig config) {
        this.name = name;
        this.config = config;
        this.filters = new ArrayList<>();
        
        // Add default log level filter
        filters.add(new LogLevelFilter(config.getMinLogLevel()));
    }

    /**
     * Updates the logger configuration.
     * Thread-safe using write lock.
     */
    public void updateConfig(LoggerConfig newConfig) {
        lock.writeLock().lock();
        try {
            this.config = newConfig;
            // Update the log level filter
            filters.clear();
            filters.add(new LogLevelFilter(newConfig.getMinLogLevel()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Adds a custom filter.
     * Thread-safe using write lock.
     */
    public void addFilter(LogFilter filter) {
        lock.writeLock().lock();
        try {
            filters.add(filter);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Convenience methods for each log level
    public void trace(String message) {
        log(LogLevel.TRACE, message);
    }

    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    public void warning(String message) {
        log(LogLevel.WARNING, message);
    }

    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    public void fatal(String message) {
        log(LogLevel.FATAL, message);
    }

    /**
     * Main logging method.
     * Thread-safe using read lock for concurrent logging.
     */
    public void log(LogLevel level, String message) {
        // Get caller information
        StackTraceElement caller = getCallerStackTrace();
        String className = caller != null ? caller.getClassName() : "Unknown";
        String methodName = caller != null ? caller.getMethodName() : "Unknown";
        int lineNumber = caller != null ? caller.getLineNumber() : 0;

        // Build log message
        LogMessage logMessage = new LogMessage.Builder()
                .level(level)
                .message(message)
                .className(className)
                .methodName(methodName)
                .lineNumber(lineNumber)
                .build();

        lock.readLock().lock();
        try {
            // Apply filters
            for (LogFilter filter : filters) {
                if (!filter.shouldLog(logMessage)) {
                    return; // Message filtered out
                }
            }

            // Notify all appenders (Observer pattern)
            for (LogAppender appender : config.getAppenders()) {
                try {
                    appender.append(logMessage);
                } catch (Exception e) {
                    System.err.println("Error in appender: " + e.getMessage());
                }
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Gets the caller's stack trace element.
     */
    private StackTraceElement getCallerStackTrace() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        // Skip getStackTrace(), getCallerStackTrace(), log(), and the convenience method
        for (int i = 3; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            if (!element.getClassName().equals(this.getClass().getName())) {
                return element;
            }
        }
        return null;
    }

    /**
     * Closes all appenders and releases resources.
     */
    public void shutdown() {
        lock.writeLock().lock();
        try {
            for (LogAppender appender : config.getAppenders()) {
                try {
                    appender.close();
                } catch (Exception e) {
                    System.err.println("Error closing appender: " + e.getMessage());
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String getName() {
        return name;
    }

    public LogLevel getMinLogLevel() {
        lock.readLock().lock();
        try {
            return config.getMinLogLevel();
        } finally {
            lock.readLock().unlock();
        }
    }
}

