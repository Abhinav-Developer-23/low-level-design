package org.lowLevelDesign.DesignPatterns.MediatorPattern;

public class Main {
    public static void main(String[] args) {
        // Create mediator
        ChatMediator chatMediator = new ChatRoomMediator();

        // Create users
        User user1 = new ChatUser(chatMediator, "Alice");
        User user2 = new ChatUser(chatMediator, "Bob");
        User user3 = new ChatUser(chatMediator, "Charlie");
        User user4 = new ChatUser(chatMediator, "Diana");

        // Add users to the chat room
        chatMediator.addUser(user1);
        chatMediator.addUser(user2);
        chatMediator.addUser(user3);
        chatMediator.addUser(user4);

        // Users send messages
        user1.send("Hi everyone!");
        user2.send("Hello Alice!");
    }
}

