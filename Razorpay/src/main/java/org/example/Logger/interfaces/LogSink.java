package org.example.Logger.interfaces;

import org.example.Logger.model.LogMessage;

/**
 * Strategy Pattern: Interface for different logging destinations
 */
public interface LogSink {
    void write(LogMessage logMessage);
    String getSinkType();
}





