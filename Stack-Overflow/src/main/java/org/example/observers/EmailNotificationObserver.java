package org.example.observers;

import lombok.Getter;
import org.example.interfaces.NotificationObserver;
import org.example.model.User;

/**
 * Observer Pattern: Email implementation for notifications
 * In a real system, this would send actual emails
 */
@Getter
public class EmailNotificationObserver implements NotificationObserver {
    
    private final User user;

    public EmailNotificationObserver(User user) {
        this.user = user;
    }

    @Override
    public void onQuestionAnswered(String questionId, String answerId, User answerer) {
        sendEmail("New Answer on Your Question", 
            "Your question (ID: " + questionId + ") has a new answer by " + answerer.getUsername());
    }

    @Override
    public void onAnswerAccepted(String answerId, User accepter) {
        sendEmail("Your Answer Was Accepted!", 
            "Congratulations! Your answer (ID: " + answerId + ") has been accepted.");
    }

    @Override
    public void onQuestionCommented(String questionId, String commentId, User commenter) {
        sendEmail("New Comment on Your Question", 
            "Your question (ID: " + questionId + ") has a new comment by " + commenter.getUsername());
    }

    @Override
    public void onAnswerCommented(String answerId, String commentId, User commenter) {
        sendEmail("New Comment on Your Answer", 
            "Your answer (ID: " + answerId + ") has a new comment by " + commenter.getUsername());
    }

    @Override
    public void onQuestionVoted(String questionId, User voter) {
        sendEmail("Vote on Your Question", 
            "Your question (ID: " + questionId + ") received a vote");
    }

    @Override
    public void onAnswerVoted(String answerId, User voter) {
        sendEmail("Vote on Your Answer", 
            "Your answer (ID: " + answerId + ") received a vote");
    }

    private void sendEmail(String subject, String body) {
        // Simulated email sending
        System.out.println("[EMAIL] To: " + user.getEmail() + " | Subject: " + subject + " | Body: " + body);
    }

}

