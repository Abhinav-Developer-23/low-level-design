package org.example.appenders;

import org.example.formatters.SimpleFormatter;
import org.example.interfaces.LogAppender;
import org.example.interfaces.LogFormatter;
import org.example.model.LogMessage;

/**
 * Abstract base class for log appenders.
 * Provides common functionality and template method pattern.
 * Follows DRY principle and enables inheritance-based extension.
 */
public abstract class AbstractLogAppender implements LogAppender {
    protected boolean closed = false;
    protected LogFormatter formatter;

    protected AbstractLogAppender() {
        this(new SimpleFormatter());
    }

    protected AbstractLogAppender(LogFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void append(LogMessage logMessage) {
        if (closed) {
            throw new IllegalStateException("Appender is closed");
        }
        doAppend(logMessage);
    }

    /**
     * Template method for subclasses to implement specific append logic.
     */
    protected abstract void doAppend(LogMessage logMessage);

    @Override
    public void close() {
        if (!closed) {
            doClose();
            closed = true;
        }
    }

    /**
     * Template method for subclasses to implement specific close logic.
     */
    protected void doClose() {
        // Default implementation does nothing
    }

    public void setFormatter(LogFormatter formatter) {
        this.formatter = formatter;
    }

    public LogFormatter getFormatter() {
        return formatter;
    }
}

