package org.example.SearchEngine.strategy;

import org.example.SearchEngine.model.SearchResult;

import java.util.List;

/**
 * Strategy interface for sorting search results
 * Following Strategy Pattern and Open/Closed Principle (SOLID)
 */
public interface SortStrategy {
    /**
     * Sort the given list of search results
     * @param results List of search results to sort
     */
    void sort(List<SearchResult> results);
}

