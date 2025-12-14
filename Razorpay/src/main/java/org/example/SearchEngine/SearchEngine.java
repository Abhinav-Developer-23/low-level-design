package org.example.SearchEngine;

import org.example.SearchEngine.enums.SortOrder;
import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.model.Document;
import org.example.SearchEngine.model.SearchResult;
import org.example.SearchEngine.model.User;
import org.example.SearchEngine.service.*;
import org.example.SearchEngine.service.Intergace.DatasetService;
import org.example.SearchEngine.service.Intergace.DocumentService;
import org.example.SearchEngine.service.Intergace.SearchService;
import org.example.SearchEngine.service.Intergace.UserService;

import java.util.List;

/**
 * Facade class providing a simplified interface to the Search Engine.
 * Implements Facade design pattern to hide the complexity of multiple services.
 * This provides a single entry point for all search engine operations.
 */
public class SearchEngine {
    private final UserService userService;
    private final DatasetService datasetService;
    private final DocumentService documentService;
    private final SearchService searchService;

    /**
     * Constructor initializing all required services.
     */
    public SearchEngine() {
        this.userService = new UserServiceImpl();
        this.datasetService = new DatasetServiceImpl();
        this.documentService = new DocumentServiceImpl();
        this.searchService = new SearchServiceImpl();
    }

    // ===== User Management =====

    /**
     * Register a new user in the system.
     * @param userId Unique user identifier
     * @param name User's name
     * @param email User's email
     * @return The created user
     */
    public User registerUser(String userId, String name, String email) {
        return userService.registerUser(userId, name, email);
    }

    /**
     * Get user by ID.
     * @param userId The user ID
     * @return The user
     */
    public User getUser(String userId) {
        return userService.getUser(userId);
    }

    // ===== Dataset Management =====

    /**
     * Create a new dataset.
     * @param datasetId Unique dataset identifier
     * @param name Dataset name
     * @param ownerId Owner's user ID
     * @return The created dataset
     */
    public Dataset createDataset(String datasetId, String name, String ownerId) {
        return datasetService.createDataset(datasetId, name, ownerId);
    }

    /**
     * Get dataset by ID.
     * @param datasetId The dataset ID
     * @return The dataset
     */
    public Dataset getDataset(String datasetId) {
        return datasetService.getDataset(datasetId);
    }

    /**
     * Delete a dataset.
     * @param datasetId The dataset ID
     * @param ownerId Owner's user ID
     */
    public void deleteDataset(String datasetId, String ownerId) {
        datasetService.deleteDataset(datasetId, ownerId);
    }

    // ===== Access Control =====

    /**
     * Grant read access to a user for a dataset.
     * @param datasetId The dataset ID
     * @param ownerId Owner's user ID
     * @param targetUserId User to grant access to
     */
    public void grantReadAccess(String datasetId, String ownerId, String targetUserId) {
        datasetService.grantReadAccess(datasetId, ownerId, targetUserId);
    }

    /**
     * Revoke read access from a user for a dataset.
     * @param datasetId The dataset ID
     * @param ownerId Owner's user ID
     * @param targetUserId User to revoke access from
     */
    public void revokeReadAccess(String datasetId, String ownerId, String targetUserId) {
        datasetService.revokeReadAccess(datasetId, ownerId, targetUserId);
    }

    /**
     * Check if user has read access to a dataset.
     * @param datasetId The dataset ID
     * @param userId The user ID
     * @return true if user has access
     */
    public boolean hasReadAccess(String datasetId, String userId) {
        return datasetService.hasReadAccess(datasetId, userId);
    }

    // ===== Document Management =====

    /**
     * Add a document to a dataset.
     * @param datasetId The dataset ID
     * @param documentId Unique document identifier
     * @param content Document content
     * @param userId User performing the operation
     * @return The created document
     */
    public Document addDocument(String datasetId, String documentId, String content, String userId) {
        return documentService.addDocument(datasetId, documentId, content, userId);
    }

    /**
     * Delete a document from a dataset.
     * @param datasetId The dataset ID
     * @param documentId The document ID
     * @param userId User performing the operation
     */
    public void deleteDocument(String datasetId, String documentId, String userId) {
        documentService.deleteDocument(datasetId, documentId, userId);
    }

    /**
     * Get a document from a dataset.
     * @param datasetId The dataset ID
     * @param documentId The document ID
     * @param userId User performing the operation
     * @return The document
     */
    public Document getDocument(String datasetId, String documentId, String userId) {
        return documentService.getDocument(datasetId, documentId, userId);
    }

    // ===== Search Operations =====

    /**
     * Search for documents in a specific dataset.
     * @param datasetId The dataset ID
     * @param pattern Search pattern
     * @param userId User performing the search
     * @param sortOrder Order to sort results
     * @return List of search results
     */
    public List<SearchResult> search(String datasetId, String pattern, String userId, SortOrder sortOrder) {
        return searchService.search(datasetId, pattern, userId, sortOrder);
    }

    /**
     * Search across all datasets the user has access to.
     * @param pattern Search pattern
     * @param userId User performing the search
     * @param sortOrder Order to sort results
     * @return List of search results
     */
    public List<SearchResult> searchAll(String pattern, String userId, SortOrder sortOrder) {
        return searchService.searchAll(pattern, userId, sortOrder);
    }
}
