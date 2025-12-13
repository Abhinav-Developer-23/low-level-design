package org.example.SearchEngine.repository;

import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.model.Document;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for dataset operations
 * Following Repository Pattern and Dependency Inversion Principle (SOLID)
 */
public interface DatasetRepository {
    /**
     * Create a new dataset
     */
    Dataset createDataset(String datasetName);

    /**
     * Get a dataset by ID
     */
    Optional<Dataset> getDataset(String datasetId);

    /**
     * Get all datasets
     */
    List<Dataset> getAllDatasets();

    /**
     * Delete a dataset
     */
    boolean deleteDataset(String datasetId);

    /**
     * Check if a dataset exists
     */
    boolean datasetExists(String datasetId);

    /**
     * Add a document to a dataset
     */
    void addDocument(String datasetId, Document document);

    /**
     * Remove a document from a dataset
     */
    void removeDocument(String datasetId, String documentId);

    /**
     * Get all documents from a dataset
     */
    List<Document> getDocuments(String datasetId);
}




