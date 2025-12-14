package org.example.SearchEngine.service.Intergace;

import org.example.SearchEngine.enums.SortOrder;
import org.example.SearchEngine.model.SearchResult;

import java.util.List;

/**
 * Service interface for search operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface SearchService {
    /**
     * Search for documents matching a pattern in a specific dataset.
     * @param datasetId The dataset ID to search in
     * @param pattern The search pattern
     * @param userId ID of the user performing the search
     * @param sortOrder The order to sort results
     * @return List of search results
     */
    List<SearchResult> search(String datasetId, String pattern, String userId, SortOrder sortOrder);

    /**
     * Search for documents matching a pattern across all datasets the user has access to.
     * @param pattern The search pattern
     * @param userId ID of the user performing the search
     * @param sortOrder The order to sort results
     * @return List of search results
     */
    List<SearchResult> searchAll(String pattern, String userId, SortOrder sortOrder);
}
