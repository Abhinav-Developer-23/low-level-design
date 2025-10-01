package org.example.model;

import java.time.LocalDateTime;

/**
 * Abstract base class for vending machine items
 * Template Method Pattern: Provides common functionality for all vending items
 */
public abstract class VendingItem {

    protected final String id;
    protected final String name;
    protected final double price;
    protected final LocalDateTime createdAt;
    protected LocalDateTime updatedAt;

    protected VendingItem(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    /**
     * Template method for validating item availability
     */
    public final boolean isValidForPurchase() {
        return isInStock() && !isExpired() && isPriceValid();
    }

    /**
     * Hook method: Check if item is in stock
     */
    protected abstract boolean isInStock();

    /**
     * Hook method: Check if item is expired
     */
    protected abstract boolean isExpired();

    /**
     * Hook method: Validate price
     */
    protected boolean isPriceValid() {
        return price > 0;
    }

    /**
     * Abstract method: Get current quantity
     */
    public abstract int getQuantity();

    /**
     * Abstract method: Process dispensing
     */
    public abstract boolean dispense();

    // Common getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return String.format("%s{id='%s', name='%s', price=%.2f, inStock=%s}",
                getClass().getSimpleName(), id, name, price, isInStock());
    }
}
