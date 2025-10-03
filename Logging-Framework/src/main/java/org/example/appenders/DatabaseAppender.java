package org.example.appenders;

import org.example.interfaces.LogFormatter;
import org.example.model.LogMessage;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Appender that writes log messages to a database.
 * This is a simulation - in production, would use JDBC or ORM.
 * Thread-safe using synchronized collections and methods.
 * 
 * Demonstrates extensibility - can be replaced with real database implementation.
 */
public class DatabaseAppender extends AbstractLogAppender {
    private final List<String> logStore; // Simulated database
    private final String tableName;

    public DatabaseAppender(String tableName) {
        super();
        this.tableName = tableName;
        this.logStore = Collections.synchronizedList(new ArrayList<>());
        System.out.println("DatabaseAppender initialized for table: " + tableName);
    }

    public DatabaseAppender(String tableName, LogFormatter formatter) {
        super(formatter);
        this.tableName = tableName;
        this.logStore = Collections.synchronizedList(new ArrayList<>());
        System.out.println("DatabaseAppender initialized for table: " + tableName);
    }

    @Override
    protected synchronized void doAppend(LogMessage logMessage) {
        // Simulate database insert
        String record = String.format("INSERT INTO %s (timestamp, level, thread, class, message) VALUES ('%s', '%s', '%s', '%s', '%s')",
                tableName,
                logMessage.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")),
                logMessage.getLevel(),
                logMessage.getThreadName(),
                logMessage.getClassName(),
                logMessage.getMessage().replace("'", "''") // Escape single quotes
        );
        
        logStore.add(record);
        
        // In production, would execute:
        // connection.prepareStatement(sql).executeUpdate();
    }

    @Override
    protected void doClose() {
        System.out.println("DatabaseAppender closed. Total records: " + logStore.size());
        // In production, would close database connection
    }

    /**
     * Retrieves all log records (for testing/demo purposes).
     */
    public List<String> getAllRecords() {
        return new ArrayList<>(logStore);
    }

    public String getTableName() {
        return tableName;
    }
}

