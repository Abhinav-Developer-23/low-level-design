package org.example.SearchEngine.strategy;

import org.example.SearchEngine.model.SearchResult;

import java.util.Comparator;
import java.util.List;

/**
 * Sort strategy for timestamp descending order (newest first).
 */
public class TimestampDescSortStrategy implements SortStrategy {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort(Comparator.comparing((SearchResult result) -> 
            result.getDocument().getTimestamp()).reversed());
    }
}
