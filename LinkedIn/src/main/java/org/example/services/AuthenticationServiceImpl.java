package org.example.services;

import org.example.interfaces.AuthenticationService;
import org.example.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of AuthenticationService
 * Handles user registration and authentication
 * Thread-safe using ConcurrentHashMap
 */
public class AuthenticationServiceImpl implements AuthenticationService {
    private final Map<String, User> usersByEmail;
    private final Map<String, User> usersById;

    public AuthenticationServiceImpl() {
        this.usersByEmail = new ConcurrentHashMap<>();
        this.usersById = new ConcurrentHashMap<>();
    }

    @Override
    public User register(String email, String password, String firstName, String lastName) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (emailExists(email)) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // In production, hash the password (e.g., using BCrypt)
        String passwordHash = hashPassword(password);

        User user = new User.Builder(email, passwordHash)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        usersByEmail.put(email.toLowerCase(), user);
        usersById.put(user.getUserId(), user);

        return user;
    }

    @Override
    public User authenticate(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password cannot be null");
        }

        User user = usersByEmail.get(email.toLowerCase());
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String passwordHash = hashPassword(password);
        if (!user.getPasswordHash().equals(passwordHash)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

    @Override
    public User getUserById(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return usersById.get(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return usersByEmail.get(email.toLowerCase());
    }

    @Override
    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        String oldPasswordHash = hashPassword(oldPassword);
        if (!user.getPasswordHash().equals(oldPasswordHash)) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        // In production, you'd need to create a new User object with the new password
        // Since passwordHash is final in our design, this would require refactoring
        // For now, we'll return false as the operation cannot be completed
        return false;
    }

    @Override
    public boolean emailExists(String email) {
        return usersByEmail.containsKey(email.toLowerCase());
    }

    /**
     * Simple password hashing (in production, use BCrypt or similar)
     */
    private String hashPassword(String password) {
        // Simple hash for demo purposes
        // In production, use: BCrypt.hashpw(password, BCrypt.gensalt())
        return String.valueOf(password.hashCode());
    }
}

