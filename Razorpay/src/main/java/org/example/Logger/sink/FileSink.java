package org.example.Logger.sink;

import lombok.Getter;
import org.example.Logger.interfaces.LogSink;
import org.example.Logger.model.LogMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * File sink implementation - writes logs to a file
 */
@Getter
public class FileSink implements LogSink {
    private final String filePath;

    public FileSink(String filePath) {
        this.filePath = filePath;
        initializeFile();
    }

    private void initializeFile() {
        try {
            // Create directory if it doesn't exist
            if (filePath.contains("/") || filePath.contains("\\")) {
                String directory = filePath.substring(0, Math.max(filePath.lastIndexOf("/"), filePath.lastIndexOf("\\")));
                Files.createDirectories(Paths.get(directory));
            }
        } catch (IOException e) {
            System.err.println("Error initializing file sink: " + e.getMessage());
        }
    }

    @Override
    public void write(LogMessage logMessage) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            writer.println(logMessage.formatMessage());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public String getSinkType() {
        return "FILE";
    }

}




