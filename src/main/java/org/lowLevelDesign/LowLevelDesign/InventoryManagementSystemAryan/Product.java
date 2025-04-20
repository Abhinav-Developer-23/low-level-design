package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

// Product class modified to work with Builder pattern
public abstract class Product {
  // Core attributes
  private final String sku;
  private final String name;
  private final double price;
  private int quantity; // optional
  private final int threshold; // optional
  private final ProductCategory category;

  // Protected constructor to be used by concrete builders
  protected Product(Builder<?> builder) {
    this.sku = builder.sku;
    this.name = builder.name;
    this.price = builder.price;
    this.quantity = builder.quantity;
    this.threshold = builder.threshold;
    this.category = builder.category;
  }

  // Getters
  public String getSku() {
    return sku;
  }
  public String getName() {
    return name;
  }
  public double getPrice() {
    return price;
  }
  public int getQuantity() {
    return quantity;
  }
  public int getThreshold() {
    return threshold;
  }
  public ProductCategory getCategory() {
    return category;
  }

  // Setters only for mutable properties
  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
  
  // Methods to modify stock
  public void addStock(int amount) {
    this.quantity += amount;
  }
  
  public void removeStock(int amount) {
    if (amount <= this.quantity) {
      this.quantity -= amount;
    } else {
      throw new IllegalArgumentException("Cannot remove more stock than available");
    }
  }

  // Abstract Builder class
  public static abstract class Builder<T extends Builder<T>> {
    // Required parameters
    private final String sku;
    private final String name;
    private final double price;
    private final ProductCategory category;

    // Optional parameters with default values
    private int quantity = 0;
    private int threshold = 10;

    // Constructor with required parameters
    public Builder(
        String sku, String name, double price, ProductCategory category) {
      this.sku = sku;
      this.name = name;
      this.price = price;
      this.category = category;
    }

    // Methods to set optional parameters
    public T quantity(int quantity) {
      this.quantity = quantity;
      return self();
    }

    public T threshold(int threshold) {
      this.threshold = threshold;
      return self();
    }

    // Method to be overridden by subclasses to return this (the current object)
    protected abstract T self();

    // Build method to be implemented by concrete builders
    public abstract Product build();
  }
} 