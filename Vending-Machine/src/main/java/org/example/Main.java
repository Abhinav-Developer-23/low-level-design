package org.example;

import org.example.enums.ProductType;
import org.example.observers.ConsoleVendingObserver;
import org.example.observers.MaintenanceObserver;
import org.example.strategies.selection.NameBasedSelectionStrategy;
import org.example.system.VendingMachineSystem;

/**
 * Main class demonstrating Vending Machine with State Pattern
 * 
 * Design Patterns Used:
 * 1. State Pattern ⭐ - Machine behavior changes based on state
 * 2. Singleton Pattern - Single machine instance
 * 3. Strategy Pattern - Payment and selection strategies
 * 4. Observer Pattern - Event notifications
 * 
 * States: Idle → Selecting → Payment → Dispensing → Idle
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🤖 VENDING MACHINE SYSTEM - STATE PATTERN DEMONSTRATION");
        System.out.println("=".repeat(80) + "\n");

        // Get singleton instance
        VendingMachineSystem machine = VendingMachineSystem.getInstance();

        // Register observers (Observer Pattern)
        ConsoleVendingObserver consoleObserver = new ConsoleVendingObserver("Console");
        MaintenanceObserver maintenanceObserver = new MaintenanceObserver();
        
        machine.registerObserver(consoleObserver);
        machine.registerObserver(maintenanceObserver);

        // Initialize inventory
        System.out.println("\n📦 Initializing Inventory...\n");
        machine.addProduct("A1", "Coca Cola", 1.50, ProductType.BEVERAGE, 140, 5);
        machine.addProduct("A2", "Pepsi", 1.50, ProductType.BEVERAGE, 150, 4);
        machine.addProduct("B1", "Lays Chips", 2.00, ProductType.SNACK, 160, 3);
        machine.addProduct("B2", "Doritos", 2.25, ProductType.SNACK, 150, 6);
        machine.addProduct("C1", "Snickers", 1.75, ProductType.CANDY, 215, 2);
        machine.addProduct("C2", "KitKat", 1.50, ProductType.CANDY, 210, 8);

        // Display initial inventory
        machine.displayInventory();

        // Scenario 1: Successful cash purchase
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 1: Cash Purchase (Exact Amount)");
        System.out.println("=".repeat(80) + "\n");
        
        machine.displayStatus();
        machine.selectProduct("A1");  // Coca Cola - $1.50
        machine.insertCoin(0.50);
        machine.insertCoin(0.50);
        machine.insertCoin(0.50);  // Total: $1.50 - Auto dispenses
        
        waitForUser();

        // Scenario 2: Cash purchase with change
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 2: Cash Purchase (With Change)");
        System.out.println("=".repeat(80) + "\n");
        
        machine.selectProduct("B1");  // Lays Chips - $2.00
        machine.insertCoin(1.00);
        machine.insertCoin(1.00);
        machine.insertCoin(0.50);  // Total: $2.50 - Returns $0.50 change
        
        waitForUser();

        // Scenario 3: Card payment
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 3: Card Payment");
        System.out.println("=".repeat(80) + "\n");
        
        machine.selectProduct("C1");  // Snickers - $1.75
        machine.insertCard("1234-5678-9012-3456", 2.00);  // Card payment with $0.25 change
        
        waitForUser();

        // Scenario 4: Mobile payment
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 4: Mobile Payment");
        System.out.println("=".repeat(80) + "\n");
        
        machine.selectProduct("B2");  // Doritos - $2.25
        machine.insertMobilePayment("MOBILE-PAY-12345", 2.25);  // Exact mobile payment
        
        waitForUser();

        // Scenario 5: Transaction cancellation
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 5: Transaction Cancellation");
        System.out.println("=".repeat(80) + "\n");
        
        machine.selectProduct("A2");  // Pepsi - $1.50
        machine.insertCoin(0.50);
        machine.insertCoin(0.25);  // Partial payment
        System.out.println("\n💭 Customer decides to cancel...\n");
        machine.cancelTransaction();  // Refunds $0.75
        
        waitForUser();

        // Scenario 6: Out of stock
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 6: Out of Stock Product");
        System.out.println("=".repeat(80) + "\n");
        
        // Try to buy the last Snickers
        machine.selectProduct("C1");  // Snickers - only 1 left
        machine.insertCard("1234-5678-9012-3456", 1.75);
        
        // Try to buy another Snickers (out of stock)
        System.out.println("\n💭 Another customer tries to buy Snickers...\n");
        machine.selectProduct("C1");  // Should show out of stock
        
        waitForUser();

        // Scenario 7: Low stock alert
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 7: Low Stock Alert");
        System.out.println("=".repeat(80) + "\n");
        
        machine.selectProduct("B1");  // Lays Chips - low stock
        machine.insertCoin(2.00);
        
        waitForUser();

        // Scenario 8: Invalid operations in different states
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 8: Invalid Operations (State Pattern Demo)");
        System.out.println("=".repeat(80) + "\n");
        
        System.out.println("💭 Testing invalid operations in IDLE state:\n");
        machine.insertCoin(1.00);  // Invalid in IDLE
        machine.cancelTransaction();  // Invalid in IDLE
        
        System.out.println("\n💭 Testing invalid operations in SELECTING state:\n");
        machine.selectProduct("C2");  // Select KitKat
        machine.selectProduct("A1");  // Try to select another (invalid)
        machine.cancelTransaction();  // Valid - cancels and goes back to IDLE
        
        waitForUser();

        // Scenario 9: Name-based selection strategy
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 9: Strategy Pattern - Name-Based Selection");
        System.out.println("=".repeat(80) + "\n");
        
        machine.setSelectionStrategy(new NameBasedSelectionStrategy());
        machine.selectProduct("kitkat");  // Select by name (case-insensitive)
        machine.insertCoin(1.50);
        
        waitForUser();

        // Scenario 10: Rapid transactions
        System.out.println("\n" + "=".repeat(80));
        System.out.println("SCENARIO 10: Multiple Rapid Transactions");
        System.out.println("=".repeat(80) + "\n");
        
        machine.selectProduct("A1");
        machine.insertCoin(1.50);
        
        machine.selectProduct("A2");
        machine.insertCard("9999-8888-7777-6666", 1.50);
        
        machine.selectProduct("B2");
        machine.insertMobilePayment("MOBILE-FAST", 2.25);
        
        waitForUser();

        // Display final inventory
        System.out.println("\n" + "=".repeat(80));
        System.out.println("FINAL INVENTORY STATUS");
        System.out.println("=".repeat(80));
        machine.displayInventory();

        // Display maintenance report
        System.out.println(maintenanceObserver.getStatistics());

        // Summary
        System.out.println("\n" + "=".repeat(80));
        System.out.println("✅ DEMONSTRATION COMPLETE");
        System.out.println("=".repeat(80));
        System.out.println("\nDesign Patterns Demonstrated:");
        System.out.println("  ✓ State Pattern - Clean state transitions (Idle → Selecting → Payment → Dispensing)");
        System.out.println("  ✓ Singleton Pattern - Single system instance with thread-safe initialization");
        System.out.println("  ✓ Strategy Pattern - Multiple payment methods (Cash, Card, Mobile)");
        System.out.println("  ✓ Observer Pattern - Real-time event notifications and monitoring");
        System.out.println("\nSOLID Principles:");
        System.out.println("  ✓ Single Responsibility - Each class has one clear purpose");
        System.out.println("  ✓ Open/Closed - Easy to add new states, strategies, observers");
        System.out.println("  ✓ Liskov Substitution - All implementations are interchangeable");
        System.out.println("  ✓ Interface Segregation - Small, focused interfaces");
        System.out.println("  ✓ Dependency Inversion - Depend on abstractions, not concretions");
        System.out.println("\nKey Features:");
        System.out.println("  ✓ Thread-safe implementation");
        System.out.println("  ✓ Automatic state transitions");
        System.out.println("  ✓ Inventory management with low stock alerts");
        System.out.println("  ✓ Multiple payment methods");
        System.out.println("  ✓ Transaction tracking and reporting");
        System.out.println("=".repeat(80) + "\n");
    }

    private static void waitForUser() {
        try {
            Thread.sleep(1000);  // Small pause between scenarios
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

