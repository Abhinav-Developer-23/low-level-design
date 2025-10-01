package org.example.model;

import org.example.enums.ProductType;
import org.example.interfaces.Dispensable;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a product in the vending machine
 * Implements Dispensable interface following Interface Segregation Principle
 */
public class Product implements Dispensable {

    private final String productId;
    private final String name;
    private final ProductType type;
    private final double price;
    private final AtomicInteger quantity;
    private final LocalDateTime expirationDate;

    public Product(String productId, String name, ProductType type, double price, int initialQuantity) {
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = new AtomicInteger(initialQuantity);
        this.expirationDate = LocalDateTime.now().plusDays(30); // Default 30 days expiration
    }

    public Product(String productId, String name, ProductType type, double price, int initialQuantity, LocalDateTime expirationDate) {
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = new AtomicInteger(initialQuantity);
        this.expirationDate = expirationDate;
    }

    @Override
    public boolean dispense() {
        int currentQuantity = quantity.get();
        if (currentQuantity > 0 && isAvailable()) {
            return quantity.compareAndSet(currentQuantity, currentQuantity - 1);
        }
        return false;
    }

    @Override
    public boolean isAvailable() {
        return quantity.get() > 0 && !isExpired();
    }

    @Override
    public String getName() {
        return name;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    public void restock(int amount) {
        quantity.addAndGet(amount);
    }

    // Getters
    public String getProductId() {
        return productId;
    }

    public ProductType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String toString() {
        return String.format("Product{id='%s', name='%s', type=%s, price=%.2f, quantity=%d, available=%s}",
                productId, name, type, price, quantity.get(), isAvailable());
    }
}
