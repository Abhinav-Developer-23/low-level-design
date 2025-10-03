package org.example.filters;

import org.example.enums.LogLevel;
import org.example.interfaces.LogFilter;
import org.example.model.LogMessage;

/**
 * Filter that checks if log message level meets minimum threshold.
 * Implements Chain of Responsibility pattern.
 */
public class LogLevelFilter implements LogFilter {
    private final LogLevel minLevel;

    public LogLevelFilter(LogLevel minLevel) {
        this.minLevel = minLevel;
    }

    @Override
    public boolean shouldLog(LogMessage logMessage) {
        return logMessage.getLevel().isEnabledFor(minLevel);
    }

    public LogLevel getMinLevel() {
        return minLevel;
    }
}

