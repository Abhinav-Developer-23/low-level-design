package org.example.SearchEngine.exception;

/**
 * Exception thrown when attempting to create a dataset that already exists.
 */
public class DatasetAlreadyExistsException extends RuntimeException {
    public DatasetAlreadyExistsException(String message) {
        super(message);
    }
}
