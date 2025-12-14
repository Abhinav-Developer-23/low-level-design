package org.example.SearchEngine.enums;

/**
 * Enum representing different sort orders for search results.
 */
public enum SortOrder {
    /**
     * Sort alphabetically by document content in ascending order (A-Z)
     */
    ALPHABETICAL_ASC,
    
    /**
     * Sort alphabetically by document content in descending order (Z-A)
     */
    ALPHABETICAL_DESC,
    
    /**
     * Sort by timestamp in ascending order (oldest first)
     */
    TIMESTAMP_ASC,
    
    /**
     * Sort by timestamp in descending order (newest first)
     */
    TIMESTAMP_DESC,
    
    /**
     * Sort by relevance (match count) in descending order (most relevant first)
     */
    RELEVANCE
}
