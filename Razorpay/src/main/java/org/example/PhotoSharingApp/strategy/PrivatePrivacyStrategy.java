package org.example.PhotoSharingApp.strategy;

import org.example.PhotoSharingApp.interfaces.PrivacyStrategy;
import org.example.PhotoSharingApp.model.Photo;
import org.example.PhotoSharingApp.model.User;

/**
 * Strategy Pattern: Concrete strategy for private photos.
 * Photos can only be viewed by the owner.
 */
public class PrivatePrivacyStrategy implements PrivacyStrategy {
    
    @Override
    public boolean canView(User viewer, Photo photo) {
        // Only the owner can view private photos
        return viewer.getUserId().equals(photo.getUserId());
    }
}



