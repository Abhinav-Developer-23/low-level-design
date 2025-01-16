package org.lowLevelDesign.DesignPatterns.MediatorPattern;

import java.util.ArrayList;
import java.util.List;

// Concrete Mediator
public class ChatRoomMediator implements ChatMediator {
    private final List<User> users;

    public ChatRoomMediator() {
        this.users = new ArrayList<>();
    }

    @Override
    public void sendMessage(String message, User user) {
        for (User u : users) {
            // Message should not be received by the user sending it
            if (u != user) {
                u.receive(message);
            }
        }
    }

    @Override
    public void addUser(User user) {
        this.users.add(user);
    }
}

