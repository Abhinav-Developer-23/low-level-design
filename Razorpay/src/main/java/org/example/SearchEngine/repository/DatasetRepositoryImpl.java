package org.example.SearchEngine.repository;

import org.example.SearchEngine.exception.DatasetAlreadyExistsException;
import org.example.SearchEngine.exception.DatasetNotFoundException;
import org.example.SearchEngine.exception.DocumentNotFoundException;
import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.model.Document;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of DatasetRepository
 * Thread-safe using ConcurrentHashMap
 */
public class DatasetRepositoryImpl implements DatasetRepository {
    // Using ConcurrentHashMap for thread-safety
    private final Map<String, Dataset> datasets;

    public DatasetRepositoryImpl() {
        this.datasets = new ConcurrentHashMap<>();
    }

    @Override
    public Dataset createDataset(String datasetName) {
        if (datasets.containsKey(datasetName)) {
            throw new DatasetAlreadyExistsException(datasetName);
        }
        Dataset dataset = new Dataset(datasetName);
        datasets.put(datasetName, dataset);
        return dataset;
    }

    @Override
    public Optional<Dataset> getDataset(String datasetId) {
        return Optional.ofNullable(datasets.get(datasetId));
    }

    @Override
    public List<Dataset> getAllDatasets() {
        return new ArrayList<>(datasets.values());
    }

    @Override
    public boolean deleteDataset(String datasetId) {
        return datasets.remove(datasetId) != null;
    }

    @Override
    public boolean datasetExists(String datasetId) {
        return datasets.containsKey(datasetId);
    }

    @Override
    public void addDocument(String datasetId, Document document) {
        Dataset dataset = datasets.get(datasetId);
        if (dataset == null) {
            throw new DatasetNotFoundException(datasetId);
        }
        dataset.addDocument(document);
    }

    @Override
    public void removeDocument(String datasetId, String documentId) {
        Dataset dataset = datasets.get(datasetId);
        if (dataset == null) {
            throw new DatasetNotFoundException(datasetId);
        }
        if (!dataset.containsDocument(documentId)) {
            throw new DocumentNotFoundException(documentId);
        }
        dataset.removeDocument(documentId);
    }

    @Override
    public List<Document> getDocuments(String datasetId) {
        Dataset dataset = datasets.get(datasetId);
        if (dataset == null) {
            throw new DatasetNotFoundException(datasetId);
        }
        return new ArrayList<>(dataset.getDocuments().values());
    }
}

