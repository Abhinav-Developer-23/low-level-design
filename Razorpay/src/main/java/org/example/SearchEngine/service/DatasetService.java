package org.example.SearchEngine.service;

import org.example.SearchEngine.model.Dataset;

/**
 * Service interface for dataset management operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface DatasetService {
    /**
     * Create a new dataset.
     * @param datasetId Unique identifier for the dataset
     * @param name Name of the dataset
     * @param ownerId ID of the user who owns the dataset
     * @return The created dataset
     */
    Dataset createDataset(String datasetId, String name, String ownerId);

    /**
     * Get a dataset by ID.
     * @param datasetId The dataset ID
     * @return The dataset
     */
    Dataset getDataset(String datasetId);

    /**
     * Delete a dataset.
     * @param datasetId The dataset ID
     * @param requesterId ID of the user requesting the deletion
     */
    void deleteDataset(String datasetId, String requesterId);

    /**
     * Grant read access to a user for a dataset.
     * @param datasetId The dataset ID
     * @param ownerId ID of the dataset owner (for authorization)
     * @param targetUserId ID of the user to grant access to
     */
    void grantReadAccess(String datasetId, String ownerId, String targetUserId);

    /**
     * Revoke read access from a user for a dataset.
     * @param datasetId The dataset ID
     * @param ownerId ID of the dataset owner (for authorization)
     * @param targetUserId ID of the user to revoke access from
     */
    void revokeReadAccess(String datasetId, String ownerId, String targetUserId);

    /**
     * Check if a user has read access to a dataset.
     * @param datasetId The dataset ID
     * @param userId The user ID
     * @return true if user has read access
     */
    boolean hasReadAccess(String datasetId, String userId);
}
