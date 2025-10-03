package org.example.interfaces;

import org.example.model.User;

/**
 * Interface for authentication operations.
 */
public interface AuthenticationService {
    /**
     * Authenticates a user with email and password.
     *
     * @param email User's email
     * @param password User's password
     * @return Authenticated user or null if authentication fails
     */
    User authenticate(String email, String password);

    /**
     * Registers a new user in the system.
     *
     * @param email User's email
     * @param password User's password
     * @param firstName User's first name
     * @param lastName User's last name
     * @return The newly created user
     * @throws IllegalArgumentException if email already exists
     */
    User register(String email, String password, String firstName, String lastName);

    /**
     * Logs out a user.
     *
     * @param userId User ID to log out
     */
    void logout(String userId);

    /**
     * Changes a user's password.
     *
     * @param userId User ID
     * @param oldPassword Current password
     * @param newPassword New password
     * @return true if password was changed successfully
     */
    boolean changePassword(String userId, String oldPassword, String newPassword);

    /**
     * Validates if a user session is active.
     *
     * @param userId User ID to validate
     * @return true if session is active
     */
    boolean isSessionActive(String userId);
}

