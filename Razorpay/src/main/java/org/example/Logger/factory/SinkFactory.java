package org.example.Logger.factory;

import org.example.Logger.config.LoggerConfig;
import org.example.Logger.interfaces.LogSink;
import org.example.Logger.sink.ConsoleSink;
import org.example.Logger.sink.FileSink;
import org.example.Logger.sink.WebSink;

/**
 * Factory Pattern: Creates appropriate sink instances based on configuration
 */
public class SinkFactory {
    
    public static LogSink createSink(LoggerConfig.SinkConfig sinkConfig) {
        String type = sinkConfig.getType().toUpperCase();
        
        switch (type) {
            case "CONSOLE":
                return new ConsoleSink();
                
            case "FILE":
                String filePath = sinkConfig.getProperty("filePath");
                if (filePath == null) {
                    filePath = "logs/application.log"; // Default path
                }
                return new FileSink(filePath);
                
            case "WEB":
                String endpoint = sinkConfig.getProperty("endpoint");
                if (endpoint == null) {
                    endpoint = "http://localhost:8080/logs"; // Default endpoint
                }
                return new WebSink(endpoint);
                
            default:
                throw new IllegalArgumentException("Unknown sink type: " + type);
        }
    }
}





