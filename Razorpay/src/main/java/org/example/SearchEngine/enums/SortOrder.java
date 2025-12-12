package org.example.SearchEngine.enums;

/**
 * Enum representing different sorting orders for search results
 */
public enum SortOrder {
    RELEVANCE_DESC("Sort by relevance (highest first)"),
    RELEVANCE_ASC("Sort by relevance (lowest first)"),
    ALPHABETICAL_ASC("Sort alphabetically (A-Z)"),
    ALPHABETICAL_DESC("Sort alphabetically (Z-A)"),
    TIMESTAMP_DESC("Sort by timestamp (newest first)"),
    TIMESTAMP_ASC("Sort by timestamp (oldest first)");

    private final String description;

    SortOrder(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}



