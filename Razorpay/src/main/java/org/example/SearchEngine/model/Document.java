package org.example.SearchEngine.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a document in the search engine.
 * Each document has a unique ID, content, and timestamp.
 */
public class Document {
    private final String documentId;
    private final String content;
    private final LocalDateTime timestamp;

    public Document(String documentId, String content) {
        this.documentId = documentId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Check if the document content contains the search pattern (case-insensitive).
     * @param pattern The search pattern
     * @return true if the pattern is found
     */
    public boolean matches(String pattern) {
        return content.toLowerCase().contains(pattern.toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(documentId, document.documentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentId);
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentId='" + documentId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
