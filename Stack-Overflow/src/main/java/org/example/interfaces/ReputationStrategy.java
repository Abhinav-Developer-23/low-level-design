package org.example.interfaces;

import org.example.enums.VoteType;

/**
 * Strategy Pattern: Different reputation calculation strategies
 */
public interface ReputationStrategy {
    int calculateQuestionVoteReputation(VoteType voteType);
    int calculateAnswerVoteReputation(VoteType voteType);
    int calculateAcceptedAnswerReputation();
    int calculateQuestionPostedReputation();
    int calculateAnswerPostedReputation();
}

