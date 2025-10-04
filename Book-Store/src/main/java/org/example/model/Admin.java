package org.example.model;

public class Admin extends User {

    public Admin(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public void showDashboard() {
        System.out.println("Admin Dashboard for: " + name);
    }
}

