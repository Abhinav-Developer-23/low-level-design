package org.example.PhotoSharingApp.exception;

/**
 * Exception thrown when a photo is not found in the system.
 */
public class PhotoNotFoundException extends RuntimeException {
    public PhotoNotFoundException(String message) {
        super(message);
    }
}



