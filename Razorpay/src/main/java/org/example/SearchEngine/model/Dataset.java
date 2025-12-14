package org.example.SearchEngine.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

/**
 * Represents a dataset containing multiple documents.
 * Supports access control through owner and authorized readers.
 */
@Getter
@EqualsAndHashCode(of = "datasetId")
public class Dataset {
    private final String datasetId;
    private final String name;
    private final String ownerId;
    private final Map<String, Document> documents; // documentId -> Document
    private final Set<String> authorizedReaders; // userIds with read access

    public Dataset(String datasetId, String name, String ownerId) {
        this.datasetId = datasetId;
        this.name = name;
        this.ownerId = ownerId;
        this.documents = new HashMap<>();
        this.authorizedReaders = new HashSet<>();
        this.authorizedReaders.add(ownerId); // Owner has implicit read access
    }

    /**
     * Add a document to the dataset.
     * @param document The document to add
     */
    public void addDocument(Document document) {
        documents.put(document.getDocumentId(), document);
    }

    /**
     * Remove a document from the dataset.
     * @param documentId The ID of the document to remove
     * @return The removed document, or null if not found
     */
    public Document removeDocument(String documentId) {
        return documents.remove(documentId);
    }

    /**
     * Get a document by ID.
     * @param documentId The document ID
     * @return The document, or null if not found
     */
    public Document getDocument(String documentId) {
        return documents.get(documentId);
    }

    /**
     * Get all documents in the dataset.
     * @return Collection of all documents
     */
    public Collection<Document> getAllDocuments() {
        return new ArrayList<>(documents.values());
    }

    /**
     * Grant read access to a user.
     * @param userId The user ID
     */
    public void grantReadAccess(String userId) {
        authorizedReaders.add(userId);
    }

    /**
     * Revoke read access from a user.
     * @param userId The user ID
     */
    public void revokeReadAccess(String userId) {
        if (!userId.equals(ownerId)) { // Cannot revoke owner's access
            authorizedReaders.remove(userId);
        }
    }

    /**
     * Check if a user has read access to this dataset.
     * @param userId The user ID
     * @return true if the user has read access
     */
    public boolean hasReadAccess(String userId) {
        return authorizedReaders.contains(userId);
    }

    /**
     * Check if a user is the owner of this dataset.
     * @param userId The user ID
     * @return true if the user is the owner
     */
    public boolean isOwner(String userId) {
        return ownerId.equals(userId);
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "datasetId='" + datasetId + '\'' +
                ", name='" + name + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", documentsCount=" + documents.size() +
                ", authorizedReaders=" + authorizedReaders.size() +
                '}';
    }
}
