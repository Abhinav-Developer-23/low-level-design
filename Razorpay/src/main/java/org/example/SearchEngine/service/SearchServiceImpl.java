package org.example.SearchEngine.service;

import org.example.SearchEngine.enums.SortOrder;
import org.example.SearchEngine.model.Document;
import org.example.SearchEngine.model.SearchResult;
import org.example.SearchEngine.repository.DatasetRepository;
import org.example.SearchEngine.strategy.SortStrategy;
import org.example.SearchEngine.strategy.SortStrategyFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of SearchService
 * Uses Strategy Pattern for sorting
 * Simplified to use basic string matching without regex or relevance scoring
 */
public class SearchServiceImpl implements SearchService {
    private final DatasetRepository datasetRepository;

    public SearchServiceImpl(DatasetRepository datasetRepository) {
        this.datasetRepository = datasetRepository;
    }

    @Override
    public List<SearchResult> search(String datasetId, String searchPattern, SortOrder sortOrder) {
        List<Document> documents = datasetRepository.getDocuments(datasetId);
        List<SearchResult> searchResults = new ArrayList<>();

        // Simple case-insensitive string matching
        String lowerCasePattern = searchPattern.toLowerCase();

        for (Document document : documents) {
            String lowerCaseContent = document.getContent().toLowerCase();
            
            // Check if the document contains the search string
            if (lowerCaseContent.contains(lowerCasePattern)) {
                // Count occurrences of the search string
                int matchCount = countOccurrences(lowerCaseContent, lowerCasePattern);
                searchResults.add(new SearchResult(document, matchCount));
            }
        }

        // Apply sorting strategy
        SortStrategy sortStrategy = SortStrategyFactory.getSortStrategy(sortOrder);
        sortStrategy.sort(searchResults);

        return searchResults;
    }

    @Override
    public List<SearchResult> search(String datasetId, String searchPattern) {
        return search(datasetId, searchPattern, SortOrder.ALPHABETICAL_ASC);
    }

    /**
     * Count occurrences of a substring in a string
     */
    private int countOccurrences(String content, String searchString) {
        int count = 0;
        int index = 0;
        
        while ((index = content.indexOf(searchString, index)) != -1) {
            count++;
            index += searchString.length();
        }
        
        return count;
    }
}



