package org.example.model;

import org.example.enums.VoteType;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vote {
    private final String voteId;
    private final User user;
    private VoteType voteType;
    private final LocalDateTime createdAt;

    public Vote(String voteId, User user, VoteType voteType) {
        this.voteId = voteId;
        this.user = user;
        this.voteType = voteType;
        this.createdAt = LocalDateTime.now();
    }

    // Allow users to change their vote
    public synchronized void changeVote(VoteType newVoteType) {
        this.voteType = newVoteType;
    }

    public String getVoteId() {
        return voteId;
    }

    public User getUser() {
        return user;
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(voteId, vote.voteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteId);
    }
}

