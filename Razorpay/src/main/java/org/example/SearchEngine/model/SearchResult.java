package org.example.SearchEngine.model;

import java.util.Objects;

/**
 * Represents a search result containing a document and match information.
 */
public class SearchResult {
    private final Document document;
    private final String datasetId;
    private final int matchCount;

    public SearchResult(Document document, String datasetId, int matchCount) {
        this.document = document;
        this.datasetId = datasetId;
        this.matchCount = matchCount;
    }

    public Document getDocument() {
        return document;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public int getMatchCount() {
        return matchCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResult that = (SearchResult) o;
        return Objects.equals(document, that.document) &&
                Objects.equals(datasetId, that.datasetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document, datasetId);
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "documentId='" + document.getDocumentId() + '\'' +
                ", datasetId='" + datasetId + '\'' +
                ", matchCount=" + matchCount +
                ", timestamp=" + document.getTimestamp() +
                '}';
    }
}
