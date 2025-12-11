package org.example.SearchEngine.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Entity representing a dataset that contains documents
 */
public class Dataset {
    private final String id;
    private final String name;
    private final Map<String, Document> documents;
    private final long createdAt;

    public Dataset(String name) {
        this.id = name; // Using name as ID for easy identification
        this.name = name;
        this.documents = new HashMap<>();
        this.createdAt = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, Document> getDocuments() {
        return new HashMap<>(documents); // Return a copy to maintain immutability
    }

    public void addDocument(Document document) {
        documents.put(document.getId(), document);
    }

    public void removeDocument(String documentId) {
        documents.remove(documentId);
    }

    public boolean containsDocument(String documentId) {
        return documents.containsKey(documentId);
    }

    public int getDocumentCount() {
        return documents.size();
    }

    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dataset dataset = (Dataset) o;
        return Objects.equals(id, dataset.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Dataset{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", documentCount=" + documents.size() +
                ", createdAt=" + createdAt +
                '}';
    }
}

