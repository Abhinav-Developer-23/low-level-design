package org.example.SearchEngine.exception;

/**
 * Exception thrown when attempting to access a document that doesn't exist.
 */
public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
