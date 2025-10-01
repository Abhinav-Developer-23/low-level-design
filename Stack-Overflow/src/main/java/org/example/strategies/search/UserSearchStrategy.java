package org.example.strategies.search;

import org.example.interfaces.SearchStrategy;
import org.example.model.Question;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy Pattern: Search by username
 */
public class UserSearchStrategy implements SearchStrategy {
    
    @Override
    public List<Question> search(String query, List<Question> questions) {
        String lowerQuery = query.toLowerCase();
        
        return questions.stream()
                .filter(q -> q.getAuthor().getUsername().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }
}

