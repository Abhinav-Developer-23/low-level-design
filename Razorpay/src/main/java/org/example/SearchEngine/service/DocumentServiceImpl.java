package org.example.SearchEngine.service;

import org.example.SearchEngine.exception.DocumentNotFoundException;
import org.example.SearchEngine.model.Document;
import org.example.SearchEngine.repository.DatasetRepository;

import java.util.List;

/**
 * Implementation of DocumentService
 */
public class DocumentServiceImpl implements DocumentService {
    private final DatasetRepository datasetRepository;

    public DocumentServiceImpl(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    @Override
    public Document addDocument(String datasetId, String content) {
        Document document = new Document(content);
        datasetRepository.addDocument(datasetId, document);
        return document;
    }

    @Override
    public void deleteDocument(String datasetId, String documentId) {
        datasetRepository.removeDocument(datasetId, documentId);
    }

    @Override
    public Document getDocument(String datasetId, String documentId) {
        List<Document> documents = datasetRepository.getDocuments(datasetId);
        return documents.stream()
                .filter(doc -> doc.getId().equals(documentId))
                .findFirst()
                .orElseThrow(() -> new DocumentNotFoundException(documentId));
    }
}



