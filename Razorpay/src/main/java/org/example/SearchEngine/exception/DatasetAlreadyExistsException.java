package org.example.SearchEngine.exception;

/**
 * Exception thrown when trying to create a dataset that already exists
 */
public class DatasetAlreadyExistsException extends RuntimeException {
    public DatasetAlreadyExistsException(String datasetId) {
        super("Dataset already exists: " + datasetId);
    }
}




