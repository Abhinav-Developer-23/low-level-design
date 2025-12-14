package org.example.SearchEngine.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a search result containing a document and match information.
 */
@Getter
@EqualsAndHashCode(of = {"document", "datasetId"})
public class SearchResult {
    private final Document document;
    private final String datasetId;
    private final int matchCount;

    public SearchResult(Document document, String datasetId, int matchCount) {
        this.document = document;
        this.datasetId = datasetId;
        this.matchCount = matchCount;
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
