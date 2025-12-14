package org.example.SearchEngine.repository;

import org.example.SearchEngine.exception.UserAlreadyExistsException;
import org.example.SearchEngine.exception.UserNotFoundException;
import org.example.SearchEngine.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Singleton repository for managing users in memory.
 * Thread-safe implementation using synchronized methods.
 */
public class UserRepository {
    private static UserRepository instance;
    private final Map<String, User> users; // userId -> User

    /**
     * Private constructor to prevent direct instantiation.
     */
    private UserRepository() {
        this.users = new HashMap<>();
    }

    /**
     * Get the singleton instance of UserRepository.
     * @return The singleton instance
     */
    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    /**
     * Create a new user.
     * @param user The user to create
     * @throws UserAlreadyExistsException if user already exists
     */
    public synchronized void createUser(User user) {
        if (users.containsKey(user.getUserId())) {
            throw new UserAlreadyExistsException("User with ID " + user.getUserId() + " already exists");
        }
        users.put(user.getUserId(), user);
    }

    /**
     * Find a user by ID.
     * @param userId The user ID
     * @return Optional containing the user if found
     */
    public synchronized Optional<User> findById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    /**
     * Get a user by ID or throw exception if not found.
     * @param userId The user ID
     * @return The user
     * @throws UserNotFoundException if user not found
     */
    public synchronized User getById(String userId) {
        return findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found"));
    }

    /**
     * Check if a user exists.
     * @param userId The user ID
     * @return true if user exists
     */
    public synchronized boolean exists(String userId) {
        return users.containsKey(userId);
    }

    /**
     * Delete a user.
     * @param userId The user ID
     * @return The deleted user
     * @throws UserNotFoundException if user not found
     */
    public synchronized User deleteUser(String userId) {
        User user = users.remove(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found");
        }
        return user;
    }

    /**
     * Get all users.
     * @return Map of all users
     */
    public synchronized Map<String, User> getAllUsers() {
        return new HashMap<>(users);
    }

    /**
     * Clear all users (useful for testing).
     */
    public synchronized void clear() {
        users.clear();
    }
}
