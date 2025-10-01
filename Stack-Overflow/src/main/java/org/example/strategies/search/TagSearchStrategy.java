package org.example.strategies.search;

import org.example.interfaces.SearchStrategy;
import org.example.model.Question;
import org.example.model.Tag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy Pattern: Search by tags
 */
public class TagSearchStrategy implements SearchStrategy {
    
    @Override
    public List<Question> search(String query, List<Question> questions) {
        String lowerQuery = query.toLowerCase();
        
        return questions.stream()
                .filter(q -> q.getTags().stream()
                        .map(Tag::getName)
                        .anyMatch(tagName -> tagName.toLowerCase().contains(lowerQuery)))
                .collect(Collectors.toList());
    }
}

