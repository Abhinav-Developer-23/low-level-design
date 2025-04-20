package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

import java.util.Date;

// Product Factory class implementing Factory Pattern
public class ProductFactory {
  public Product createProduct(ProductCategory category, String sku, 
      String name, double price, int quantity) {
    switch (category) {
      case ELECTRONICS:
        return new ElectronicsProduct.ElectronicsBuilder(sku, name, price, "Generic")
            .quantity(quantity)
            .build();
            
      case CLOTHING:
        return new ClothingProduct.ClothingBuilder(sku, name, price, "M", "Black")
            .quantity(quantity)
            .build();
            
      case GROCERY:
        // Default expiry date set to 30 days from now
        Date expiryDate = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);
        return new GroceryProduct.GroceryBuilder(sku, name, price, expiryDate)
            .quantity(quantity)
            .build();
            
      case FURNITURE:
        return new FurnitureProduct.FurnitureBuilder(sku, name, price, "Wood", "Standard")
            .quantity(quantity)
            .build();
            
      default:
        throw new IllegalArgumentException("Unsupported product category: " + category);
    }
  }
  
  // Overloaded methods to create products with more specific details
  
  public ElectronicsProduct createElectronicsProduct(String sku, String name, double price, 
      int quantity, String brand, int warrantyPeriod) {
    return new ElectronicsProduct.ElectronicsBuilder(sku, name, price, brand)
        .quantity(quantity)
        .warrantyPeriod(warrantyPeriod)
        .build();
  }
  
  public ClothingProduct createClothingProduct(String sku, String name, double price, 
      int quantity, String size, String color, String material) {
    return new ClothingProduct.ClothingBuilder(sku, name, price, size, color)
        .quantity(quantity)
        .material(material)
        .build();
  }
  
  public GroceryProduct createGroceryProduct(String sku, String name, double price, 
      int quantity, Date expiryDate, boolean refrigerated) {
    return new GroceryProduct.GroceryBuilder(sku, name, price, expiryDate)
        .quantity(quantity)
        .refrigerated(refrigerated)
        .build();
  }
  
  public FurnitureProduct createFurnitureProduct(String sku, String name, double price, 
      int quantity, String material, String dimensions, String brand) {
    return new FurnitureProduct.FurnitureBuilder(sku, name, price, material, dimensions)
        .quantity(quantity)
        .brand(brand)
        .build();
  }
} 