package org.lowLevelDesign.DesignPatterns.MediatorPattern;

import java.util.ArrayList;
import java.util.List;

class MediatorPatternExample {

    // Main method to demonstrate the pattern
    public static void main(String[] args) {
        ChatMediator chatMediator = new ChatRoomMediator();

        User user1 = new ChatUser(chatMediator, "Alice");
        User user2 = new ChatUser(chatMediator, "Bob");
        User user3 = new ChatUser(chatMediator, "Charlie");
        User user4 = new ChatUser(chatMediator, "Diana");

        chatMediator.addUser(user1);
        chatMediator.addUser(user2);
        chatMediator.addUser(user3);
        chatMediator.addUser(user4);

        user1.send("Hi everyone!");
        user2.send("Hello Alice!");
    }

    // Mediator interface
    interface ChatMediator {
        void sendMessage(String message, User user);

        void addUser(User user);
    }

    // Concrete Mediator
    static class ChatRoomMediator implements ChatMediator {
        private List<User> users;

        public ChatRoomMediator() {
            this.users = new ArrayList<>();
        }

        @Override
        public void sendMessage(String message, User user) {
            for (User u : users) {
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

    // Abstract User class
    abstract static class User {
        protected ChatMediator mediator;
        protected String name;

        public User(ChatMediator mediator, String name) {
            this.mediator = mediator;
            this.name = name;
        }

        public abstract void send(String message);

        public abstract void receive(String message);
    }

    // Concrete User class
    static class ChatUser extends User {
        public ChatUser(ChatMediator mediator, String name) {
            super(mediator, name);
        }

        @Override
        public void send(String message) {
            System.out.println(this.name + " sends: " + message);
            mediator.sendMessage(message, this);
        }

        @Override
        public void receive(String message) {
            System.out.println(this.name + " receives: " + message);
        }
    }
}
