package org.example.PhotoSharingApp.service;

import org.example.PhotoSharingApp.exception.UserNotFoundException;
import org.example.PhotoSharingApp.model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Service for managing users.
 * Single Responsibility: Handles all user-related operations.
 */
public class UserService {
    private final Map<String, User> users;
    
    public UserService() {
        this.users = new HashMap<>();
    }
    
    /**
     * Registers a new user in the system.
     * 
     * @param user the user to register
     */
    public void registerUser(User user) {
        if (users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("User already exists: " + user.getUserId());
        }
        users.put(user.getUserId(), user);
        System.out.println("User registered: " + user.getUsername());
    }
    
    /**
     * Retrieves a user by ID.
     * 
     * @param userId the user ID
     * @return the user
     * @throws UserNotFoundException if user doesn't exist
     */
    public User getUser(String userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found: " + userId);
        }
        return user;
    }
    
    /**
     * Makes one user follow another.
     * 
     * @param followerId the ID of the user who wants to follow
     * @param followeeId the ID of the user to be followed
     */
    public void followUser(String followerId, String followeeId) {
        if (followerId.equals(followeeId)) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }
        
        User follower = getUser(followerId);
        User followee = getUser(followeeId);
        
        follower.follow(followeeId);
        followee.addFollower(followerId);
        
        System.out.println(follower.getUsername() + " is now following " + followee.getUsername());
    }
    
    /**
     * Makes one user unfollow another.
     * 
     * @param followerId the ID of the user who wants to unfollow
     * @param followeeId the ID of the user to be unfollowed
     */
    public void unfollowUser(String followerId, String followeeId) {
        User follower = getUser(followerId);
        User followee = getUser(followeeId);
        
        follower.unfollow(followeeId);
        followee.removeFollower(followerId);
        
        System.out.println(follower.getUsername() + " unfollowed " + followee.getUsername());
    }
    
    /**
     * Gets all users in the system.
     * 
     * @return map of user IDs to users
     */
    public Map<String, User> getAllUsers() {
        return new HashMap<>(users);
    }
}




