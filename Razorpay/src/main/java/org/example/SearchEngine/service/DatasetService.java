package org.example.SearchEngine.service;

import org.example.SearchEngine.model.Dataset;

import java.util.List;

/**
 * Service interface for dataset operations
 * Following Single Responsibility Principle (SOLID)
 */
public interface DatasetService {
    /**
     * Create a new dataset
     */
    Dataset createDataset(String datasetName);

    /**
     * Get a dataset by ID
     */
    Dataset getDataset(String datasetId);

    /**
     * Get all datasets
     */
    List<Dataset> getAllDatasets();

    /**
     * Delete a dataset
     */
    void deleteDataset(String datasetId);
}



