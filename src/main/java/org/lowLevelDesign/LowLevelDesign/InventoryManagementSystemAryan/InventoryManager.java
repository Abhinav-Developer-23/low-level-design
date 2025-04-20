package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
  // Singleton instance
  private static InventoryManager instance;

  // System components
  private List<Warehouse> warehouses;
  private ProductFactory productFactory;
  private List<InventoryObserver> observers;
  private ReplenishmentStrategy replenishmentStrategy;

  // Private constructor to prevent instantiation from outside
  private InventoryManager() {
    // Initialize collections and dependencies
    warehouses = new ArrayList<>();
    observers = new ArrayList<>();
    productFactory = new ProductFactory();
  }

  // Static method to get the singleton instance with thread safety
  public static synchronized InventoryManager getInstance() {
    if (instance == null) {
      instance = new InventoryManager();
    }
    return instance;
  }

  // Strategy pattern method
  public void setReplenishmentStrategy(ReplenishmentStrategy strategy) {
    this.replenishmentStrategy = strategy;
  }

  // Observer pattern methods
  public void addObserver(InventoryObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(InventoryObserver observer) {
    observers.remove(observer);
  }

  public void notifyObservers(Product product) {
    for (InventoryObserver observer : observers) {
      observer.update(product);
    }
  }
  
  // Warehouse management
  public void addWarehouse(Warehouse warehouse) {
    warehouses.add(warehouse);
  }

  public void removeWarehouse(Warehouse warehouse) {
    warehouses.remove(warehouse);
  }

  // Product inventory operations
  public Product getProductBySku(String sku) {
    for (Warehouse warehouse : warehouses) {
      Product product = warehouse.getProductBySku(sku);
      if (product != null) {
        return product;
      }
    }
    return null;
  }
  
  // Total inventory count across all warehouses for a product
  public int getTotalInventory(String sku) {
    int total = 0;
    for (Warehouse warehouse : warehouses) {
      total += warehouse.getAvailableQuantity(sku);
    }
    return total;
  }
  
  // Transfer product between warehouses
  public boolean transferProduct(String sku, Warehouse source, Warehouse destination, int quantity) {
    // Check if source has enough inventory
    if (source.getAvailableQuantity(sku) >= quantity) {
      // Remove from source
      if (source.removeProduct(sku, quantity)) {
        // Get the product details
        Product product = getProductBySku(sku);
        if (product != null) {
          // Add to destination
          destination.addProduct(product, quantity);
          System.out.println("Transferred " + quantity + " units of product SKU: " + sku +
                            " from " + source.getName() + " to " + destination.getName());
          return true;
        }
      }
    }
    System.out.println("Transfer failed for product SKU: " + sku);
    return false;
  }

  // Check stock levels and apply replenishment strategy if needed
  public void checkAndReplenish(String sku) {
    Product product = getProductBySku(sku);
    if (product != null) {
      // If product is below threshold, notify observers
      if (product.getQuantity() < product.getThreshold()) {
        notifyObservers(product);
        // Apply current replenishment strategy
        if (replenishmentStrategy != null) {
          replenishmentStrategy.replenish(product);
        }
      }
    }
  }

  // Global inventory check
  public void performInventoryCheck() {
    for (Warehouse warehouse : warehouses) {
      for (Product product : warehouse.getAllProducts()) {
        if (product.getQuantity() < product.getThreshold()) {
          notifyObservers(product);
          if (replenishmentStrategy != null) {
            replenishmentStrategy.replenish(product);
          }
        }
      }
    }
  }
  
  // Create product through factory
  public Product createProduct(ProductCategory category, String sku, String name, double price, int quantity) {
    return productFactory.createProduct(category, sku, name, price, quantity);
  }
} 