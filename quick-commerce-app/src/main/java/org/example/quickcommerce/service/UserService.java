package org.example.quickcommerce.service;

import org.example.quickcommerce.model.Address;
import org.example.quickcommerce.model.User;
import org.example.quickcommerce.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing users and their addresses.
 * Uses Repository singleton for data storage.
 */
public class UserService {

    private ConcurrentHashMap<String, User> getUserDb() {
        return Repository.getInstance().getUserDb();
    }

    public User registerUser(String name, String email, String phone) {
        User user = new User(name, email, phone);
        getUserDb().put(user.getUserId(), user);
        return user;
    }

    public Optional<User> getUser(String userId) {
        return Optional.ofNullable(getUserDb().get(userId));
    }

    public User getUserOrThrow(String userId) {
        return getUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(getUserDb().values());
    }

    public void login(String userId) {
        getUserOrThrow(userId).login();
    }

    public void logout(String userId) {
        getUserOrThrow(userId).logout();
    }

    public boolean isLoggedIn(String userId) {
        return getUser(userId).map(User::isLoggedIn).orElse(false);
    }

    public void addAddress(String userId, Address address) {
        getUserOrThrow(userId).addAddress(address);
    }

    public void removeAddress(String userId, String addressId) {
        getUserOrThrow(userId).removeAddress(addressId);
    }

    public void selectAddress(String userId, String addressId) {
        getUserOrThrow(userId).selectAddress(addressId);
    }

    public Address getSelectedAddress(String userId) {
        return getUserOrThrow(userId).getSelectedAddress();
    }
}

