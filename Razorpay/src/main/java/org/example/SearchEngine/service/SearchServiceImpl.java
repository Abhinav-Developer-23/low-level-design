package org.example.SearchEngine.service;

import org.example.SearchEngine.enums.SortOrder;
import org.example.SearchEngine.model.Document;
import org.example.SearchEngine.model.SearchResult;
import org.example.SearchEngine.repository.DatasetRepository;
import org.example.SearchEngine.strategy.SortStrategy;
import org.example.SearchEngine.strategy.SortStrategyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of SearchService
 * Uses Strategy Pattern for sorting
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

        // Compile pattern for efficient matching
        Pattern pattern = Pattern.compile(searchPattern, Pattern.CASE_INSENSITIVE);

        for (Document document : documents) {
            Matcher matcher = pattern.matcher(document.getContent());
            int matchCount = 0;

            // Count occurrences of the pattern
            while (matcher.find()) {
                matchCount++;
            }

            // Only include documents that have matches
            if (matchCount > 0) {
                double relevanceScore = calculateRelevanceScore(document, searchPattern, matchCount);
                searchResults.add(new SearchResult(document, relevanceScore, matchCount));
            }
        }

        // Apply sorting strategy
        SortStrategy sortStrategy = SortStrategyFactory.getSortStrategy(sortOrder);
        sortStrategy.sort(searchResults);

        return searchResults;
    }

    @Override
    public List<SearchResult> search(String datasetId, String searchPattern) {
        return search(datasetId, searchPattern, SortOrder.RELEVANCE_DESC);
    }

    /**
     * Calculate relevance score based on match count and document length
     * Higher score means more relevant
     */
    private double calculateRelevanceScore(Document document, String searchPattern, int matchCount) {
        String content = document.getContent();
        int contentLength = content.length();
        
        // Base score from match count
        double baseScore = matchCount * 100.0;
        
        // Adjust for document length (favor shorter documents with same match count)
        double lengthPenalty = Math.log(contentLength + 1);
        
        // Adjust for pattern length (longer patterns are more specific)
        double patternBonus = searchPattern.length() * 0.5;
        
        return baseScore + patternBonus - lengthPenalty;
    }
}

