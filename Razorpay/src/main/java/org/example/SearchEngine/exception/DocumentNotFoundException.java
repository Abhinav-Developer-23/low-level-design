package org.example.SearchEngine.exception;

/**
 * Exception thrown when a document is not found
 */
public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String documentId) {
        super("Document not found: " + documentId);
    }
}

