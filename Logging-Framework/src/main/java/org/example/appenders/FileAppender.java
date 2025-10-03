package org.example.appenders;

import org.example.interfaces.LogFormatter;
import org.example.model.LogMessage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Appender that writes log messages to a file.
 * Thread-safe using synchronized method.
 * Implements proper resource management.
 */
public class FileAppender extends AbstractLogAppender {
    private final String filePath;
    private BufferedWriter writer;

    public FileAppender(String filePath) {
        super();
        this.filePath = filePath;
        initializeWriter();
    }

    public FileAppender(String filePath, LogFormatter formatter) {
        super(formatter);
        this.filePath = filePath;
        initializeWriter();
    }

    private void initializeWriter() {
        try {
            // Ensure parent directory exists
            Path path = Paths.get(filePath);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            
            this.writer = new BufferedWriter(new FileWriter(filePath, true));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize FileAppender: " + e.getMessage(), e);
        }
    }

    @Override
    protected synchronized void doAppend(LogMessage logMessage) {
        try {
            writer.write(formatter.format(logMessage));
            writer.newLine();
            writer.flush(); // Ensure immediate write for reliability
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    @Override
    protected void doClose() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("Failed to close log file: " + e.getMessage());
            }
        }
    }

    public String getFilePath() {
        return filePath;
    }
}

