package org.example.Logger.config;

import org.example.Logger.enums.LogLevel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple JSON parser for logger configuration
 * Uses manual parsing to avoid external dependencies
 */
public class ConfigReader {

    public static LoggerConfig readConfig(String filePath) throws IOException {
        String json;
        
        // Try to read from resources first, then from file system
        InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream != null) {
            json = readFromInputStream(inputStream);
        } else {
            json = readFromFile(filePath);
        }
        
        return parseConfig(json);
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static String readFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static LoggerConfig parseConfig(String json) {
        LoggerConfig config = new LoggerConfig();
        
        // Remove whitespace and newlines
        json = json.replaceAll("\\s+", "");
        
        // Extract logLevels object
        int logLevelsStart = json.indexOf("\"logLevels\":{") + 13;
        int logLevelsEnd = findMatchingBrace(json, logLevelsStart - 1);
        String logLevelsJson = json.substring(logLevelsStart, logLevelsEnd);
        
        // Parse each log level
        parseLogLevels(logLevelsJson, config);
        
        return config;
    }

    private static void parseLogLevels(String logLevelsJson, LoggerConfig config) {
        // Split by log level entries
        String[] levelEntries = logLevelsJson.split("(?=\"\\w+\":\\[)");
        
        for (String entry : levelEntries) {
            if (entry.trim().isEmpty()) continue;
            
            // Extract log level name
            int nameStart = entry.indexOf("\"") + 1;
            int nameEnd = entry.indexOf("\"", nameStart);
            if (nameStart <= 0 || nameEnd <= 0) continue;
            
            String levelName = entry.substring(nameStart, nameEnd);
            LogLevel logLevel;
            try {
                logLevel = LogLevel.valueOf(levelName.toUpperCase());
            } catch (IllegalArgumentException e) {
                continue; // Skip invalid log levels
            }
            
            // Extract sinks array
            int sinksStart = entry.indexOf("[");
            int sinksEnd = entry.lastIndexOf("]");
            if (sinksStart < 0 || sinksEnd < 0) continue;
            
            String sinksJson = entry.substring(sinksStart + 1, sinksEnd);
            List<LoggerConfig.SinkConfig> sinks = parseSinks(sinksJson);
            
            for (LoggerConfig.SinkConfig sink : sinks) {
                config.addSinkForLogLevel(logLevel, sink);
            }
        }
    }

    private static List<LoggerConfig.SinkConfig> parseSinks(String sinksJson) {
        List<LoggerConfig.SinkConfig> sinks = new ArrayList<>();
        
        // Split by sink objects
        int depth = 0;
        int start = 0;
        
        for (int i = 0; i < sinksJson.length(); i++) {
            char c = sinksJson.charAt(i);
            if (c == '{') {
                if (depth == 0) start = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    String sinkJson = sinksJson.substring(start + 1, i);
                    LoggerConfig.SinkConfig sink = parseSink(sinkJson);
                    if (sink != null) {
                        sinks.add(sink);
                    }
                }
            }
        }
        
        return sinks;
    }

    private static LoggerConfig.SinkConfig parseSink(String sinkJson) {
        LoggerConfig.SinkConfig sink = new LoggerConfig.SinkConfig();
        
        // Extract key-value pairs
        String[] pairs = sinkJson.split(",(?=\")");
        
        for (String pair : pairs) {
            int colonIndex = pair.indexOf(":");
            if (colonIndex < 0) continue;
            
            String key = pair.substring(0, colonIndex).replaceAll("\"", "").trim();
            String value = pair.substring(colonIndex + 1).replaceAll("\"", "").trim();
            
            if (key.equals("type")) {
                sink.setType(value);
            } else {
                sink.addProperty(key, value);
            }
        }
        
        return sink.getType() != null ? sink : null;
    }

    private static int findMatchingBrace(String json, int openBraceIndex) {
        int depth = 1;
        for (int i = openBraceIndex + 1; i < json.length(); i++) {
            if (json.charAt(i) == '{') depth++;
            else if (json.charAt(i) == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return json.length();
    }
}

