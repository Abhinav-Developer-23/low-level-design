package org.example.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user in the LinkedIn system.
 */
public class User {
    private final String userId;
    private String email;
    private String passwordHash;
    private Profile profile;
    private final List<String> connectionIds;
    private final List<String> pendingConnectionRequestIds;
    private final LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private boolean isActive;

    public User(String email, String passwordHash) {
        this.userId = UUID.randomUUID().toString();
        this.email = email;
        this.passwordHash = passwordHash;
        this.connectionIds = new ArrayList<>();
        this.pendingConnectionRequestIds = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.isActive = true;
    }

    // Builder pattern for flexible user creation
    public static class Builder {
        private final String email;
        private final String passwordHash;
        private Profile profile;

        public Builder(String email, String passwordHash) {
            this.email = email;
            this.passwordHash = passwordHash;
        }

        public Builder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public User build() {
            User user = new User(email, passwordHash);
            user.profile = this.profile;
            return user;
        }
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Profile getProfile() {
        return profile;
    }

    public List<String> getConnectionIds() {
        return new ArrayList<>(connectionIds);
    }

    public List<String> getPendingConnectionRequestIds() {
        return new ArrayList<>(pendingConnectionRequestIds);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public boolean isActive() {
        return isActive;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Connection management methods
    public void addConnection(String userId) {
        if (!connectionIds.contains(userId)) {
            connectionIds.add(userId);
        }
    }

    public void removeConnection(String userId) {
        connectionIds.remove(userId);
    }

    public void addPendingConnectionRequest(String userId) {
        if (!pendingConnectionRequestIds.contains(userId)) {
            pendingConnectionRequestIds.add(userId);
        }
    }

    public void removePendingConnectionRequest(String userId) {
        pendingConnectionRequestIds.remove(userId);
    }

    public boolean isConnectedWith(String userId) {
        return connectionIds.contains(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", profile=" + (profile != null ? profile.getFirstName() + " " + profile.getLastName() : "null") +
                ", connections=" + connectionIds.size() +
                ", isActive=" + isActive +
                '}';
    }
}

