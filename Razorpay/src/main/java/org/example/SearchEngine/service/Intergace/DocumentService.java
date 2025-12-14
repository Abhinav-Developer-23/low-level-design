package org.example.SearchEngine.service.Intergace;

import org.example.SearchEngine.model.Document;

/**
 * Service interface for document management operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface DocumentService {
    /**
     * Add a document to a dataset.
     * @param datasetId The dataset ID
     * @param documentId The document ID
     * @param content The document content
     * @param userId ID of the user performing the operation
     * @return The created document
     */
    Document addDocument(String datasetId, String documentId, String content, String userId);

    /**
     * Delete a document from a dataset.
     * @param datasetId The dataset ID
     * @param documentId The document ID
     * @param userId ID of the user performing the operation
     */
    void deleteDocument(String datasetId, String documentId, String userId);

    /**
     * Get a document from a dataset.
     * @param datasetId The dataset ID
     * @param documentId The document ID
     * @param userId ID of the user performing the operation
     * @return The document
     */
    Document getDocument(String datasetId, String documentId, String userId);
}
