package org.example;

import org.example.enums.CoinType;
import org.example.model.Coin;
import org.example.model.Transaction;
import org.example.observers.ConsoleVendingObserver;
import org.example.observers.MaintenanceObserver;
import org.example.strategies.payment.CardPaymentStrategy;
import org.example.strategies.selection.NameBasedSelectionStrategy;
import org.example.system.VendingMachineSystem;

/**
 * Main class demonstrating the vending machine system functionality
 * Shows usage of all design patterns and SOLID principles
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== VENDING MACHINE SYSTEM DEMO ===\n");

        // Get singleton instance
        VendingMachineSystem vendingMachine = VendingMachineSystem.getInstance();

        // Register observers
        ConsoleVendingObserver consoleObserver = new ConsoleVendingObserver("CONSOLE");
        MaintenanceObserver maintenanceObserver = new MaintenanceObserver();

        vendingMachine.registerObserver(consoleObserver);
        vendingMachine.registerObserver(maintenanceObserver);

        // Display initial status
        vendingMachine.displayStatus();

        System.out.println("\n=== DEMONSTRATION SCENARIOS ===\n");

        // Scenario 1: Successful coin purchase
        System.out.println("1. Successful coin purchase:");
        demonstrateCoinPurchase(vendingMachine);

        // Scenario 2: Product selection by name
        System.out.println("\n2. Product selection by name:");
        demonstrateNameBasedSelection(vendingMachine);

        // Scenario 3: Card payment
        System.out.println("\n3. Card payment:");
        demonstrateCardPayment(vendingMachine);

        // Scenario 4: Insufficient payment
        System.out.println("\n4. Insufficient payment (refund):");
        demonstrateInsufficientPayment(vendingMachine);

        // Scenario 5: Out of stock
        System.out.println("\n5. Product out of stock:");
        demonstrateOutOfStock(vendingMachine);

        // Scenario 6: Multiple concurrent transactions (simulated)
        System.out.println("\n6. Multiple transactions:");
        demonstrateMultipleTransactions(vendingMachine);

        // Display final status
        System.out.println("\n=== FINAL MACHINE STATUS ===");
        vendingMachine.displayStatus();

        // Display maintenance statistics
        System.out.println("\n=== MAINTENANCE STATISTICS ===");
        System.out.printf("Total transactions: %d%n", maintenanceObserver.getTotalTransactions());
        System.out.printf("Failed transactions: %d%n", maintenanceObserver.getFailedTransactions());
        System.out.printf("Failure rate: %.1f%%%n", maintenanceObserver.getFailureRate());
        System.out.printf("Low inventory alerts: %d%n", maintenanceObserver.getLowInventoryAlerts());

        System.out.println("\n=== DEMO COMPLETED ===");
    }

    private static void demonstrateCoinPurchase(VendingMachineSystem vendingMachine) {
        try {
            // Select product
            Transaction transaction = vendingMachine.selectProduct("P001");
            System.out.printf("Selected: %s ($%.2f)%n", transaction.getProduct().getName(), transaction.getRequiredAmount());

            // Insert coins
            vendingMachine.insertCoin(transaction, new Coin(CoinType.ONE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));

            System.out.printf("Total paid: $%.2f, Change: $%.2f%n", transaction.getAmountPaid(), transaction.getChangeAmount());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void demonstrateNameBasedSelection(VendingMachineSystem vendingMachine) {
        // Change to name-based selection strategy
        vendingMachine.setSelectionStrategy(new NameBasedSelectionStrategy());

        try {
            Transaction transaction = vendingMachine.selectProduct("chips");
            System.out.printf("Selected by name: %s ($%.2f)%n", transaction.getProduct().getName(), transaction.getRequiredAmount());

            // Insert exact payment
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.ONE));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Reset to basic selection
        vendingMachine.setSelectionStrategy(new org.example.strategies.selection.BasicProductSelectionStrategy());
    }

    private static void demonstrateCardPayment(VendingMachineSystem vendingMachine) {
        // Change to card payment strategy
        CardPaymentStrategy cardStrategy = new CardPaymentStrategy();
        vendingMachine.setPaymentStrategy(cardStrategy);

        try {
            Transaction transaction = vendingMachine.selectProduct("P004");
            System.out.printf("Selected: %s ($%.2f) - Paying by card%n", transaction.getProduct().getName(), transaction.getRequiredAmount());

            // Simulate card payment (no coins needed)
            // In a real implementation, this would integrate with card reader

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Reset to coin payment
        vendingMachine.setPaymentStrategy(new org.example.strategies.payment.CoinPaymentStrategy());
    }

    private static void demonstrateInsufficientPayment(VendingMachineSystem vendingMachine) {
        try {
            Transaction transaction = vendingMachine.selectProduct("P002");
            System.out.printf("Selected: %s ($%.2f)%n", transaction.getProduct().getName(), transaction.getRequiredAmount());

            // Insert insufficient coins
            vendingMachine.insertCoin(transaction, new Coin(CoinType.ONE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.FIVE));

            System.out.printf("Total paid: $%.2f (insufficient)%n", transaction.getAmountPaid());

            // Cancel transaction (would trigger refund)
            vendingMachine.cancelTransaction(transaction);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void demonstrateOutOfStock(VendingMachineSystem vendingMachine) {
        try {
            // Try to select a product that's running low
            Transaction transaction = vendingMachine.selectProduct("P006");
            System.out.printf("Selected: %s ($%.2f)%n", transaction.getProduct().getName(), transaction.getRequiredAmount());

            // Insert payment
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.TWENTY_FIVE));
            vendingMachine.insertCoin(transaction, new Coin(CoinType.ONE));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void demonstrateMultipleTransactions(VendingMachineSystem vendingMachine) {
        // Simulate multiple transactions
        for (int i = 0; i < 3; i++) {
            try {
                Transaction transaction = vendingMachine.selectProduct("P005");
                System.out.printf("Transaction %d: Selected %s%n", i + 1, transaction.getProduct().getName());

                // Insert exact payment
                vendingMachine.insertCoin(transaction, new Coin(CoinType.ONE));

            } catch (Exception e) {
                System.out.println("Error in transaction " + (i + 1) + ": " + e.getMessage());
            }
        }
    }
}