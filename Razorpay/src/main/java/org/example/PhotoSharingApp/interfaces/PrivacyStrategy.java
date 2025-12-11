package org.example.PhotoSharingApp.interfaces;

import org.example.PhotoSharingApp.model.Photo;
import org.example.PhotoSharingApp.model.User;

/**
 * Strategy Pattern: Interface for different privacy checking strategies.
 * Allows different privacy rules to be applied without modifying Photo class.
 */
public interface PrivacyStrategy {
    /**
     * Checks if a user can view a photo based on privacy rules.
     * 
     * @param viewer the user trying to view the photo
     * @param photo the photo being viewed
     * @return true if the user can view the photo
     */
    boolean canView(User viewer, Photo photo);
}

