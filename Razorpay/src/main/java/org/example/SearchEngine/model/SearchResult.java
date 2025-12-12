package org.example.SearchEngine.model;

/**
 * Entity representing a search result with relevance score
 */
public class SearchResult {
    private final Document document;
    private final double relevanceScore;
    private final int matchCount;

    public SearchResult(Document document, double relevanceScore, int matchCount) {
        this.document = document;
        this.relevanceScore = relevanceScore;
        this.matchCount = matchCount;
    }

    public Document getDocument() {
        return document;
    }

    public double getRelevanceScore() {
        return relevanceScore;
    }

    public int getMatchCount() {
        return matchCount;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "documentId='" + document.getId() + '\'' +
                ", content='" + document.getContent() + '\'' +
                ", relevanceScore=" + String.format("%.2f", relevanceScore) +
                ", matchCount=" + matchCount +
                '}';
    }
}



