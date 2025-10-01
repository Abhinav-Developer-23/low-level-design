package org.example.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Answer {
    private final String answerId;
    private final User author;
    private String content;
    private final Question question;
    private boolean isAccepted;
    private final AtomicInteger voteCount;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final Map<String, Vote> votes; // userId -> Vote
    private final List<Comment> comments;

    public Answer(String answerId, User author, String content, Question question) {
        this.answerId = answerId;
        this.author = author;
        this.content = content;
        this.question = question;
        this.isAccepted = false;
        this.voteCount = new AtomicInteger(0);
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.votes = new ConcurrentHashMap<>();
        this.comments = new CopyOnWriteArrayList<>();
    }

    public synchronized void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    public synchronized void markAsAccepted() {
        this.isAccepted = true;
    }

    public synchronized void unmarkAsAccepted() {
        this.isAccepted = false;
    }

    public Vote addVote(Vote vote) {
        Vote existingVote = votes.get(vote.getUser().getUserId());
        
        if (existingVote != null) {
            // User already voted, calculate the difference
            int oldValue = existingVote.getVoteType().getValue();
            int newValue = vote.getVoteType().getValue();
            
            if (oldValue != newValue) {
                existingVote.changeVote(vote.getVoteType());
                voteCount.addAndGet(newValue - oldValue);
            }
            return existingVote;
        } else {
            votes.put(vote.getUser().getUserId(), vote);
            voteCount.addAndGet(vote.getVoteType().getValue());
            return vote;
        }
    }

    public boolean removeVote(String userId) {
        Vote vote = votes.remove(userId);
        if (vote != null) {
            voteCount.addAndGet(-vote.getVoteType().getValue());
            return true;
        }
        return false;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    // Getters
    public String getAnswerId() {
        return answerId;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Question getQuestion() {
        return question;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public int getVoteCount() {
        return voteCount.get();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Comment> getComments() {
        return new ArrayList<>(comments);
    }

    public Map<String, Vote> getVotes() {
        return new HashMap<>(votes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(answerId, answer.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerId);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "author=" + author.getUsername() +
                ", votes=" + voteCount.get() +
                ", accepted=" + isAccepted +
                '}';
    }
}

