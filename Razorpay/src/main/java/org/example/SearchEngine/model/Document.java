package org.example.SearchEngine.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Represents a document in the search engine.
 * Each document has a unique ID, content, and timestamp.
 */
@Getter
@EqualsAndHashCode(of = "documentId")
public class Document {
    private final String documentId;
    private final String content;
    private final LocalDateTime timestamp;

    public Document(String documentId, String content) {
        this.documentId = documentId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
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
    public String toString() {
        return "Document{" +
                "documentId='" + documentId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
