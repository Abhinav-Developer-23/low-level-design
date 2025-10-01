package org.example.interfaces;

import org.example.model.Comment;
import java.util.List;

/**
 * Interface Segregation Principle: 
 * Separate interface for entities that can be commented on
 */
public interface Commentable {
    void addComment(Comment comment);
    List<Comment> getComments();
}

