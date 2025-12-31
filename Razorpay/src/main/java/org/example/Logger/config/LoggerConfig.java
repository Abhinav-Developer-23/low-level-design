package org.example.Logger.config;

import org.example.Logger.enums.LogLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration class that maps log levels to their destinations
 */
public class LoggerConfig {
    private Map<LogLevel, List<SinkConfig>> logLevelSinkMap;

    public LoggerConfig() {
        this.logLevelSinkMap = new HashMap<>();
    }

    public void addSinkForLogLevel(LogLevel level, SinkConfig sinkConfig) {
        logLevelSinkMap.computeIfAbsent(level, k -> new ArrayList<>()).add(sinkConfig);
    }

    public List<SinkConfig> getSinksForLogLevel(LogLevel level) {
        return logLevelSinkMap.getOrDefault(level, new ArrayList<>());
    }

    public Map<LogLevel, List<SinkConfig>> getLogLevelSinkMap() {
        return logLevelSinkMap;
    }

    public void setLogLevelSinkMap(Map<LogLevel, List<SinkConfig>> logLevelSinkMap) {
        this.logLevelSinkMap = logLevelSinkMap;
    }

    public static class SinkConfig {
        private String type; // CONSOLE, FILE, WEB
        private Map<String, String> properties; // filePath for FILE, endpoint for WEB

        public SinkConfig() {
            this.properties = new HashMap<>();
        }

        public SinkConfig(String type) {
            this.type = type;
            this.properties = new HashMap<>();
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        public String getProperty(String key) {
            return properties.get(key);
        }

        public void addProperty(String key, String value) {
            properties.put(key, value);
        }
    }
}




