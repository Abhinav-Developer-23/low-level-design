package org.example.interfaces;

import org.example.model.LogMessage;

/**
 * Interface for formatting log messages.
 * Follows Strategy Pattern - different formatting strategies can be implemented.
 * Allows custom formatting of log messages.
 */
public interface LogFormatter {
    /**
     * Formats a log message into a string representation.
     * @param logMessage the message to format
     * @return formatted string
     */
    String format(LogMessage logMessage);
}

