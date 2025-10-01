package org.example.observers;

import org.example.interfaces.VendingMachineObserver;
import org.example.model.Product;
import org.example.model.Transaction;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Observer Pattern: Maintenance observer for monitoring vending machine health
 * Tracks low inventory, frequent failures, and maintenance needs
 */
public class MaintenanceObserver implements VendingMachineObserver {

    private final AtomicInteger totalTransactions;
    private final AtomicInteger failedTransactions;
    private final AtomicInteger lowInventoryAlerts;

    public MaintenanceObserver() {
        this.totalTransactions = new AtomicInteger(0);
        this.failedTransactions = new AtomicInteger(0);
        this.lowInventoryAlerts = new AtomicInteger(0);
    }

    @Override
    public void onProductDispensed(Product product, Transaction transaction) {
        totalTransactions.incrementAndGet();

        // Check for low inventory
        if (product.getQuantity() <= 2) {
            lowInventoryAlerts.incrementAndGet();
            System.out.printf("[%s] [MAINTENANCE] LOW INVENTORY ALERT: %s has only %d items remaining%n",
                    LocalDateTime.now(), product.getName(), product.getQuantity());
        }

        // Periodic maintenance check
        if (totalTransactions.get() % 10 == 0) {
            performMaintenanceCheck();
        }
    }

    @Override
    public void onPaymentReceived(Transaction transaction) {
        // Monitor payment processing
    }

    @Override
    public void onTransactionFailed(Transaction transaction, String reason) {
        failedTransactions.incrementAndGet();

        double failureRate = (double) failedTransactions.get() / totalTransactions.get() * 100;
        if (failureRate > 10.0) {
            System.out.printf("[%s] [MAINTENANCE] HIGH FAILURE RATE ALERT: %.1f%% of transactions failing%n",
                    LocalDateTime.now(), failureRate);
        }
    }

    @Override
    public void onRefundProcessed(Transaction transaction, double amount) {
        // Monitor refunds - high refund rate might indicate machine issues
    }

    private void performMaintenanceCheck() {
        System.out.printf("[%s] [MAINTENANCE] Periodic check: %d total transactions, %d failures, %d low inventory alerts%n",
                LocalDateTime.now(), totalTransactions.get(), failedTransactions.get(), lowInventoryAlerts.get());
    }

    // Maintenance statistics getters
    public int getTotalTransactions() {
        return totalTransactions.get();
    }

    public int getFailedTransactions() {
        return failedTransactions.get();
    }

    public int getLowInventoryAlerts() {
        return lowInventoryAlerts.get();
    }

    public double getFailureRate() {
        int total = totalTransactions.get();
        return total > 0 ? (double) failedTransactions.get() / total * 100 : 0.0;
    }
}
