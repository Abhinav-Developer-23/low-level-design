package org.lowLevelDesign.LowLevelDesign.InventoryManagementSystemAryan;

import java.util.Arrays;
import java.util.Date;

public class Main {
  public static void main(String[] args) {
    // Get the singleton instance of InventoryManager
    InventoryManager inventoryManager = InventoryManager.getInstance();

    // Create and add warehouses
    Warehouse warehouse1 = new Warehouse(1, "Warehouse 1", "New York");
    Warehouse warehouse2 = new Warehouse(2, "Warehouse 2", "Los Angeles");
    inventoryManager.addWarehouse(warehouse1);
    inventoryManager.addWarehouse(warehouse2);

    // Add observers for inventory monitoring
    inventoryManager.addObserver(new SupplierNotifier("ElectroSupplier", "contact@electrosupplier.com"));
    inventoryManager.addObserver(new DashboardAlertSystem("MEDIUM", Arrays.asList("admin1", "admin2")));

    // Create products using ProductFactory with Builder pattern
    ProductFactory productFactory = new ProductFactory();
    
    // Electronics product with the builder pattern
    ElectronicsProduct laptop = new ElectronicsProduct.ElectronicsBuilder(
        "E-123", "Premium Laptop", 1299.99, "TechBrand")
        .quantity(50)
        .threshold(10)
        .warrantyPeriod(24)
        .wirelessConnectivity(true)
        .modelNumber("TBX2000")
        .build();
    
    // Clothing product with the builder pattern
    ClothingProduct tshirt = new ClothingProduct.ClothingBuilder(
        "C-456", "Cotton T-Shirt", 19.99, "L", "Blue")
        .quantity(200)
        .threshold(30)
        .material("100% Cotton")
        .brand("FashionBrand")
        .season("Summer")
        .build();
    
    // Grocery product with the builder pattern
    GroceryProduct apple = new GroceryProduct.GroceryBuilder(
        "G-789", "Organic Apples", 3.99, new Date(System.currentTimeMillis() + 14L * 24 * 60 * 60 * 1000))
        .quantity(100)
        .threshold(20)
        .refrigerated(true)
        .organic(true)
        .origin("Washington")
        .build();

    // Furniture product with the builder pattern
    FurnitureProduct desk = new FurnitureProduct.FurnitureBuilder(
        "F-101", "Office Desk", 249.99, "Oak Wood", "60x30x29 inches")
        .quantity(15)
        .threshold(5)
        .brand("FurnitureCo")
        .requiresAssembly(true)
        .warrantyPeriod(36)
        .build();

    // Add products to warehouses
    warehouse1.addProduct(laptop);
    warehouse1.addProduct(tshirt);
    warehouse2.addProduct(apple);
    warehouse2.addProduct(desk);

    // Test inventory operations
    System.out.println("\n===== Testing Inventory Operations =====");
    // Check stock levels
    System.out.println("Laptop stock in Warehouse 1: " + warehouse1.getAvailableQuantity("E-123"));
    System.out.println("Total apple stock across all warehouses: " + inventoryManager.getTotalInventory("G-789"));
    
    // Remove some stock
    System.out.println("\n===== Testing Stock Removal =====");
    warehouse1.removeProduct("E-123", 5);
    
    // Transfer products between warehouses
    System.out.println("\n===== Testing Product Transfer =====");
    inventoryManager.transferProduct("E-123", warehouse1, warehouse2, 10);
    
    // Set replenishment strategy to Just-In-Time
    System.out.println("\n===== Testing Just-In-Time Replenishment =====");
    inventoryManager.setReplenishmentStrategy(new JustInTimeStrategy());
    
    // Create a low-stock situation to trigger replenishment
    warehouse1.removeProduct("E-123", 30);
    
    // Check and replenish specific product
    inventoryManager.checkAndReplenish("E-123");

    // Switch replenishment strategy to Bulk Order
    System.out.println("\n===== Testing Bulk Order Replenishment =====");
    inventoryManager.setReplenishmentStrategy(new BulkOrderStrategy(3));
    
    // Perform global inventory check with the new strategy
    inventoryManager.performInventoryCheck();
  }
} 