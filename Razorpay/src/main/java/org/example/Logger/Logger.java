package org.example.Logger;

import org.example.Logger.config.ConfigReader;
import org.example.Logger.config.LoggerConfig;
import org.example.Logger.enums.LogLevel;
import org.example.Logger.factory.SinkFactory;
import org.example.Logger.interfaces.LogSink;
import org.example.Logger.model.LogMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Main Logger class - Singleton Pattern
 * Routes logs to different sinks based on log level and configuration
 */
public class Logger {
    private static Logger instance;
    private final Map<LogLevel, List<LogSink>> logLevelSinkMap;
    private final ExecutorService executorService;
    private final boolean asyncLogging;

    private Logger(LoggerConfig config, boolean asyncLogging) {
        this.logLevelSinkMap = new HashMap<>();
        this.asyncLogging = asyncLogging;
        this.executorService = asyncLogging ? Executors.newFixedThreadPool(5) : null;
        initializeSinks(config);
    }

    /**
     * Initialize logger with configuration file
     */
    public static synchronized Logger initialize(String configFilePath) throws IOException {
        return initialize(configFilePath, false);
    }

    /**
     * Initialize logger with configuration file and async option
     */
    public static synchronized Logger initialize(String configFilePath, boolean asyncLogging) throws IOException {
        if (instance != null) {
            throw new IllegalStateException("Logger already initialized");
        }
        LoggerConfig config = ConfigReader.readConfig(configFilePath);
        instance = new Logger(config, asyncLogging);
        return instance;
    }

    /**
     * Get logger instance - must be initialized first
     */
    public static Logger getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Logger not initialized. Call initialize() first.");
        }
        return instance;
    }

    /**
     * Initialize sinks from configuration
     */
    private void initializeSinks(LoggerConfig config) {
        for (Map.Entry<LogLevel, List<LoggerConfig.SinkConfig>> entry : config.getLogLevelSinkMap().entrySet()) {
            LogLevel level = entry.getKey();
            List<LogSink> sinks = new ArrayList<>();
            
            for (LoggerConfig.SinkConfig sinkConfig : entry.getValue()) {
                LogSink sink = SinkFactory.createSink(sinkConfig);
                sinks.add(sink);
            }
            
            logLevelSinkMap.put(level, sinks);
        }
    }

    /**
     * Log a message with specified level
     */
    public void log(LogLevel level, String message) {
        log(level, message, null);
    }

    /**
     * Log a message with specified level and class name
     */
    public void log(LogLevel level, String message, String className) {
        LogMessage logMessage = new LogMessage.Builder()
                .level(level)
                .message(message)
                .className(className)
                .build();

        List<LogSink> sinks = logLevelSinkMap.get(level);
        if (sinks == null || sinks.isEmpty()) {
            // Fallback to console if no sinks configured for this level
            System.out.println("[NO SINK CONFIGURED] " + logMessage.formatMessage());
            return;
        }

        if (asyncLogging) {
            for (LogSink sink : sinks) {
                executorService.submit(() -> sink.write(logMessage));
            }
        } else {
            for (LogSink sink : sinks) {
                sink.write(logMessage);
            }
        }
    }

    /**
     * Convenience method for INFO level
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Convenience method for WARNING level
     */
    public void warning(String message) {
        log(LogLevel.WARNING, message);
    }

    /**
     * Convenience method for ERROR level
     */
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }

    /**
     * Convenience method for DEBUG level
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Convenience method for DELETE level
     */
    public void delete(String message) {
        log(LogLevel.DELETE, message);
    }

    /**
     * Shutdown the logger gracefully (for async logging)
     */
    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Reset the logger instance (useful for testing)
     */
    public static synchronized void reset() {
        if (instance != null) {
            instance.shutdown();
            instance = null;
        }
    }
}




