package org.example.SearchEngine.strategy;

import org.example.SearchEngine.enums.SortOrder;

/**
 * Factory for creating sort strategies
 * Following Factory Pattern
 */
public class SortStrategyFactory {
    
    public static SortStrategy getSortStrategy(SortOrder sortOrder) {
        switch (sortOrder) {
            case RELEVANCE_DESC:
                return new RelevanceDescSortStrategy();
            case RELEVANCE_ASC:
                return new RelevanceAscSortStrategy();
            case ALPHABETICAL_ASC:
                return new AlphabeticalAscSortStrategy();
            case ALPHABETICAL_DESC:
                return new AlphabeticalDescSortStrategy();
            case TIMESTAMP_DESC:
                return new TimestampDescSortStrategy();
            case TIMESTAMP_ASC:
                return new TimestampAscSortStrategy();
            default:
                return new RelevanceDescSortStrategy(); // Default strategy
        }
    }
}



