package org.example.quickcommerce.model;

import org.example.quickcommerce.enums.ProductCategory;

import java.util.UUID;

/**
 * Represents a product in the quick commerce system.
 */
public class Product {
    private final String productId;
    private String name;
    private double price;
    private ProductCategory category;
    private String unit;
    private String description;

    public Product(String name, double price, ProductCategory category, String unit) {
        this.productId = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.category = category;
        this.unit = unit;
    }

    public Product(String name, double price, ProductCategory category, String unit, String description) {
        this(name, price, category, unit);
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public String getUnit() {
        return unit;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{productId='" + productId + "', name='" + name + "', price=" + price + ", unit='" + unit + "'}";
    }
}

