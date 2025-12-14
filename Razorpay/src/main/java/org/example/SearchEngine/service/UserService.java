package org.example.SearchEngine.service;

import org.example.SearchEngine.model.User;

/**
 * Service interface for user management operations.
 * Follows Interface Segregation Principle (ISP).
 */
public interface UserService {
    /**
     * Register a new user in the system.
     * @param userId Unique identifier for the user
     * @param name User's name
     * @param email User's email
     * @return The created user
     */
    User registerUser(String userId, String name, String email);

    /**
     * Get a user by their ID.
     * @param userId The user ID
     * @return The user
     */
    User getUser(String userId);

    /**
     * Check if a user exists.
     * @param userId The user ID
     * @return true if user exists
     */
    boolean userExists(String userId);
}
