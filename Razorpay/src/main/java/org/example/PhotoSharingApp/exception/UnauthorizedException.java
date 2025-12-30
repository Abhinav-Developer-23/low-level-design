package org.example.PhotoSharingApp.exception;

/**
 * Exception thrown when a user tries to perform an unauthorized action.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}





