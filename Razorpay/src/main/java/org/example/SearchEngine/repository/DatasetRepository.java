package org.example.SearchEngine.repository;

import org.example.SearchEngine.exception.DatasetAlreadyExistsException;
import org.example.SearchEngine.exception.DatasetNotFoundException;
import org.example.SearchEngine.model.Dataset;

import java.util.*;

/**
 * Singleton repository for managing datasets in memory.
 * Thread-safe implementation using synchronized methods.
 */
public class DatasetRepository {
    private static DatasetRepository instance;
    private final Map<String, Dataset> datasets; // datasetId -> Dataset

    /**
     * Private constructor to prevent direct instantiation.
     */
    private DatasetRepository() {
        this.datasets = new HashMap<>();
    }

    /**
     * Get the singleton instance of DatasetRepository.
     * @return The singleton instance
     */
    public static synchronized DatasetRepository getInstance() {
        if (instance == null) {
            instance = new DatasetRepository();
        }
        return instance;
    }

    /**
     * Create a new dataset.
     * @param dataset The dataset to create
     * @throws DatasetAlreadyExistsException if dataset already exists
     */
    public synchronized void createDataset(Dataset dataset) {
        if (datasets.containsKey(dataset.getDatasetId())) {
            throw new DatasetAlreadyExistsException("Dataset with ID " + dataset.getDatasetId() + " already exists");
        }
        datasets.put(dataset.getDatasetId(), dataset);
    }

    /**
     * Find a dataset by ID.
     * @param datasetId The dataset ID
     * @return Optional containing the dataset if found
     */
    public synchronized Optional<Dataset> findById(String datasetId) {
        return Optional.ofNullable(datasets.get(datasetId));
    }

    /**
     * Get a dataset by ID or throw exception if not found.
     * @param datasetId The dataset ID
     * @return The dataset
     * @throws DatasetNotFoundException if dataset not found
     */
    public synchronized Dataset getById(String datasetId) {
        return findById(datasetId)
                .orElseThrow(() -> new DatasetNotFoundException("Dataset with ID " + datasetId + " not found"));
    }

    /**
     * Check if a dataset exists.
     * @param datasetId The dataset ID
     * @return true if dataset exists
     */
    public synchronized boolean exists(String datasetId) {
        return datasets.containsKey(datasetId);
    }

    /**
     * Delete a dataset.
     * @param datasetId The dataset ID
     * @return The deleted dataset
     * @throws DatasetNotFoundException if dataset not found
     */
    public synchronized Dataset deleteDataset(String datasetId) {
        Dataset dataset = datasets.remove(datasetId);
        if (dataset == null) {
            throw new DatasetNotFoundException("Dataset with ID " + datasetId + " not found");
        }
        return dataset;
    }

    /**
     * Get all datasets.
     * @return Collection of all datasets
     */
    public synchronized Collection<Dataset> getAllDatasets() {
        return new ArrayList<>(datasets.values());
    }

    /**
     * Get datasets owned by a specific user.
     * @param ownerId The owner user ID
     * @return List of datasets owned by the user
     */
    public synchronized List<Dataset> getDatasetsByOwner(String ownerId) {
        List<Dataset> result = new ArrayList<>();
        for (Dataset dataset : datasets.values()) {
            if (dataset.getOwnerId().equals(ownerId)) {
                result.add(dataset);
            }
        }
        return result;
    }

    /**
     * Get datasets that a user has read access to.
     * @param userId The user ID
     * @return List of datasets the user can read
     */
    public synchronized List<Dataset> getDatasetsWithReadAccess(String userId) {
        List<Dataset> result = new ArrayList<>();
        for (Dataset dataset : datasets.values()) {
            if (dataset.hasReadAccess(userId)) {
                result.add(dataset);
            }
        }
        return result;
    }

    /**
     * Clear all datasets (useful for testing).
     */
    public synchronized void clear() {
        datasets.clear();
    }
}
