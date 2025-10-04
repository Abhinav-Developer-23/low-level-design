package org.example.interfaces;

import org.example.model.User;

/**
 * Interface for user authentication and registration
 * Follows Interface Segregation Principle
 */
public interface AuthenticationService {
    /**
     * Register a new user
     */
    User register(String email, String password, String firstName, String lastName);

    /**
     * Authenticate a user with email and password
     */
    User authenticate(String email, String password);

    /**
     * Get user by user ID
     */
    User getUserById(String userId);

    /**
     * Get user by email
     */
    User getUserByEmail(String email);

    /**
     * Change user password
     */
    boolean changePassword(String userId, String oldPassword, String newPassword);

    /**
     * Check if email already exists
     */
    boolean emailExists(String email);
}

