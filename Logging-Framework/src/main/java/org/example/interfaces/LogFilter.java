package org.example.interfaces;

import org.example.model.LogMessage;

/**
 * Interface for filtering log messages.
 * Implements Chain of Responsibility pattern.
 */
public interface LogFilter {
    /**
     * Determines if a log message should be logged.
     * @param logMessage the message to check
     * @return true if the message should be logged, false otherwise
     */
    boolean shouldLog(LogMessage logMessage);
}

