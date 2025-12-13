package org.example.Sql.exception;

/**
 * Exception thrown when a constraint validation fails.
 * Provides meaningful error messages for debugging.
 */
public class ConstraintViolationException extends RuntimeException {
    public ConstraintViolationException(String message) {
        super(message);
    }
    
    public ConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}




