package org.example.SearchEngine;

import org.example.SearchEngine.enums.SortOrder;
import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.model.Document;
import org.example.SearchEngine.model.SearchResult;
import org.example.SearchEngine.repository.DatasetRepository;
import org.example.SearchEngine.repository.DatasetRepositoryImpl;
import org.example.SearchEngine.service.*;

import java.util.List;

/**
 * Facade for the Search Engine system
 * Following Facade Pattern to provide a simplified interface to the complex subsystem
 * This is the main entry point for clients
 */
public class SearchEngine {
    private final DatasetService datasetService;
    private final DocumentService documentService;
    private final SearchService searchService;

    /**
     * Constructor initializes all services with dependency injection
     * Following Dependency Inversion Principle
     */
    public SearchEngine() {
        DatasetRepository datasetRepository = new DatasetRepositoryImpl();
        this.datasetService = new DatasetServiceImpl(datasetRepository);
        this.documentService = new DocumentServiceImpl(datasetRepository);
        this.searchService = new SearchServiceImpl(datasetRepository);
    }

    /**
     * Constructor for custom repository (useful for testing)
     */
    public SearchEngine(DatasetRepository datasetRepository) {
        this.datasetService = new DatasetServiceImpl(datasetRepository);
        this.documentService = new DocumentServiceImpl(datasetRepository);
        this.searchService = new SearchServiceImpl(datasetRepository);
    }

    // Dataset operations
    
    /**
     * Create a new dataset
     * @param datasetName Name of the dataset
     * @return Created dataset
     */
    public Dataset createDataset(String datasetName) {
        return datasetService.createDataset(datasetName);
    }

    /**
     * Get a dataset by ID
     * @param datasetId ID of the dataset
     * @return Dataset
     */
    public Dataset getDataset(String datasetId) {
        return datasetService.getDataset(datasetId);
    }

    /**
     * Get all datasets
     * @return List of all datasets
     */
    public List<Dataset> getAllDatasets() {
        return datasetService.getAllDatasets();
    }

    /**
     * Delete a dataset
     * @param datasetId ID of the dataset to delete
     */
    public void deleteDataset(String datasetId) {
        datasetService.deleteDataset(datasetId);
    }

    // Document operations
    
    /**
     * Add a document to a dataset
     * @param datasetId ID of the dataset
     * @param content Content of the document
     * @return Created document
     */
    public Document addDocument(String datasetId, String content) {
        return documentService.addDocument(datasetId, content);
    }

    /**
     * Delete a document from a dataset
     * @param datasetId ID of the dataset
     * @param documentId ID of the document to delete
     */
    public void deleteDocument(String datasetId, String documentId) {
        documentService.deleteDocument(datasetId, documentId);
    }

    /**
     * Get a document by ID
     * @param datasetId ID of the dataset
     * @param documentId ID of the document
     * @return Document
     */
    public Document getDocument(String datasetId, String documentId) {
        return documentService.getDocument(datasetId, documentId);
    }

    // Search operations
    
    /**
     * Search for documents with default sorting (relevance descending)
     * @param datasetId ID of the dataset to search in
     * @param searchPattern Pattern to search for
     * @return List of search results
     */
    public List<SearchResult> search(String datasetId, String searchPattern) {
        return searchService.search(datasetId, searchPattern);
    }

    /**
     * Search for documents with custom sorting
     * @param datasetId ID of the dataset to search in
     * @param searchPattern Pattern to search for
     * @param sortOrder Order to sort results
     * @return List of search results
     */
    public List<SearchResult> search(String datasetId, String searchPattern, SortOrder sortOrder) {
        return searchService.search(datasetId, searchPattern, sortOrder);
    }
}

