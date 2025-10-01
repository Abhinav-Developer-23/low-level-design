package org.example.observers;

import org.example.interfaces.NotificationObserver;
import org.example.model.User;

/**
 * Observer Pattern: Console implementation for notifications
 */
public class ConsoleNotificationObserver implements NotificationObserver {
    
    private final User user;

    public ConsoleNotificationObserver(User user) {
        this.user = user;
    }

    @Override
    public void onQuestionAnswered(String questionId, String answerId, User answerer) {
        System.out.println("[NOTIFICATION] " + user.getUsername() + 
            ": Your question (ID: " + questionId + ") has a new answer by " + answerer.getUsername());
    }

    @Override
    public void onAnswerAccepted(String answerId, User accepter) {
        System.out.println("[NOTIFICATION] " + user.getUsername() + 
            ": Your answer (ID: " + answerId + ") has been accepted!");
    }

    @Override
    public void onQuestionCommented(String questionId, String commentId, User commenter) {
        System.out.println("[NOTIFICATION] " + user.getUsername() + 
            ": Your question (ID: " + questionId + ") has a new comment by " + commenter.getUsername());
    }

    @Override
    public void onAnswerCommented(String answerId, String commentId, User commenter) {
        System.out.println("[NOTIFICATION] " + user.getUsername() + 
            ": Your answer (ID: " + answerId + ") has a new comment by " + commenter.getUsername());
    }

    @Override
    public void onQuestionVoted(String questionId, User voter) {
        System.out.println("[NOTIFICATION] " + user.getUsername() + 
            ": Your question (ID: " + questionId + ") received a vote");
    }

    @Override
    public void onAnswerVoted(String answerId, User voter) {
        System.out.println("[NOTIFICATION] " + user.getUsername() + 
            ": Your answer (ID: " + answerId + ") received a vote");
    }

    public User getUser() {
        return user;
    }
}

