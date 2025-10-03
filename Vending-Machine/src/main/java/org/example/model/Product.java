package org.example.model;

import org.example.enums.ProductType;
import org.example.interfaces.Dispensable;

import java.util.Objects;

/**
 * Immutable Product class representing a vending machine product
 */
public class Product implements Dispensable {
    private final String productId;
    private final String name;
    private final double price;
    private final ProductType type;
    private final int calories;

    public Product(String productId, String name, double price, ProductType type, int calories) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.type = type;
        this.calories = calories;
    }

    @Override
    public String getId() {
        return productId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean isAvailable() {
        // Availability is checked through Inventory
        return true;
    }

    public String getProductId() {
        return productId;
    }

    public ProductType getType() {
        return type;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public String toString() {
        return String.format("%s - %s ($%.2f) [%s, %d cal]",
                productId, name, price, type.getDisplayName(), calories);
    }
}

