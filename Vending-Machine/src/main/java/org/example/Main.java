package org.example;

import org.example.enums.PaymentMethod;
import org.example.system.VendingMachineSystem;

/**
 * Main class to demonstrate the Vending Machine functionality.
 * This class shows how to use the vending machine through various scenarios.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== VENDING MACHINE DEMONSTRATION ===\n");

        VendingMachineSystem vendingMachine = new VendingMachineSystem();

        // Display initial inventory
        vendingMachine.displayInventory();

        // Demonstrate basic product selection and purchase
        demonstrateBasicPurchase(vendingMachine);

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Demonstrate name-based product selection
        demonstrateNameBasedSelection(vendingMachine);

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Demonstrate different payment methods
        demonstratePaymentMethods(vendingMachine);

        System.out.println("\n" + "=".repeat(50) + "\n");

        // Demonstrate maintenance operations
        vendingMachine.performMaintenance();

        System.out.println("\n=== DEMONSTRATION COMPLETE ===");
    }

    private static void demonstrateBasicPurchase(VendingMachineSystem vendingMachine) {
        System.out.println("=== DEMONSTRATING BASIC PURCHASE ===");

        // Select a product
        vendingMachine.selectProductBySlot("A1");

        // Insert coins (Chips cost 150 cents, so insert 150 cents)
        System.out.println("Inserting coins for payment...");
        vendingMachine.insertCoin(50);  // Quarter
        vendingMachine.insertCoin(50);  // Quarter
        vendingMachine.insertCoin(25);  // Quarter
        vendingMachine.insertCoin(25);  // Quarter

        // Process payment
        vendingMachine.processPayment();

        System.out.println();
    }

    private static void demonstrateNameBasedSelection(VendingMachineSystem vendingMachine) {
        System.out.println("=== DEMONSTRATING NAME-BASED SELECTION ===");

        // Try to select a product by name
        vendingMachine.selectProductByName("chocolate");

        // Insert coins for payment (Chocolate costs 200 cents)
        System.out.println("Inserting coins for payment...");
        vendingMachine.insertCoin(100); // Dollar
        vendingMachine.insertCoin(100); // Dollar

        // Process payment
        vendingMachine.processPayment();

        System.out.println();
    }

    private static void demonstratePaymentMethods(VendingMachineSystem vendingMachine) {
        System.out.println("=== DEMONSTRATING PAYMENT METHODS ===");

        // Select a product
        vendingMachine.selectProductBySlot("B1"); // Soda

        // Demonstrate different payment methods
        System.out.println("\n--- Cash Payment ---");
        vendingMachine.setPaymentMethod(PaymentMethod.CASH);
        vendingMachine.insertCoin(100);
        vendingMachine.insertCoin(50);
        vendingMachine.insertCoin(25);
        vendingMachine.processPayment();

        System.out.println("\n--- Card Payment ---");
        vendingMachine.selectProductBySlot("C1"); // Gum
        vendingMachine.setPaymentMethod(PaymentMethod.CARD);
        vendingMachine.processPayment();

        System.out.println("\n--- Mobile Payment ---");
        vendingMachine.selectProductBySlot("A2"); // Chocolate (if available)
        vendingMachine.setPaymentMethod(PaymentMethod.MOBILE);
        vendingMachine.processPayment();

        System.out.println();
    }
}
