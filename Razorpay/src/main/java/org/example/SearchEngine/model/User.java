package org.example.SearchEngine.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Represents a user in the search engine system.
 * Users can own datasets and have read access to other users' datasets.
 */
@Getter
@EqualsAndHashCode(of = "userId")
public class User {
    private final String userId;
    private final String name;
    private final String email;

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
