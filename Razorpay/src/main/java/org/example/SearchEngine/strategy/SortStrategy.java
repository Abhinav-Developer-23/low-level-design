package org.example.SearchEngine.strategy;

import org.example.SearchEngine.model.SearchResult;

import java.util.List;

/**
 * Strategy interface for sorting search results.
 * Implements Strategy design pattern for flexible sorting algorithms.
 */
public interface SortStrategy {
    /**
     * Sort the given list of search results.
     * @param results The list of search results to sort
     */
    void sort(List<SearchResult> results);
}
