package org.example.Logger.sink;

import org.example.Logger.interfaces.LogSink;
import org.example.Logger.model.LogMessage;

/**
 * Console sink implementation - writes logs to console/stdout
 */
public class ConsoleSink implements LogSink {
    
    @Override
    public void write(LogMessage logMessage) {
        System.out.println("[CONSOLE] " + logMessage.formatMessage());
    }

    @Override
    public String getSinkType() {
        return "CONSOLE";
    }
}





