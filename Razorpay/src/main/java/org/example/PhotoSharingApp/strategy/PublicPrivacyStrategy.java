package org.example.PhotoSharingApp.strategy;

import org.example.PhotoSharingApp.interfaces.PrivacyStrategy;
import org.example.PhotoSharingApp.model.Photo;
import org.example.PhotoSharingApp.model.User;

/**
 * Strategy Pattern: Concrete strategy for public photos.
 * Public photos can be viewed by anyone.
 */
public class PublicPrivacyStrategy implements PrivacyStrategy {
    
    @Override
    public boolean canView(User viewer, Photo photo) {
        // Public photos are visible to everyone
        return true;
    }
}







