package org.example.interfaces;

import java.util.List;
import java.util.Map;

/**
 * Strategy interface for search functionality
 * Part of Strategy Design Pattern
 * 
 * @param <T> The type of object to search for
 */
public interface SearchStrategy<T> {
    /**
     * Search for items matching the query and criteria
     * 
     * @param query The search query
     * @param criteria Additional search criteria (filters)
     * @param items The collection of items to search
     * @return List of matching items
     */
    List<T> search(String query, Map<String, Object> criteria, List<T> items);
}

