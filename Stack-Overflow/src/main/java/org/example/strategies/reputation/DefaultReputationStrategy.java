package org.example.strategies.reputation;

import org.example.enums.VoteType;
import org.example.interfaces.ReputationStrategy;

/**
 * Default implementation of reputation calculation
 * Following Stack Overflow's reputation system
 */
public class DefaultReputationStrategy implements ReputationStrategy {
    
    private static final int QUESTION_UPVOTE_REP = 5;
    private static final int QUESTION_DOWNVOTE_REP = -2;
    private static final int ANSWER_UPVOTE_REP = 10;
    private static final int ANSWER_DOWNVOTE_REP = -2;
    private static final int ACCEPTED_ANSWER_REP = 15;
    private static final int QUESTION_POSTED_REP = 0;
    private static final int ANSWER_POSTED_REP = 0;

    @Override
    public int calculateQuestionVoteReputation(VoteType voteType) {
        return voteType == VoteType.UPVOTE ? QUESTION_UPVOTE_REP : QUESTION_DOWNVOTE_REP;
    }

    @Override
    public int calculateAnswerVoteReputation(VoteType voteType) {
        return voteType == VoteType.UPVOTE ? ANSWER_UPVOTE_REP : ANSWER_DOWNVOTE_REP;
    }

    @Override
    public int calculateAcceptedAnswerReputation() {
        return ACCEPTED_ANSWER_REP;
    }

    @Override
    public int calculateQuestionPostedReputation() {
        return QUESTION_POSTED_REP;
    }

    @Override
    public int calculateAnswerPostedReputation() {
        return ANSWER_POSTED_REP;
    }
}

