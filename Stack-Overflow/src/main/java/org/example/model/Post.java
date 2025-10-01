package org.example.model;

import org.example.enums.VoteType;
import org.example.interfaces.Commentable;
import org.example.interfaces.Votable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Abstract base class for posts (Question and Answer)
 * Implements common functionality for voting and commenting
 */
public abstract class Post implements Votable, Commentable {
    
    protected final String id;
    protected final User author;
    protected String content;
    protected final LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    
    // Simplified voting - just track vote types per user
    //user tracking is required
    //Vote Spam: User can click upvote 10 times = 10 votes
    protected final Map<String, VoteType> userVotes; // userId -> VoteType
    protected final AtomicInteger upvotes;
    protected final AtomicInteger downvotes;
    
    // Comments
    protected final List<Comment> comments;

    protected Post(String id, User author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.userVotes = new ConcurrentHashMap<>();
        this.upvotes = new AtomicInteger(0);
        this.downvotes = new AtomicInteger(0);
        this.comments = new CopyOnWriteArrayList<>();
    }

    // Votable interface implementation
    @Override
    public Vote addVote(Vote vote) {
        String userId = vote.getUser().getUserId();
        VoteType newVoteType = vote.getVoteType();
        VoteType existingVoteType = userVotes.get(userId);
        
        if (existingVoteType != null) {
            // User already voted, remove old vote
            if (existingVoteType == VoteType.UPVOTE) {
                upvotes.decrementAndGet();
            } else {
                downvotes.decrementAndGet();
            }
        }
        
        // Add new vote
        userVotes.put(userId, newVoteType);
        if (newVoteType == VoteType.UPVOTE) {
            upvotes.incrementAndGet();
        } else {
            downvotes.incrementAndGet();
        }
        
        return vote;
    }

    @Override
    public boolean removeVote(String userId) {
        VoteType voteType = userVotes.remove(userId);
        if (voteType != null) {
            if (voteType == VoteType.UPVOTE) {
                upvotes.decrementAndGet();
            } else {
                downvotes.decrementAndGet();
            }
            return true;
        }
        return false;
    }

    @Override
    public int getVoteCount() {
        return upvotes.get() - downvotes.get();
    }

    public int getUpvotes() {
        return upvotes.get();
    }

    public int getDownvotes() {
        return downvotes.get();
    }

    public VoteType getUserVote(String userId) {
        return userVotes.get(userId);
    }

    // Commentable interface implementation
    @Override
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    @Override
    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    // Update content
    public synchronized void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    // Common getters
    public String getId() {
        return id;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

