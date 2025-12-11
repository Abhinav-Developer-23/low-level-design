package org.example.SearchEngine.exception;

/**
 * Exception thrown when a dataset is not found
 */
public class DatasetNotFoundException extends RuntimeException {
    public DatasetNotFoundException(String datasetId) {
        super("Dataset not found: " + datasetId);
    }
}

