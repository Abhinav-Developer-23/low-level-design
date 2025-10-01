package org.example.interfaces;

import org.example.model.Vote;

/**
 * Interface Segregation Principle: 
 * Separate interface for entities that can be voted on
 */
public interface Votable {
    Vote addVote(Vote vote);
    boolean removeVote(String userId);
    int getVoteCount();
}

