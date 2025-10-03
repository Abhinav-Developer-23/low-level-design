package org.example.formatters;

import org.example.interfaces.LogFormatter;
import org.example.model.LogMessage;

import java.time.format.DateTimeFormatter;

/**
 * Pattern-based log formatter allowing custom format strings.
 * 
 * Supported patterns:
 * - %d{format} : Date/time (e.g., %d{yyyy-MM-dd HH:mm:ss})
 * - %level : Log level
 * - %thread : Thread name
 * - %class : Class name
 * - %method : Method name
 * - %line : Line number
 * - %msg : Log message
 * - %n : Newline
 * 
 * Example: "%d{yyyy-MM-dd HH:mm:ss} [%level] %class.%method:%line - %msg"
 */
public class PatternFormatter implements LogFormatter {
    private final String pattern;
    private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public PatternFormatter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String format(LogMessage logMessage) {
        String result = pattern;
        
        // Replace date pattern
        if (result.contains("%d{")) {
            int start = result.indexOf("%d{");
            int end = result.indexOf("}", start);
            if (end > start) {
                String dateFormat = result.substring(start + 3, end);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
                String formattedDate = logMessage.getTimestamp().format(formatter);
                result = result.substring(0, start) + formattedDate + result.substring(end + 1);
            }
        } else if (result.contains("%d")) {
            result = result.replace("%d", logMessage.getTimestamp().format(DEFAULT_TIME_FORMATTER));
        }
        
        // Replace other patterns
        result = result.replace("%level", logMessage.getLevel().toString());
        result = result.replace("%thread", logMessage.getThreadName());
        result = result.replace("%class", 
            logMessage.getClassName() != null ? logMessage.getClassName() : "");
        result = result.replace("%method", 
            logMessage.getMethodName() != null ? logMessage.getMethodName() : "");
        result = result.replace("%line", 
            logMessage.getLineNumber() > 0 ? String.valueOf(logMessage.getLineNumber()) : "");
        result = result.replace("%msg", logMessage.getMessage());
        result = result.replace("%n", System.lineSeparator());
        
        return result;
    }

    public String getPattern() {
        return pattern;
    }
}

