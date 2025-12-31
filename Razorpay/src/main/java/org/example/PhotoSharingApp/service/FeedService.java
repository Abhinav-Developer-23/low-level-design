package org.example.PhotoSharingApp.service;

import org.example.PhotoSharingApp.interfaces.PrivacyStrategy;
import org.example.PhotoSharingApp.model.Photo;
import org.example.PhotoSharingApp.model.User;
import org.example.PhotoSharingApp.strategy.PrivacyStrategyFactory;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for generating user feeds.
 * Single Responsibility: Handles feed generation logic.
 */
public class FeedService {
    private final PhotoService photoService;
    
    public FeedService(PhotoService photoService) {
        this.photoService = photoService;
    }
    
    /**
     * Generates a personalized feed for a user.
     * Shows photos from users they follow, sorted by upload time.
     * 
     * @param user the user requesting the feed
     * @param limit maximum number of photos to return
     * @return list of photos in the feed
     */
    public List<Photo> generateFeed(User user, int limit) {
        return photoService.getAllPhotos().values().stream()
            // Filter to only photos from users they follow
            .filter(photo -> user.isFollowing(photo.getUserId()))
            // Check privacy settings
            .filter(photo -> {
                PrivacyStrategy strategy = PrivacyStrategyFactory.getStrategy(photo.getPrivacyLevel());
                return strategy.canView(user, photo);
            })
            // Sort by upload time (newest first)
            .sorted(Comparator.comparing(Photo::getUploadTime).reversed())
            // Limit results
            .limit(limit)
            .collect(Collectors.toList());
    }
    
    /**
     * Generates an explore feed showing popular public photos.
     * 
     * @param user the user requesting the feed
     * @param limit maximum number of photos to return
     * @return list of popular photos
     */
    public List<Photo> generateExploreFeed(User user, int limit) {
        return photoService.getAllPhotos().values().stream()
            // Check privacy settings
            .filter(photo -> {
                PrivacyStrategy strategy = PrivacyStrategyFactory.getStrategy(photo.getPrivacyLevel());
                return strategy.canView(user, photo);
            })
            // Sort by popularity (like count)
            .sorted(Comparator.comparing(Photo::getLikeCount).reversed())
            // Limit results
            .limit(limit)
            .collect(Collectors.toList());
    }
}






