package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user in the LinkedIn system
 * Uses Builder pattern for flexible object construction
 */
public class User {
    private final String userId;
    private final String email;
    private final String passwordHash;
    private String firstName;
    private String lastName;
    private Profile profile;
    private List<String> connectionIds;
    private List<String> pendingRequestIds;
    private List<String> postedJobIds;

    // Private constructor for Builder pattern
    private User(Builder builder) {
        this.userId = UUID.randomUUID().toString();
        this.email = builder.email;
        this.passwordHash = builder.passwordHash;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.profile = builder.profile != null ? builder.profile : new Profile();
        this.connectionIds = new ArrayList<>();
        this.pendingRequestIds = new ArrayList<>();
        this.postedJobIds = new ArrayList<>();
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<String> getConnectionIds() {
        return new ArrayList<>(connectionIds);
    }

    public void addConnection(String userId) {
        if (!connectionIds.contains(userId)) {
            connectionIds.add(userId);
        }
    }

    public void removeConnection(String userId) {
        connectionIds.remove(userId);
    }

    public List<String> getPendingRequestIds() {
        return new ArrayList<>(pendingRequestIds);
    }

    public void addPendingRequest(String connectionId) {
        if (!pendingRequestIds.contains(connectionId)) {
            pendingRequestIds.add(connectionId);
        }
    }

    public void removePendingRequest(String connectionId) {
        pendingRequestIds.remove(connectionId);
    }

    public List<String> getPostedJobIds() {
        return new ArrayList<>(postedJobIds);
    }

    public void addPostedJob(String jobId) {
        if (!postedJobIds.contains(jobId)) {
            postedJobIds.add(jobId);
        }
    }

    public boolean isConnectedTo(String userId) {
        return connectionIds.contains(userId);
    }

    /**
     * Builder class for User
     */
    public static class Builder {
        private final String email;
        private final String passwordHash;
        private String firstName;
        private String lastName;
        private Profile profile;

        public Builder(String email, String passwordHash) {
            this.email = email;
            this.passwordHash = passwordHash;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder profile(Profile profile) {
            this.profile = profile;
            return this;
        }

        public User build() {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            if (passwordHash == null || passwordHash.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }
            return new User(this);
        }
    }
}

