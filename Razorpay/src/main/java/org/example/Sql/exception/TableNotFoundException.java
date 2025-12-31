package org.example.Sql.exception;

/**
 * Exception thrown when attempting to operate on a non-existent table.
 */
public class TableNotFoundException extends RuntimeException {
    public TableNotFoundException(String message) {
        super(message);
    }
}






