package org.example.SearchEngine.service;

import org.example.SearchEngine.enums.SortOrder;
import org.example.SearchEngine.exception.UnauthorizedAccessException;
import org.example.SearchEngine.model.Dataset;
import org.example.SearchEngine.model.Document;
import org.example.SearchEngine.model.SearchResult;
import org.example.SearchEngine.repository.DatasetRepository;
import org.example.SearchEngine.service.Intergace.SearchService;
import org.example.SearchEngine.strategy.SortStrategy;
import org.example.SearchEngine.strategy.SortStrategyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of SearchService.
 * Handles search operations with access control and sorting.
 */
public class SearchServiceImpl implements SearchService {
    private final DatasetRepository datasetRepository;

    public SearchServiceImpl() {
        this.datasetRepository = DatasetRepository.getInstance();
    }

    @Override
    public List<SearchResult> search(String datasetId, String pattern, String userId, SortOrder sortOrder) {
        Dataset dataset = datasetRepository.getById(datasetId);
        
        // Check if user has read access
        if (!dataset.hasReadAccess(userId)) {
            throw new UnauthorizedAccessException(
                "User " + userId + " does not have read access to dataset " + datasetId);
        }
        
        List<SearchResult> results = searchInDataset(dataset, pattern);
        
        // Apply sorting
        SortStrategy sortStrategy = SortStrategyFactory.getStrategy(sortOrder);
        sortStrategy.sort(results);
        
        return results;
    }

    @Override
    public List<SearchResult> searchAll(String pattern, String userId, SortOrder sortOrder) {
        List<SearchResult> allResults = new ArrayList<>();
        
        // Get all datasets user has access to
        List<Dataset> accessibleDatasets = datasetRepository.getDatasetsWithReadAccess(userId);
        
        // Search in each accessible dataset
        for (Dataset dataset : accessibleDatasets) {
            List<SearchResult> datasetResults = searchInDataset(dataset, pattern);
            allResults.addAll(datasetResults);
        }
        
        // Apply sorting
        SortStrategy sortStrategy = SortStrategyFactory.getStrategy(sortOrder);
        sortStrategy.sort(allResults);
        
        return allResults;
    }

    /**
     * Helper method to search for pattern in a dataset.
     * @param dataset The dataset to search in
     * @param searchPattern The search pattern
     * @return List of search results
     */
    private List<SearchResult> searchInDataset(Dataset dataset, String searchPattern) {
        List<SearchResult> results = new ArrayList<>();
        
        for (Document document : dataset.getAllDocuments()) {
            if (document.matches(searchPattern)) {
                int matchCount = countMatches(document.getContent(), searchPattern);
                SearchResult result = new SearchResult(document, dataset.getDatasetId(), matchCount);
                results.add(result);
            }
        }
        
        return results;
    }

    /**
     * Count the number of times a pattern appears in the content (case-insensitive).
     * @param content The content to search in
     * @param searchPattern The pattern to search for
     * @return The number of matches
     */
    private int countMatches(String content, String searchPattern) {
        Pattern pattern = Pattern.compile(Pattern.quote(searchPattern), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        
        return count;
    }
}
