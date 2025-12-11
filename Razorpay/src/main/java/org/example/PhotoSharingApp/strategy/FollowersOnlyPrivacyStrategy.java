package org.example.PhotoSharingApp.strategy;

import org.example.PhotoSharingApp.interfaces.PrivacyStrategy;
import org.example.PhotoSharingApp.model.Photo;
import org.example.PhotoSharingApp.model.User;

/**
 * Strategy Pattern: Concrete strategy for followers-only photos.
 * Photos can only be viewed by the owner or their followers.
 */
public class FollowersOnlyPrivacyStrategy implements PrivacyStrategy {
    
    @Override
    public boolean canView(User viewer, Photo photo) {
        // Owner can always view their own photos
        if (viewer.getUserId().equals(photo.getUserId())) {
            return true;
        }
        
        // Check if viewer is following the photo owner
        return viewer.isFollowing(photo.getUserId());
    }
}

