package org.example.SearchEngine.exception;

/**
 * Exception thrown when attempting to access a dataset that doesn't exist.
 */
public class DatasetNotFoundException extends RuntimeException {
    public DatasetNotFoundException(String message) {
        super(message);
    }
}
