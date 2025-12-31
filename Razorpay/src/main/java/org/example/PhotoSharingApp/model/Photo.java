package org.example.PhotoSharingApp.model;

import org.example.PhotoSharingApp.enums.PrivacyLevel;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a photo in the system.
 * Builder Pattern: Uses builder for flexible object construction.
 * Encapsulation: All fields are private with controlled access.
 */
public class Photo {
    private final String photoId;
    private final String userId;
    private final String imageUrl;
    private final String caption;
    private final LocalDateTime uploadTime;
    private final PrivacyLevel privacyLevel;
    private final Set<String> tags;
    private final Set<String> likedByUsers;
    private final List<Comment> comments;
    
    /**
     * Private constructor to enforce builder pattern usage.
     */
    private Photo(Builder builder) {
        this.photoId = builder.photoId;
        this.userId = builder.userId;
        this.imageUrl = builder.imageUrl;
        this.caption = builder.caption;
        this.uploadTime = builder.uploadTime;
        this.privacyLevel = builder.privacyLevel;
        this.tags = new HashSet<>(builder.tags);
        this.likedByUsers = new HashSet<>();
        this.comments = new ArrayList<>();
    }
    
    /**
     * Adds a like from a user.
     * 
     * @param userId the ID of the user who liked the photo
     */
    public void addLike(String userId) {
        likedByUsers.add(userId);
    }
    
    /**
     * Removes a like from a user.
     * 
     * @param userId the ID of the user who unliked the photo
     */
    public void removeLike(String userId) {
        likedByUsers.remove(userId);
    }
    
    /**
     * Adds a comment to the photo.
     * 
     * @param comment the comment to add
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }
    
    /**
     * Checks if a user has liked this photo.
     * 
     * @param userId the ID of the user
     * @return true if the user has liked this photo
     */
    public boolean isLikedBy(String userId) {
        return likedByUsers.contains(userId);
    }
    
    /**
     * Gets the number of likes on this photo.
     * 
     * @return the like count
     */
    public int getLikeCount() {
        return likedByUsers.size();
    }
    
    /**
     * Gets the number of comments on this photo.
     * 
     * @return the comment count
     */
    public int getCommentCount() {
        return comments.size();
    }
    
    // Getters
    public String getPhotoId() {
        return photoId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public String getCaption() {
        return caption;
    }
    
    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
    
    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }
    
    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }
    
    public Set<String> getLikedByUsers() {
        return Collections.unmodifiableSet(likedByUsers);
    }
    
    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }
    
    @Override
    public String toString() {
        return String.format("Photo{id='%s', caption='%s', likes=%d, comments=%d, privacy=%s}",
            photoId, caption, getLikeCount(), getCommentCount(), privacyLevel);
    }
    
    /**
     * Builder Pattern: Provides fluent API for creating Photo objects.
     */
    public static class Builder {
        private String photoId;
        private String userId;
        private String imageUrl;
        private String caption;
        private LocalDateTime uploadTime;
        private PrivacyLevel privacyLevel = PrivacyLevel.PUBLIC;
        private Set<String> tags = new HashSet<>();
        
        public Builder(String photoId, String userId, String imageUrl) {
            this.photoId = photoId;
            this.userId = userId;
            this.imageUrl = imageUrl;
            this.uploadTime = LocalDateTime.now();
        }
        
        public Builder caption(String caption) {
            this.caption = caption;
            return this;
        }
        
        public Builder uploadTime(LocalDateTime uploadTime) {
            this.uploadTime = uploadTime;
            return this;
        }
        
        public Builder privacyLevel(PrivacyLevel privacyLevel) {
            this.privacyLevel = privacyLevel;
            return this;
        }
        
        public Builder addTag(String tag) {
            this.tags.add(tag);
            return this;
        }
        
        public Builder tags(Set<String> tags) {
            this.tags = new HashSet<>(tags);
            return this;
        }
        
        public Photo build() {
            return new Photo(this);
        }
    }
}







