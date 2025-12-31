package org.example.Logger.model;

import org.example.Logger.enums.LogLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogMessage {
    private final LogLevel level;
    private final String message;
    private final LocalDateTime timestamp;
    private final String threadName;
    private final String className;

    private LogMessage(Builder builder) {
        this.level = builder.level;
        this.message = builder.message;
        this.timestamp = builder.timestamp != null ? builder.timestamp : LocalDateTime.now();
        this.threadName = builder.threadName != null ? builder.threadName : Thread.currentThread().getName();
        this.className = builder.className;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getClassName() {
        return className;
    }

    public String formatMessage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(timestamp.format(formatter)).append("]");
        sb.append(" [").append(level).append("]");
        if (threadName != null) {
            sb.append(" [").append(threadName).append("]");
        }
        if (className != null) {
            sb.append(" [").append(className).append("]");
        }
        sb.append(" - ").append(message);
        return sb.toString();
    }

    public static class Builder {
        private LogLevel level;
        private String message;
        private LocalDateTime timestamp;
        private String threadName;
        private String className;

        public Builder level(LogLevel level) {
            this.level = level;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder threadName(String threadName) {
            this.threadName = threadName;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public LogMessage build() {
            if (level == null || message == null) {
                throw new IllegalArgumentException("LogLevel and message are required");
            }
            return new LogMessage(this);
        }
    }
}




