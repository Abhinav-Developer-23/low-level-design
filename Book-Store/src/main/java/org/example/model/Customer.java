package org.example.model;

public class Customer extends User {
    private ShoppingCart cart;

    public Customer(String id, String name, String email) {
        super(id, name, email);
        this.cart = new ShoppingCart();
    }

    public ShoppingCart getCart() {
        return cart;
    }

    @Override
    public void showDashboard() {
        System.out.println("Customer Dashboard for: " + name);
    }
}

