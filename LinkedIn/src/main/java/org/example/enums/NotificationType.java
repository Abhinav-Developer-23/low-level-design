package org.example.enums;

/**
 * Represents different types of notifications in the system.
 */
public enum NotificationType {
    CONNECTION_REQUEST,     // New connection request received
    CONNECTION_ACCEPTED,    // Connection request accepted
    NEW_MESSAGE,           // New message received
    JOB_POSTING,          // New job posting matching user profile
    JOB_APPLICATION,      // Application status update
    PROFILE_VIEW,         // Someone viewed your profile
    POST_LIKE,            // Someone liked your post
    POST_COMMENT          // Someone commented on your post
}

