package org.example.strategies.search;

import org.example.interfaces.SearchStrategy;
import org.example.model.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Composite Pattern: Combines multiple search strategies
 */
public class CompositeSearchStrategy implements SearchStrategy {
    
    private final List<SearchStrategy> strategies;

    public CompositeSearchStrategy(List<SearchStrategy> strategies) {
        this.strategies = strategies;
    }

    @Override
    public List<Question> search(String query, List<Question> questions) {
        Set<Question> results = new HashSet<>();
        
        for (SearchStrategy strategy : strategies) {
            results.addAll(strategy.search(query, questions));
        }
        
        return new ArrayList<>(results);
    }
}

