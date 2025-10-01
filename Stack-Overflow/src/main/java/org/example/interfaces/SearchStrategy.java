package org.example.interfaces;

import org.example.model.Question;
import java.util.List;

/**
 * Strategy Pattern: Different search strategies for questions
 */
public interface SearchStrategy {
    List<Question> search(String query, List<Question> questions);
}

