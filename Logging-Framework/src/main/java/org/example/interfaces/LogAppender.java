package org.example.interfaces;

import org.example.model.LogMessage;

/**
 * Interface for log appenders (output destinations).
 * Follows Open/Closed Principle - open for extension with new appenders.
 * Follows Interface Segregation - single focused interface.
 */
public interface LogAppender {
    /**
     * Appends the log message to the destination.
     * @param logMessage the message to log
     */
    void append(LogMessage logMessage);

    /**
     * Closes the appender and releases any resources.
     */
    void close();
}

