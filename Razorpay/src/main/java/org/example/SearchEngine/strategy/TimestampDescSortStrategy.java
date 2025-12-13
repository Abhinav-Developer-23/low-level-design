package org.example.SearchEngine.strategy;

import org.example.SearchEngine.model.SearchResult;

import java.util.Comparator;
import java.util.List;

/**
 * Sort strategy for sorting by timestamp in descending order (newest first)
 */
public class TimestampDescSortStrategy implements SortStrategy {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort(Comparator.comparingLong((SearchResult result) -> result.getDocument().getTimestamp()).reversed());
    }
}




