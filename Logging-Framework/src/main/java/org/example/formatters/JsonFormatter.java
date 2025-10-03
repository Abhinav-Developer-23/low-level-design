package org.example.formatters;

import org.example.interfaces.LogFormatter;
import org.example.model.LogMessage;

import java.time.format.DateTimeFormatter;

/**
 * JSON log formatter for structured logging.
 * Outputs log messages in JSON format for easy parsing and analysis.
 */
public class JsonFormatter implements LogFormatter {
    private static final DateTimeFormatter TIME_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Override
    public String format(LogMessage logMessage) {
        StringBuilder json = new StringBuilder("{");
        
        json.append("\"timestamp\":\"")
            .append(logMessage.getTimestamp().format(TIME_FORMATTER))
            .append("\",");
        
        json.append("\"level\":\"")
            .append(logMessage.getLevel())
            .append("\",");
        
        json.append("\"thread\":\"")
            .append(escapeJson(logMessage.getThreadName()))
            .append("\",");
        
        if (logMessage.getClassName() != null) {
            json.append("\"class\":\"")
                .append(escapeJson(logMessage.getClassName()))
                .append("\",");
        }
        
        if (logMessage.getMethodName() != null) {
            json.append("\"method\":\"")
                .append(escapeJson(logMessage.getMethodName()))
                .append("\",");
        }
        
        if (logMessage.getLineNumber() > 0) {
            json.append("\"line\":")
                .append(logMessage.getLineNumber())
                .append(",");
        }
        
        json.append("\"message\":\"")
            .append(escapeJson(logMessage.getMessage()))
            .append("\"");
        
        json.append("}");
        
        return json.toString();
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }
}

