package org.example.SearchEngine.service;

import org.example.SearchEngine.model.User;
import org.example.SearchEngine.repository.UserRepository;

/**
 * Implementation of UserService.
 * Handles business logic for user management.
 */
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl() {
        this.userRepository = UserRepository.getInstance();
    }

    @Override
    public User registerUser(String userId, String name, String email) {
        User user = new User(userId, name, email);
        userRepository.createUser(user);
        return user;
    }

    @Override
    public User getUser(String userId) {
        return userRepository.getById(userId);
    }

    @Override
    public boolean userExists(String userId) {
        return userRepository.exists(userId);
    }
}
