package org.example.SearchEngine.strategy;

import org.example.SearchEngine.model.SearchResult;

import java.util.Comparator;
import java.util.List;

/**
 * Sort strategy for alphabetical ascending order (A-Z) based on document content.
 */
public class AlphabeticalAscSortStrategy implements SortStrategy {
    @Override
    public void sort(List<SearchResult> results) {
        results.sort(Comparator.comparing(result -> 
            result.getDocument().getContent().toLowerCase()));
    }
}
