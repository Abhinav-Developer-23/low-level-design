package org.example.logger;

import org.example.config.LoggerConfig;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory class for creating and managing Logger instances.
 * Implements Singleton pattern for each logger name.
 * Thread-safe using ConcurrentHashMap.
 * Follows Factory pattern and Dependency Inversion principle.
 */
public class LoggerFactory {
    private static final ConcurrentHashMap<String, Logger> loggers = new ConcurrentHashMap<>();
    private static volatile LoggerConfig defaultConfig;

    // Private constructor to prevent instantiation
    private LoggerFactory() {
    }

    /**
     * Sets the default configuration for new loggers.
     */
    public static void setDefaultConfig(LoggerConfig config) {
        defaultConfig = config;
    }

    /**
     * Gets a logger by name. Creates one if it doesn't exist.
     * Thread-safe using ConcurrentHashMap.computeIfAbsent.
     */
    public static Logger getLogger(String name) {
        if (defaultConfig == null) {
            throw new IllegalStateException("Default config must be set before getting loggers. Call LoggerFactory.setDefaultConfig() first.");
        }
        return loggers.computeIfAbsent(name, n -> new Logger(n, defaultConfig));
    }

    /**
     * Gets a logger for a specific class.
     */
    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    /**
     * Creates a logger with custom configuration.
     */
    public static Logger getLogger(String name, LoggerConfig config) {
        return loggers.computeIfAbsent(name, n -> new Logger(n, config));
    }

    /**
     * Updates configuration for an existing logger.
     */
    public static void updateLoggerConfig(String name, LoggerConfig config) {
        Logger logger = loggers.get(name);
        if (logger != null) {
            logger.updateConfig(config);
        }
    }

    /**
     * Shuts down all loggers and releases resources.
     */
    public static void shutdownAll() {
        for (Logger logger : loggers.values()) {
            try {
                logger.shutdown();
            } catch (Exception e) {
                System.err.println("Error shutting down logger " + logger.getName() + ": " + e.getMessage());
            }
        }
        loggers.clear();
    }

    /**
     * Gets all registered loggers (for testing/monitoring).
     */
    public static ConcurrentHashMap<String, Logger> getAllLoggers() {
        return new ConcurrentHashMap<>(loggers);
    }
}

