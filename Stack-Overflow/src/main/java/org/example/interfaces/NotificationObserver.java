package org.example.interfaces;

import org.example.model.User;

/**
 * Observer Pattern: Observers get notified of system events
 */
public interface NotificationObserver {
    void onQuestionAnswered(String questionId, String answerId, User answerer);
    void onAnswerAccepted(String answerId, User accepter);
    void onQuestionCommented(String questionId, String commentId, User commenter);
    void onAnswerCommented(String answerId, String commentId, User commenter);
    void onQuestionVoted(String questionId, User voter);
    void onAnswerVoted(String answerId, User voter);
}

