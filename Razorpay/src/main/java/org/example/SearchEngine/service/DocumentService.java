package org.example.SearchEngine.service;

import org.example.SearchEngine.model.Document;

/**
 * Service interface for document operations
 * Following Single Responsibility Principle (SOLID)
 */
public interface DocumentService {
    /**
     * Add a document to a dataset
     */
    Document addDocument(String datasetId, String content);

    /**
     * Delete a document from a dataset
     */
    void deleteDocument(String datasetId, String documentId);

    /**
     * Get a document by ID from a dataset
     */
    Document getDocument(String datasetId, String documentId);
}




