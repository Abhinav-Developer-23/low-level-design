package org.example.SearchEngine.service;

import org.example.SearchEngine.exception.DatasetNotFoundException;
import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.repository.DatasetRepository;

import java.util.List;

/**
 * Implementation of DatasetService
 */
public class DatasetServiceImpl implements DatasetService {
    private final DatasetRepository datasetRepository;

    public DatasetServiceImpl(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    @Override
    public Dataset createDataset(String datasetName) {
        return datasetRepository.createDataset(datasetName);
    }

    @Override
    public Dataset getDataset(String datasetId) {
        return datasetRepository.getDataset(datasetId)
                .orElseThrow(() -> new DatasetNotFoundException(datasetId));
    }

    @Override
    public List<Dataset> getAllDatasets() {
        return datasetRepository.getAllDatasets();
    }

    @Override
    public void deleteDataset(String datasetId) {
        if (!datasetRepository.deleteDataset(datasetId)) {
            throw new DatasetNotFoundException(datasetId);
        }
    }
}




