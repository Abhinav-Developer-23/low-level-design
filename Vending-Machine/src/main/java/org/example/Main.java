package org.example;

import org.example.enums.PaymentMethod;
import org.example.observers.ConsoleVendingObserver;
import org.example.observers.MaintenanceObserver;
import org.example.system.VendingMachineSystem;

/**
 * Main class demonstrating the Vending Machine Low-Level Design.
 * Showcases multiple scenarios including:
 * - Cash payment with exact change
 * - Cash payment with change return
 * - Card payment
 * - Mobile payment
 * - Name-based product selection
 * - Transaction cancellation
 * - Maintenance mode
 * - Low stock alerts
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println("          VENDING MACHINE - LOW LEVEL DESIGN DEMO                      ");
        System.out.println("          State Pattern | Strategy Pattern | Observer Pattern          ");
        System.out.println("========================================================================\n");

        // Initialize vending machine
        VendingMachineSystem vendingMachine = new VendingMachineSystem();
        
        // Add observers
        ConsoleVendingObserver consoleObserver = new ConsoleVendingObserver();
        MaintenanceObserver maintenanceObserver = new MaintenanceObserver();
        vendingMachine.addObserver(consoleObserver);
        vendingMachine.addObserver(maintenanceObserver);

        // Display initial inventory
        vendingMachine.displayInventory();
        
        // Run demo scenarios
        scenario1_CashPaymentExactChange(vendingMachine);
        pause();
        
        scenario2_CashPaymentWithChange(vendingMachine);
        pause();
        
        scenario3_CardPayment(vendingMachine);
        pause();
        
        scenario4_MobilePayment(vendingMachine);
        pause();
        
        scenario5_NameBasedSelection(vendingMachine);
        pause();
        
        scenario6_TransactionCancellation(vendingMachine);
        pause();
        
        scenario7_OutOfStockHandling(vendingMachine);
        pause();
        
        scenario8_MaintenanceMode(vendingMachine);
        
        // Display final status
        System.out.println("\n" + vendingMachine.getMachineStatus());
        
        // Display maintenance logs
        maintenanceObserver.printLogs();
        
        System.out.println("\n========================================================================");
        System.out.println("                    DEMO COMPLETED                                      ");
        System.out.println("========================================================================");
    }

    /**
     * Scenario 1: Purchase with exact cash payment
     */
    private static void scenario1_CashPaymentExactChange(VendingMachineSystem vm) {
        printScenarioHeader("Scenario 1: Cash Payment with Exact Change");
        
        vm.selectProductBySlot("A1"); // Chips - $1.25
        vm.insertCoin(100); // $1.00
        vm.insertCoin(25);  // $0.25
        vm.processPayment();
    }

    /**
     * Scenario 2: Purchase with cash and change return
     */
    private static void scenario2_CashPaymentWithChange(VendingMachineSystem vm) {
        printScenarioHeader("Scenario 2: Cash Payment with Change Return");
        
        vm.selectProductBySlot("A3"); // Soda - $1.75
        vm.insertCoin(100); // $1.00
        vm.insertCoin(100); // $1.00 (Total: $2.00, Change: $0.25)
        vm.processPayment();
    }

    /**
     * Scenario 3: Purchase with card payment
     */
    private static void scenario3_CardPayment(VendingMachineSystem vm) {
        printScenarioHeader("Scenario 3: Card Payment");
        
        vm.setPaymentMethod(PaymentMethod.CARD);
        vm.selectProductBySlot("A2"); // Chocolate - $1.50
        vm.processPayment();
    }

    /**
     * Scenario 4: Purchase with mobile payment
     */
    private static void scenario4_MobilePayment(VendingMachineSystem vm) {
        printScenarioHeader("Scenario 4: Mobile Payment");
        
        vm.setPaymentMethod(PaymentMethod.MOBILE);
        vm.selectProductBySlot("B1"); // Candy - $1.00
        vm.processPayment();
    }

    /**
     * Scenario 5: Name-based product selection
     */
    private static void scenario5_NameBasedSelection(VendingMachineSystem vm) {
        printScenarioHeader("Scenario 5: Name-Based Product Selection");
        
        vm.setPaymentMethod(PaymentMethod.CASH);
        vm.selectProductByName("water"); // Will find Water product
        vm.insertCoin(100); // $1.00
        vm.insertCoin(25);  // $0.25
        vm.processPayment();
    }

    /**
     * Scenario 6: Transaction cancellation with refund
     */
    private static void scenario6_TransactionCancellation(VendingMachineSystem vm) {
        printScenarioHeader("Scenario 6: Transaction Cancellation");
        
        vm.setPaymentMethod(PaymentMethod.CASH);
        vm.selectProductBySlot("B2"); // Gum - $0.50
        vm.insertCoin(25);  // $0.25
        System.out.println("\nUser decides to cancel...");
        vm.cancelTransaction(); // Should refund $0.25
    }

    /**
     * Scenario 7: Handling out-of-stock products
     */
    private static void scenario7_OutOfStockHandling(VendingMachineSystem vm) {
        printScenarioHeader("Scenario 7: Out of Stock Handling");
        
        // First, let's buy all remaining C2 items (should be 3)
        System.out.println("Purchasing all C2 items to create out-of-stock scenario...\n");
        
        vm.setPaymentMethod(PaymentMethod.CASH);
        for (int i = 0; i < 3; i++) {
            vm.selectProductBySlot("C2"); // Chocolate - $1.50
            vm.insertCoin(100);
            vm.insertCoin(25);
            vm.insertCoin(25);
            vm.processPayment();
            if (i < 2) pause(500);
        }
        
        System.out.println("\n--- Now trying to buy from empty slot ---\n");
        vm.selectProductBySlot("C2"); // Should fail - out of stock
    }

    /**
     * Scenario 8: Maintenance mode
     */
    private static void scenario8_MaintenanceMode(VendingMachineSystem vm) {
        printScenarioHeader("Scenario 8: Maintenance Mode");
        
        vm.performMaintenance();
        
        System.out.println("\n--- Attempting to purchase while in maintenance ---");
        vm.selectProductBySlot("A1"); // Should be rejected
        
        System.out.println("\n--- Restocking low inventory items ---");
        vm.restockProduct("C2", 10);
        
        System.out.println("\n--- Returning to service ---");
        vm.returnToService();
        
        System.out.println("\n--- Verifying machine is operational ---");
        vm.setPaymentMethod(PaymentMethod.CASH);
        vm.selectProductBySlot("C2"); // Now should work
        vm.insertCoin(100);
        vm.insertCoin(25);
        vm.insertCoin(25);
        vm.processPayment();
    }

    /**
     * Prints a scenario header.
     */
    private static void printScenarioHeader(String title) {
        System.out.println("\n========================================================================");
        System.out.println("  " + title);
        System.out.println("========================================================================\n");
    }

    /**
     * Pauses execution for better demo readability.
     */
    private static void pause() {
        pause(1500);
    }

    /**
     * Pauses execution for the specified milliseconds.
     */
    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

