package org.example.Sql.exception;

/**
 * Exception thrown when a table schema is invalid or malformed.
 */
public class InvalidSchemaException extends RuntimeException {
    public InvalidSchemaException(String message) {
        super(message);
    }
}





