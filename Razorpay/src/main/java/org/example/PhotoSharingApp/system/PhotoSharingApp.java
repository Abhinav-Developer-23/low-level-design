package org.example.PhotoSharingApp.system;

import org.example.PhotoSharingApp.model.Comment;
import org.example.PhotoSharingApp.model.Photo;
import org.example.PhotoSharingApp.model.User;
import org.example.PhotoSharingApp.service.*;

import java.util.List;

/**
 * Main system class for the Photo Sharing Application.
 * Singleton Pattern: Ensures only one instance of the system exists.
 * Facade Pattern: Provides a unified interface to the subsystems.
 */
public class PhotoSharingApp {
    private static PhotoSharingApp instance;
    
    private final UserService userService;
    private final PhotoService photoService;
    private final FeedService feedService;
    private final NotificationService notificationService;
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private PhotoSharingApp() {
        this.userService = new UserService();
        this.photoService = new PhotoService();
        this.feedService = new FeedService(photoService);
        this.notificationService = new NotificationService();
    }
    
    /**
     * Gets the singleton instance of the application.
     * Thread-safe lazy initialization.
     * 
     * @return the singleton instance
     */
    public static synchronized PhotoSharingApp getInstance() {
        if (instance == null) {
            instance = new PhotoSharingApp();
        }
        return instance;
    }
    
    /**
     * Registers a new user.
     * 
     * @param user the user to register
     */
    public void registerUser(User user) {
        userService.registerUser(user);
    }
    
    /**
     * Gets a user by ID.
     * 
     * @param userId the user ID
     * @return the user
     */
    public User getUser(String userId) {
        return userService.getUser(userId);
    }
    
    /**
     * Makes one user follow another and sends notification.
     * 
     * @param followerId the follower user ID
     * @param followeeId the followee user ID
     */
    public void followUser(String followerId, String followeeId) {
        userService.followUser(followerId, followeeId);
        
        // Send notification
        User follower = userService.getUser(followerId);
        User followee = userService.getUser(followeeId);
        notificationService.notifyNewFollower(followee, follower);
    }
    
    /**
     * Makes one user unfollow another.
     * 
     * @param followerId the follower user ID
     * @param followeeId the followee user ID
     */
    public void unfollowUser(String followerId, String followeeId) {
        userService.unfollowUser(followerId, followeeId);
    }
    
    /**
     * Uploads a photo and notifies followers.
     * 
     * @param photo the photo to upload
     * @param userId the user ID uploading the photo
     */
    public void uploadPhoto(Photo photo, String userId) {
        User user = userService.getUser(userId);
        photoService.uploadPhoto(photo, user);
        
        // Notify all followers about the new photo
        for (String followerId : user.getFollowers()) {
            User follower = userService.getUser(followerId);
            notificationService.notifyNewPhotoFromFollowing(follower, user, photo.getPhotoId());
        }
    }
    
    /**
     * Gets a photo if the user has permission.
     * 
     * @param viewerId the viewer user ID
     * @param photoId the photo ID
     * @return the photo
     */
    public Photo getPhoto(String viewerId, String photoId) {
        User viewer = userService.getUser(viewerId);
        return photoService.getPhotoForUser(viewer, photoId);
    }
    
    /**
     * Likes a photo and sends notification.
     * 
     * @param photoId the photo ID
     * @param userId the user ID who likes
     */
    public void likePhoto(String photoId, String userId) {
        User liker = userService.getUser(userId);
        Photo photo = photoService.getPhoto(photoId);
        
        if (!photo.isLikedBy(userId)) {
            photoService.likePhoto(photoId, userId);
            
            // Notify photo owner (if not liking own photo)
            if (!photo.getUserId().equals(userId)) {
                User photoOwner = userService.getUser(photo.getUserId());
                notificationService.notifyPhotoLiked(photoOwner, liker, photoId);
            }
            
            System.out.println(liker.getUsername() + " liked the photo");
        }
    }
    
    /**
     * Unlikes a photo.
     * 
     * @param photoId the photo ID
     * @param userId the user ID who unlikes
     */
    public void unlikePhoto(String photoId, String userId) {
        User unliker = userService.getUser(userId);
        photoService.unlikePhoto(photoId, userId);
        System.out.println(unliker.getUsername() + " unliked the photo");
    }
    
    /**
     * Adds a comment to a photo and sends notification.
     * 
     * @param photoId the photo ID
     * @param userId the user ID commenting
     * @param content the comment content
     */
    public void commentOnPhoto(String photoId, String userId, String content) {
        User commenter = userService.getUser(userId);
        Photo photo = photoService.getPhoto(photoId);
        
        Comment comment = new Comment(userId, photoId, content);
        photoService.addComment(photoId, comment);
        
        // Notify photo owner (if not commenting on own photo)
        if (!photo.getUserId().equals(userId)) {
            User photoOwner = userService.getUser(photo.getUserId());
            notificationService.notifyPhotoCommented(photoOwner, commenter, photoId);
        }
        
        System.out.println(commenter.getUsername() + " commented: " + content);
    }
    
    /**
     * Searches photos by tag.
     * 
     * @param tag the tag to search
     * @param userId the user ID searching
     * @return list of matching photos
     */
    public List<Photo> searchByTag(String tag, String userId) {
        User user = userService.getUser(userId);
        return photoService.searchByTag(tag, user);
    }
    
    /**
     * Gets photos uploaded by a specific user.
     * 
     * @param targetUserId the user whose photos to get
     * @param viewerId the user viewing the photos
     * @return list of photos
     */
    public List<Photo> getUserPhotos(String targetUserId, String viewerId) {
        User viewer = userService.getUser(viewerId);
        return photoService.getPhotosBy(targetUserId, viewer);
    }
    
    /**
     * Generates a personalized feed for a user.
     * 
     * @param userId the user ID
     * @param limit maximum number of photos
     * @return list of photos in the feed
     */
    public List<Photo> getFeed(String userId, int limit) {
        User user = userService.getUser(userId);
        return feedService.generateFeed(user, limit);
    }
    
    /**
     * Generates an explore feed showing popular photos.
     * 
     * @param userId the user ID
     * @param limit maximum number of photos
     * @return list of popular photos
     */
    public List<Photo> getExploreFeed(String userId, int limit) {
        User user = userService.getUser(userId);
        return feedService.generateExploreFeed(user, limit);
    }
    
    /**
     * Displays a user's notifications.
     * 
     * @param userId the user ID
     */
    public void showNotifications(String userId) {
        User user = userService.getUser(userId);
        System.out.println("\n=== Notifications for " + user.getUsername() + " ===");
        if (user.getNotifications().isEmpty()) {
            System.out.println("No notifications");
        } else {
            user.getNotifications().forEach(System.out::println);
        }
    }
}

