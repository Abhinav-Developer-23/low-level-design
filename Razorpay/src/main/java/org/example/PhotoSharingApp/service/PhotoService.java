package org.example.PhotoSharingApp.service;

import org.example.PhotoSharingApp.exception.PhotoNotFoundException;
import org.example.PhotoSharingApp.exception.UnauthorizedException;
import org.example.PhotoSharingApp.interfaces.PrivacyStrategy;
import org.example.PhotoSharingApp.model.Comment;
import org.example.PhotoSharingApp.model.Photo;
import org.example.PhotoSharingApp.model.User;
import org.example.PhotoSharingApp.strategy.PrivacyStrategyFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing photos.
 * Single Responsibility: Handles all photo-related operations.
 */
public class PhotoService {
    private final Map<String, Photo> photos;
    private final Map<String, Set<String>> tagIndex; // tag -> Set of photoIds
    
    public PhotoService() {
        this.photos = new HashMap<>();
        this.tagIndex = new HashMap<>();
    }
    
    /**
     * Uploads a new photo.
     * 
     * @param photo the photo to upload
     * @param user the user uploading the photo
     */
    public void uploadPhoto(Photo photo, User user) {
        photos.put(photo.getPhotoId(), photo);
        user.addPhoto(photo.getPhotoId());
        
        // Index tags for searching
        for (String tag : photo.getTags()) {
            tagIndex.computeIfAbsent(tag, k -> new HashSet<>()).add(photo.getPhotoId());
        }
        
        System.out.println("Photo uploaded by " + user.getUsername() + ": " + photo.getCaption());
    }
    
    /**
     * Retrieves a photo by ID.
     * 
     * @param photoId the photo ID
     * @return the photo
     * @throws PhotoNotFoundException if photo doesn't exist
     */
    public Photo getPhoto(String photoId) {
        Photo photo = photos.get(photoId);
        if (photo == null) {
            throw new PhotoNotFoundException("Photo not found: " + photoId);
        }
        return photo;
    }
    
    /**
     * Checks if a user can view a photo based on privacy settings.
     * 
     * @param viewer the user trying to view
     * @param photoId the photo ID
     * @return true if the user can view the photo
     */
    public boolean canViewPhoto(User viewer, String photoId) {
        Photo photo = getPhoto(photoId);
        PrivacyStrategy strategy = PrivacyStrategyFactory.getStrategy(photo.getPrivacyLevel());
        return strategy.canView(viewer, photo);
    }
    
    /**
     * Gets a photo if the viewer has permission.
     * 
     * @param viewer the user trying to view
     * @param photoId the photo ID
     * @return the photo
     * @throws UnauthorizedException if viewer cannot access the photo
     */
    public Photo getPhotoForUser(User viewer, String photoId) {
        if (!canViewPhoto(viewer, photoId)) {
            throw new UnauthorizedException("You don't have permission to view this photo");
        }
        return getPhoto(photoId);
    }
    
    /**
     * Adds a like to a photo.
     * 
     * @param photoId the photo ID
     * @param userId the user ID who liked
     */
    public void likePhoto(String photoId, String userId) {
        Photo photo = getPhoto(photoId);
        photo.addLike(userId);
    }
    
    /**
     * Removes a like from a photo.
     * 
     * @param photoId the photo ID
     * @param userId the user ID who unliked
     */
    public void unlikePhoto(String photoId, String userId) {
        Photo photo = getPhoto(photoId);
        photo.removeLike(userId);
    }
    
    /**
     * Adds a comment to a photo.
     * 
     * @param photoId the photo ID
     * @param comment the comment to add
     */
    public void addComment(String photoId, Comment comment) {
        Photo photo = getPhoto(photoId);
        photo.addComment(comment);
    }
    
    /**
     * Searches photos by tag.
     * 
     * @param tag the tag to search for
     * @param viewer the user performing the search
     * @return list of photos with the tag that the user can view
     */
    public List<Photo> searchByTag(String tag, User viewer) {
        Set<String> photoIds = tagIndex.getOrDefault(tag, Collections.emptySet());
        return photoIds.stream()
            .map(photos::get)
            .filter(photo -> {
                PrivacyStrategy strategy = PrivacyStrategyFactory.getStrategy(photo.getPrivacyLevel());
                return strategy.canView(viewer, photo);
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Gets all photos uploaded by a specific user.
     * 
     * @param userId the user ID
     * @param viewer the user viewing the photos
     * @return list of photos that the viewer can see
     */
    public List<Photo> getPhotosBy(String userId, User viewer) {
        return photos.values().stream()
            .filter(photo -> photo.getUserId().equals(userId))
            .filter(photo -> {
                PrivacyStrategy strategy = PrivacyStrategyFactory.getStrategy(photo.getPrivacyLevel());
                return strategy.canView(viewer, photo);
            })
            .sorted(Comparator.comparing(Photo::getUploadTime).reversed())
            .collect(Collectors.toList());
    }
    
    /**
     * Gets all photos in the system.
     * 
     * @return map of photo IDs to photos
     */
    public Map<String, Photo> getAllPhotos() {
        return new HashMap<>(photos);
    }
}






