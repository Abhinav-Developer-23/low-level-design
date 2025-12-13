package org.example.SearchEngine.model;

/**
 * Entity representing a search result
 * Simplified to only track match count without relevance scoring
 */
public class SearchResult {
    private final Document document;
    private final int matchCount;

    public SearchResult(Document document, int matchCount) {
        this.document = document;
        this.matchCount = matchCount;
    }

    public Document getDocument() {
        return document;
    }

    public int getMatchCount() {
        return matchCount;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "documentId='" + document.getId() + '\'' +
                ", content='" + document.getContent() + '\'' +
                ", matchCount=" + matchCount +
                '}';
    }
}



