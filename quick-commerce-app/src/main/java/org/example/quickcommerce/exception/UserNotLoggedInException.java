package org.example.quickcommerce.exception;

public class UserNotLoggedInException extends QuickCommerceException {
    public UserNotLoggedInException(String userId) {
        super("User must be logged in to perform this action. User ID: " + userId);
    }
}

