package org.example.PhotoSharingApp.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a comment on a photo.
 * Immutable class for thread safety and simplicity.
 */
public class Comment {
    private final String commentId;
    private final String userId;
    private final String photoId;
    private final String content;
    private final LocalDateTime timestamp;
    
    public Comment(String userId, String photoId, String content) {
        this.commentId = UUID.randomUUID().toString();
        this.userId = userId;
        this.photoId = photoId;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getCommentId() {
        return commentId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getPhotoId() {
        return photoId;
    }
    
    public String getContent() {
        return content;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return String.format("Comment{user='%s', content='%s', time=%s}",
            userId, content, timestamp);
    }
}







