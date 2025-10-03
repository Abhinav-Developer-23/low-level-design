package org.example.config;

import org.example.enums.LogLevel;
import org.example.interfaces.LogAppender;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for Logger.
 * Follows Builder pattern for fluent configuration.
 * Immutable after build for thread-safety.
 */
public class LoggerConfig {
    private final LogLevel minLogLevel;
    private final List<LogAppender> appenders;

    private LoggerConfig(Builder builder) {
        this.minLogLevel = builder.minLogLevel;
        this.appenders = List.copyOf(builder.appenders);
    }

    public LogLevel getMinLogLevel() {
        return minLogLevel;
    }

    public List<LogAppender> getAppenders() {
        return appenders;
    }

    /**
     * Builder for creating LoggerConfig instances.
     */
    public static class Builder {
        private LogLevel minLogLevel = LogLevel.INFO; // Default
        private List<LogAppender> appenders = new ArrayList<>();

        public Builder setMinLogLevel(LogLevel minLogLevel) {
            this.minLogLevel = minLogLevel;
            return this;
        }

        public Builder addAppender(LogAppender appender) {
            this.appenders.add(appender);
            return this;
        }

        public Builder setAppenders(List<LogAppender> appenders) {
            this.appenders = new ArrayList<>(appenders);
            return this;
        }

        public LoggerConfig build() {
            if (appenders.isEmpty()) {
                throw new IllegalStateException("At least one appender must be configured");
            }
            return new LoggerConfig(this);
        }
    }
}

