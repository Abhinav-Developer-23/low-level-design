package org.example.SearchEngine.service;

import org.example.SearchEngine.exception.UnauthorizedAccessException;
import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.repository.DatasetRepository;
import org.example.SearchEngine.repository.UserRepository;
import org.example.SearchEngine.service.Intergace.DatasetService;

/**
 * Implementation of DatasetService.
 * Handles business logic for dataset management with access control.
 */
public class DatasetServiceImpl implements DatasetService {
    private final DatasetRepository datasetRepository;
    private final UserRepository userRepository;

    public DatasetServiceImpl() {
        this.datasetRepository = DatasetRepository.getInstance();
        this.userRepository = UserRepository.getInstance();
    }

    @Override
    public Dataset createDataset(String datasetId, String name, String ownerId) {
        // Verify owner exists
        userRepository.getById(ownerId);
        
        Dataset dataset = new Dataset(datasetId, name, ownerId);
        datasetRepository.createDataset(dataset);
        return dataset;
    }

    @Override
    public Dataset getDataset(String datasetId) {
        return datasetRepository.getById(datasetId);
    }

    @Override
    public void deleteDataset(String datasetId, String requesterId) {
        Dataset dataset = datasetRepository.getById(datasetId);
        
        // Only owner can delete
        if (!dataset.isOwner(requesterId)) {
            throw new UnauthorizedAccessException(
                "User " + requesterId + " is not authorized to delete dataset " + datasetId);
        }
        
        datasetRepository.deleteDataset(datasetId);
    }

    @Override
    public void grantReadAccess(String datasetId, String ownerId, String targetUserId) {
        Dataset dataset = datasetRepository.getById(datasetId);
        
        // Verify owner
        if (!dataset.isOwner(ownerId)) {
            throw new UnauthorizedAccessException(
                "User " + ownerId + " is not the owner of dataset " + datasetId);
        }
        
        // Verify target user exists
        userRepository.getById(targetUserId);
        
        dataset.grantReadAccess(targetUserId);
    }

    @Override
    public void revokeReadAccess(String datasetId, String ownerId, String targetUserId) {
        Dataset dataset = datasetRepository.getById(datasetId);
        
        // Verify owner
        if (!dataset.isOwner(ownerId)) {
            throw new UnauthorizedAccessException(
                "User " + ownerId + " is not the owner of dataset " + datasetId);
        }
        
        dataset.revokeReadAccess(targetUserId);
    }

    @Override
    public boolean hasReadAccess(String datasetId, String userId) {
        Dataset dataset = datasetRepository.getById(datasetId);
        return dataset.hasReadAccess(userId);
    }
}
