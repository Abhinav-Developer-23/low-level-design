package org.example.strategies.search;

import org.example.interfaces.SearchStrategy;
import org.example.model.Question;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy Pattern: Search by keywords in title or content
 */
public class KeywordSearchStrategy implements SearchStrategy {
    
    @Override
    public List<Question> search(String query, List<Question> questions) {
        String lowerQuery = query.toLowerCase();
        
        return questions.stream()
                .filter(q -> q.getTitle().toLowerCase().contains(lowerQuery) ||
                           q.getContent().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
}

