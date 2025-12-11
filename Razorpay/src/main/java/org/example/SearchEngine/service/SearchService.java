package org.example.SearchEngine.service;

import org.example.SearchEngine.enums.SortOrder;
import org.example.SearchEngine.model.SearchResult;

import java.util.List;

/**
 * Service interface for search operations
 * Following Single Responsibility Principle (SOLID)
 */
public interface SearchService {
    /**
     * Search for documents containing the search pattern
     * @param datasetId The dataset to search in
     * @param searchPattern The pattern to search for
     * @param sortOrder The order to sort results
     * @return List of search results sorted by the specified order
     */
    List<SearchResult> search(String datasetId, String searchPattern, SortOrder sortOrder);

    /**
     * Search for documents containing the search pattern with default sorting (relevance descending)
     * @param datasetId The dataset to search in
     * @param searchPattern The pattern to search for
     * @return List of search results sorted by relevance
     */
    List<SearchResult> search(String datasetId, String searchPattern);
}

