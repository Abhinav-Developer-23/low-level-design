package org.example.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.example.enums.VoteType;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter

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


}

