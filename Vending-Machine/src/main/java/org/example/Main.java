package org.example;

import org.example.enums.CoinType;
import org.example.enums.PaymentMethod;
import org.example.system.VendingMachineSystem;

/**
 * Main class demonstrating the Vending Machine Low Level Design implementation.
 * Showcases various features including State Pattern, Strategy Pattern, and Observer Pattern.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== VENDING MACHINE LLD DEMONSTRATION ===\n");

        VendingMachineSystem vendingMachine = new VendingMachineSystem();

        // Show help information
        vendingMachine.showHelp();

        // Demonstration 1: Basic product purchase
        demonstrateBasicPurchase(vendingMachine);

        // Demonstration 2: Name-based product selection
        demonstrateNameBasedSelection(vendingMachine);

        // Demonstration 3: Different payment methods
        demonstratePaymentMethods(vendingMachine);

        // Demonstration 4: Maintenance and monitoring
        demonstrateMaintenance(vendingMachine);

        // Demonstration 5: Error handling
        demonstrateErrorHandling(vendingMachine);

        System.out.println("\n=== DEMONSTRATION COMPLETED ===");
    }

    /**
     * Demonstrates basic product purchase flow.
     */
    private static void demonstrateBasicPurchase(VendingMachineSystem vendingMachine) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DEMONSTRATION 1: Basic Product Purchase");
        System.out.println("=".repeat(50));

        // Display inventory
        vendingMachine.displayInventory();

        // Select product by slot
        vendingMachine.selectProductBySlot("A1");

        // Insert coins
        vendingMachine.insertCoin(CoinType.QUARTER);
        vendingMachine.insertCoin(CoinType.QUARTER);
        vendingMachine.insertCoin(CoinType.QUARTER);
        vendingMachine.insertCoin(CoinType.QUARTER);
        vendingMachine.insertCoin(CoinType.QUARTER);

        // Process payment
        vendingMachine.processPayment();

        pauseForEffect();
    }

    /**
     * Demonstrates name-based product selection.
     */
    private static void demonstrateNameBasedSelection(VendingMachineSystem vendingMachine) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DEMONSTRATION 2: Name-based Product Selection");
        System.out.println("=".repeat(50));

        // Display inventory first
        vendingMachine.displayInventory();

        // Select by product name
        System.out.println("\nSelecting by product name:");
        vendingMachine.selectProductByName("pepsi");

        // Insert coins
        vendingMachine.insertCoin(CoinType.DOLLAR);
        vendingMachine.insertCoin(CoinType.QUARTER);

        // Process payment
        vendingMachine.processPayment();

        pauseForEffect();

        // Select by product type
        System.out.println("\nSelecting by product type:");
        vendingMachine.selectProductByName("snack");

        // Insert coins
        vendingMachine.insertCoin(CoinType.DOLLAR);
        vendingMachine.insertCoin(CoinType.DOLLAR);

        // Process payment
        vendingMachine.processPayment();

        pauseForEffect();
    }

    /**
     * Demonstrates different payment methods.
     */
    private static void demonstratePaymentMethods(VendingMachineSystem vendingMachine) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DEMONSTRATION 3: Different Payment Methods");
        System.out.println("=".repeat(50));

        // Card payment
        System.out.println("\n--- Card Payment ---");
        vendingMachine.selectProductBySlot("B1");
        vendingMachine.setPaymentMethod(PaymentMethod.CARD);
        vendingMachine.insertCoin(CoinType.DOLLAR);
        vendingMachine.insertCoin(CoinType.DOLLAR);
        vendingMachine.processPayment();

        pauseForEffect();

        // Mobile payment
        System.out.println("\n--- Mobile Payment ---");
        vendingMachine.selectProductBySlot("C1");
        vendingMachine.setPaymentMethod(PaymentMethod.MOBILE);
        vendingMachine.insertCoin(CoinType.QUARTER);
        vendingMachine.insertCoin(CoinType.QUARTER);
        vendingMachine.processPayment();

        pauseForEffect();
    }

    /**
     * Demonstrates maintenance and monitoring features.
     */
    private static void demonstrateMaintenance(VendingMachineSystem vendingMachine) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DEMONSTRATION 4: Maintenance & Monitoring");
        System.out.println("=".repeat(50));

        // Show maintenance report
        System.out.println("\nMaintenance Report:");
        System.out.println(vendingMachine.getMaintenanceReport());

        // Restock some items
        System.out.println("\nRestocking products...");
        vendingMachine.restockProduct("A1", 5);
        vendingMachine.restockProduct("B2", 10);

        // Enter service mode
        System.out.println("\nEntering service mode...");
        vendingMachine.setServiceMode(true);

        // Try to make a purchase while in service mode
        System.out.println("\nAttempting purchase during maintenance...");
        vendingMachine.selectProductBySlot("A1");

        // Exit service mode
        System.out.println("\nExiting service mode...");
        vendingMachine.setServiceMode(false);

        pauseForEffect();
    }

    /**
     * Demonstrates error handling scenarios.
     */
    private static void demonstrateErrorHandling(VendingMachineSystem vendingMachine) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("DEMONSTRATION 5: Error Handling");
        System.out.println("=".repeat(50));

        // Insufficient funds
        System.out.println("\n--- Insufficient Funds ---");
        vendingMachine.selectProductBySlot("B3"); // $2.00 item
        vendingMachine.insertCoin(CoinType.DOLLAR); // Only $1.00
        vendingMachine.processPayment();

        pauseForEffect();

        // Invalid product selection
        System.out.println("\n--- Invalid Product Selection ---");
        vendingMachine.selectProductBySlot("Z9"); // Non-existent slot

        pauseForEffect();

        // Transaction cancellation
        System.out.println("\n--- Transaction Cancellation ---");
        vendingMachine.selectProductBySlot("C2");
        vendingMachine.insertCoin(CoinType.DOLLAR);
        vendingMachine.cancelTransaction();

        pauseForEffect();
    }

    /**
     * Pauses execution for dramatic effect in demonstrations.
     */
    private static void pauseForEffect() {
        try {
            Thread.sleep(2000); // 2 second pause
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
