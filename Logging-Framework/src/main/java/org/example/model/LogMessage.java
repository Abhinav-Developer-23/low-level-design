package org.example.model;

import org.example.enums.LogLevel;

import java.time.LocalDateTime;

/**
 * Immutable class representing a log message with timestamp, level, and content.
 * Follows immutability principle for thread-safety.
 */
public class LogMessage {
    private final LocalDateTime timestamp;
    private final LogLevel level;
    private final String message;
    private final String threadName;
    private final String className;
    private final String methodName;
    private final int lineNumber;

    private LogMessage(Builder builder) {
        this.timestamp = builder.timestamp;
        this.level = builder.level;
        this.message = builder.message;
        this.threadName = builder.threadName;
        this.className = builder.className;
        this.methodName = builder.methodName;
        this.lineNumber = builder.lineNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Builder pattern for creating LogMessage instances.
     */
    public static class Builder {
        private LocalDateTime timestamp;
        private LogLevel level;
        private String message;
        private String threadName;
        private String className;
        private String methodName;
        private int lineNumber;

        public Builder() {
            this.timestamp = LocalDateTime.now();
            this.threadName = Thread.currentThread().getName();
        }

        public Builder level(LogLevel level) {
            this.level = level;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder lineNumber(int lineNumber) {
            this.lineNumber = lineNumber;
            return this;
        }

        public Builder threadName(String threadName) {
            this.threadName = threadName;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public LogMessage build() {
            if (level == null || message == null) {
                throw new IllegalStateException("Level and message are required");
            }
            return new LogMessage(this);
        }
    }
}

