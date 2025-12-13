package org.example.SearchEngine.strategy;

import org.example.SearchEngine.model.SearchResult;

import java.util.Comparator;
import java.util.List;

/**
 * Sort strategy for sorting alphabetically in descending order
 */
public class AlphabeticalDescSortStrategy implements SortStrategy {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort(Comparator.comparing((SearchResult result) -> result.getDocument().getContent()).reversed());
    }
}




