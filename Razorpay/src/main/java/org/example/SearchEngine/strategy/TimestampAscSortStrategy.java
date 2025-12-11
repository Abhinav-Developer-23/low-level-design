package org.example.SearchEngine.strategy;

import org.example.SearchEngine.model.SearchResult;

import java.util.Comparator;
import java.util.List;

/**
 * Sort strategy for sorting by timestamp in ascending order (oldest first)
 */
public class TimestampAscSortStrategy implements SortStrategy {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort(Comparator.comparingLong(result -> result.getDocument().getTimestamp()));
    }
}

