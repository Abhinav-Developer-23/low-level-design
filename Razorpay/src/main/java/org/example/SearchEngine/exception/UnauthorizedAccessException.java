package org.example.SearchEngine.exception;

/**
 * Exception thrown when a user attempts an operation without proper authorization.
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
