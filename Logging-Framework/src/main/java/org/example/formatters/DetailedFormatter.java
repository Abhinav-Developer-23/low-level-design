package org.example.formatters;

import org.example.interfaces.LogFormatter;
import org.example.model.LogMessage;

import java.time.format.DateTimeFormatter;

/**
 * Detailed log formatter with method name and line number.
 * Format: [timestamp] [level] [thread] [class.method:line] - message
 */
public class DetailedFormatter implements LogFormatter {
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(LogMessage logMessage) {
        String location = buildLocation(logMessage);
        
        return String.format("[%s] [%s] [%s] [%s] - %s",
                logMessage.getTimestamp().format(TIME_FORMATTER),
                logMessage.getLevel(),
                logMessage.getThreadName(),
                location,
                logMessage.getMessage());
    }

    private String buildLocation(LogMessage logMessage) {
        StringBuilder location = new StringBuilder();
        
        if (logMessage.getClassName() != null) {
            location.append(logMessage.getClassName());
        } else {
            location.append("Unknown");
        }
        
        if (logMessage.getMethodName() != null) {
            location.append(".").append(logMessage.getMethodName());
        }
        
        if (logMessage.getLineNumber() > 0) {
            location.append(":").append(logMessage.getLineNumber());
        }
        
        return location.toString();
    }
}

