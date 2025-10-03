package org.example.appenders;

import org.example.interfaces.LogFormatter;
import org.example.model.LogMessage;

import java.io.PrintStream;

/**
 * Appender that writes log messages to console output.
 * Thread-safe using synchronized method.
 */
public class ConsoleAppender extends AbstractLogAppender {
    private final PrintStream outputStream;

    public ConsoleAppender() {
        this(System.out);
    }

    public ConsoleAppender(PrintStream outputStream) {
        super();
        this.outputStream = outputStream;
    }

    public ConsoleAppender(LogFormatter formatter) {
        super(formatter);
        this.outputStream = System.out;
    }

    public ConsoleAppender(PrintStream outputStream, LogFormatter formatter) {
        super(formatter);
        this.outputStream = outputStream;
    }

    @Override
    protected synchronized void doAppend(LogMessage logMessage) {
        outputStream.println(formatter.format(logMessage));
    }

    @Override
    protected void doClose() {
        // Don't close System.out
        if (outputStream != System.out && outputStream != System.err) {
            outputStream.close();
        }
    }
}

