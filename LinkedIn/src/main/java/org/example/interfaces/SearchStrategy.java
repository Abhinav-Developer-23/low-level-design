package org.example.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Strategy interface for different search implementations.
 * Implements the Strategy design pattern for flexible search algorithms.
 */
public interface SearchStrategy<T> {
    /**
     * Performs a search based on the given query and criteria.
     *
     * @param query The search query string
     * @param criteria Additional search criteria (e.g., filters, location, etc.)
     * @param items The collection to search through
     * @return List of matching items, ranked by relevance
     */
    List<T> search(String query, Map<String, Object> criteria, List<T> items);

    /**
     * Returns the name of this search strategy.
     *
     * @return Strategy name
     */
    String getStrategyName();
}

