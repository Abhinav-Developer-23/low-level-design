package org.example.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {
    private final String commentId;
    private final User author;
    private String content;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment(String commentId, User author, String content) {
        this.commentId = commentId;
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public synchronized void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    public String getCommentId() {
        return commentId;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(commentId, comment.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }

    @Override
    public String toString() {
        return "Comment by " + author.getUsername() + ": " + content;
    }
}

