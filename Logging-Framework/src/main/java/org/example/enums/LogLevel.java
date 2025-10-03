package org.example.enums;

/**
 * Enumeration representing different log levels in ascending order of severity.
 * Each level has a priority value used for filtering.
 * Extensible - new levels can be added easily.
 */
public enum LogLevel {
    TRACE(0),
    DEBUG(1),
    INFO(2),
    WARNING(3),
    ERROR(4),
    FATAL(5);

    private final int priority;

    LogLevel(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * Checks if this log level is enabled for a given minimum level.
     */
    public boolean isEnabledFor(LogLevel minLevel) {
        return this.priority >= minLevel.priority;
    }
}

