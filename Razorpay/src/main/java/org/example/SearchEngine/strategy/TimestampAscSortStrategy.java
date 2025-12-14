package org.example.SearchEngine.strategy;

import org.example.SearchEngine.model.SearchResult;

import java.util.Comparator;
import java.util.List;

/**
 * Sort strategy for timestamp ascending order (oldest first).
 */
public class TimestampAscSortStrategy implements SortStrategy {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort(Comparator.comparing(result -> 
            result.getDocument().getTimestamp()));
    }
}
