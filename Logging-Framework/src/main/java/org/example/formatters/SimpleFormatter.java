package org.example.formatters;

import org.example.interfaces.LogFormatter;
import org.example.model.LogMessage;

import java.time.format.DateTimeFormatter;

/**
 * Simple log formatter with basic formatting.
 * Format: [timestamp] [level] [thread] [class] - message
 */
public class SimpleFormatter implements LogFormatter {
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(LogMessage logMessage) {
        return String.format("[%s] [%s] [%s] [%s] - %s",
                logMessage.getTimestamp().format(TIME_FORMATTER),
                logMessage.getLevel(),
                logMessage.getThreadName(),
                logMessage.getClassName() != null ? logMessage.getClassName() : "Unknown",
                logMessage.getMessage());
    }
}

