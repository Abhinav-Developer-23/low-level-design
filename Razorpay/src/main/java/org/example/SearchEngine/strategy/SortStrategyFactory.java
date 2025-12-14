package org.example.SearchEngine.strategy;

import org.example.SearchEngine.enums.SortOrder;

/**
 * Factory class for creating sort strategies.
 * Implements Factory design pattern for creating strategy instances.
 */
public class SortStrategyFactory {
    
    /**
     * Get the appropriate sort strategy based on the sort order.
     * @param sortOrder The desired sort order
     * @return The corresponding sort strategy
     */
    public static SortStrategy getStrategy(SortOrder sortOrder) {
        switch (sortOrder) {
            case ALPHABETICAL_ASC:
                return new AlphabeticalAscSortStrategy();
            case ALPHABETICAL_DESC:
                return new AlphabeticalDescSortStrategy();
            case TIMESTAMP_ASC:
                return new TimestampAscSortStrategy();
            case TIMESTAMP_DESC:
                return new TimestampDescSortStrategy();
            case RELEVANCE:
                return new RelevanceSortStrategy();
            default:
                throw new IllegalArgumentException("Unknown sort order: " + sortOrder);
        }
    }
}
