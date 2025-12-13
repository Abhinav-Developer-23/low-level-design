package org.example.PhotoSharingApp.model;

import org.example.PhotoSharingApp.interfaces.Observer;

import java.util.*;

/**
 * Represents a user in the photo-sharing application.
 * Implements Observer pattern to receive notifications.
 * Encapsulation: All fields are private with controlled access.
 */
public class User implements Observer {
    private final String userId;
    private final String username;
    private final String email;
    private final Set<String> followers;
    private final Set<String> following;
    private final List<String> photoIds;
    private final List<Notification> notifications;
    
    /**
     * Private constructor to enforce builder pattern usage.
     */
    private User(Builder builder) {
        this.userId = builder.userId;
        this.username = builder.username;
        this.email = builder.email;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.photoIds = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }
    
    /**
     * Adds a follower to this user.
     * 
     * @param followerId the ID of the follower
     */
    public void addFollower(String followerId) {
        followers.add(followerId);
    }
    
    /**
     * Removes a follower from this user.
     * 
     * @param followerId the ID of the follower
     */
    public void removeFollower(String followerId) {
        followers.remove(followerId);
    }
    
    /**
     * Adds a user that this user is following.
     * 
     * @param userId the ID of the user to follow
     */
    public void follow(String userId) {
        following.add(userId);
    }
    
    /**
     * Removes a user from the following list.
     * 
     * @param userId the ID of the user to unfollow
     */
    public void unfollow(String userId) {
        following.remove(userId);
    }
    
    /**
     * Adds a photo ID to the user's photo collection.
     * 
     * @param photoId the ID of the photo
     */
    public void addPhoto(String photoId) {
        photoIds.add(photoId);
    }
    
    /**
     * Checks if this user is following another user.
     * 
     * @param userId the ID of the user to check
     * @return true if following
     */
    public boolean isFollowing(String userId) {
        return following.contains(userId);
    }
    
    /**
     * Checks if a user is a follower of this user.
     * 
     * @param userId the ID of the user to check
     * @return true if they are a follower
     */
    public boolean isFollower(String userId) {
        return followers.contains(userId);
    }
    
    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }
    
    // Getters
    public String getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public Set<String> getFollowers() {
        return Collections.unmodifiableSet(followers);
    }
    
    public Set<String> getFollowing() {
        return Collections.unmodifiableSet(following);
    }
    
    public List<String> getPhotoIds() {
        return Collections.unmodifiableList(photoIds);
    }
    
    public List<Notification> getNotifications() {
        return Collections.unmodifiableList(notifications);
    }
    
    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', followers=%d, following=%d, photos=%d}",
            userId, username, followers.size(), following.size(), photoIds.size());
    }
    
    /**
     * Builder Pattern: Provides fluent API for creating User objects.
     */
    public static class Builder {
        private String userId;
        private String username;
        private String email;
        
        public Builder(String userId) {
            this.userId = userId;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public User build() {
            return new User(this);
        }
    }
}




