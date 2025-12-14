package org.example.SearchEngine.service;

import org.example.SearchEngine.exception.DocumentNotFoundException;
import org.example.SearchEngine.exception.UnauthorizedAccessException;
import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.model.Document;
import org.example.SearchEngine.repository.DatasetRepository;

/**
 * Implementation of DocumentService.
 * Handles business logic for document management with access control.
 */
public class DocumentServiceImpl implements DocumentService {
    private final DatasetRepository datasetRepository;

    public DocumentServiceImpl() {
        this.datasetRepository = DatasetRepository.getInstance();
    }

    @Override
    public Document addDocument(String datasetId, String documentId, String content, String userId) {
        Dataset dataset = datasetRepository.getById(datasetId);
        
        // Only owner can add documents
        if (!dataset.isOwner(userId)) {
            throw new UnauthorizedAccessException(
                "User " + userId + " is not authorized to add documents to dataset " + datasetId);
        }
        
        Document document = new Document(documentId, content);
        dataset.addDocument(document);
        return document;
    }

    @Override
    public void deleteDocument(String datasetId, String documentId, String userId) {
        Dataset dataset = datasetRepository.getById(datasetId);
        
        // Only owner can delete documents
        if (!dataset.isOwner(userId)) {
            throw new UnauthorizedAccessException(
                "User " + userId + " is not authorized to delete documents from dataset " + datasetId);
        }
        
        Document removed = dataset.removeDocument(documentId);
        if (removed == null) {
            throw new DocumentNotFoundException(
                "Document " + documentId + " not found in dataset " + datasetId);
        }
    }

    @Override
    public Document getDocument(String datasetId, String documentId, String userId) {
        Dataset dataset = datasetRepository.getById(datasetId);
        
        // User must have read access
        if (!dataset.hasReadAccess(userId)) {
            throw new UnauthorizedAccessException(
                "User " + userId + " does not have read access to dataset " + datasetId);
        }
        
        Document document = dataset.getDocument(documentId);
        if (document == null) {
            throw new DocumentNotFoundException(
                "Document " + documentId + " not found in dataset " + datasetId);
        }
        
        return document;
    }
}
