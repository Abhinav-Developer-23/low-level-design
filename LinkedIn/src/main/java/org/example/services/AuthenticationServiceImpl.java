package org.example.services;

import org.example.interfaces.AuthenticationService;
import org.example.model.Profile;
import org.example.model.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of authentication service.
 * Handles user registration, login, and session management.
 */
public class AuthenticationServiceImpl implements AuthenticationService {
    private final Map<String, User> usersByEmail;
    private final Map<String, User> usersById;
    private final Set<String> activeSessions;

    public AuthenticationServiceImpl() {
        this.usersByEmail = new HashMap<>();
        this.usersById = new HashMap<>();
        this.activeSessions = new HashSet<>();
    }

    @Override
    public User authenticate(String email, String password) {
        User user = usersByEmail.get(email.toLowerCase());
        
        if (user == null) {
            return null;
        }

        // In production, use proper password hashing (BCrypt, Argon2, etc.)
        String passwordHash = hashPassword(password);
        
        if (user.getPasswordHash().equals(passwordHash)) {
            user.setLastLoginAt(LocalDateTime.now());
            activeSessions.add(user.getUserId());
            return user;
        }
        
        return null;
    }

    @Override
    public User register(String email, String password, String firstName, String lastName) {
        String emailLower = email.toLowerCase();
        
        if (usersByEmail.containsKey(emailLower)) {
            throw new IllegalArgumentException("Email already registered: " + email);
        }

        // Validate password strength
        validatePassword(password);

        String passwordHash = hashPassword(password);
        
        // Create profile
        Profile profile = new Profile(firstName, lastName);
        
        // Create user
        User user = new User.Builder(emailLower, passwordHash)
                .profile(profile)
                .build();

        // Store user
        usersByEmail.put(emailLower, user);
        usersById.put(user.getUserId(), user);

        return user;
    }

    @Override
    public void logout(String userId) {
        activeSessions.remove(userId);
    }

    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = usersById.get(userId);
        
        if (user == null) {
            return false;
        }

        String oldPasswordHash = hashPassword(oldPassword);
        
        if (!user.getPasswordHash().equals(oldPasswordHash)) {
            return false;
        }

        validatePassword(newPassword);
        String newPasswordHash = hashPassword(newPassword);
        user.setPasswordHash(newPasswordHash);
        
        return true;
    }

    @Override
    public boolean isSessionActive(String userId) {
        return activeSessions.contains(userId);
    }

    // Helper methods

    /**
     * Gets a user by their ID.
     */
    public User getUserById(String userId) {
        return usersById.get(userId);
    }

    /**
     * Gets a user by their email.
     */
    public User getUserByEmail(String email) {
        return usersByEmail.get(email.toLowerCase());
    }

    /**
     * Validates password strength.
     */
    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        // Add more validation rules as needed
    }

    /**
     * Hashes a password.
     * In production, use BCrypt, Argon2, or similar secure hashing algorithms.
     */
    private String hashPassword(String password) {
        // Simple hash for demonstration - DO NOT use in production!
        // Use BCrypt, Argon2, or PBKDF2 in real applications
        return Integer.toString(password.hashCode());
    }
}

