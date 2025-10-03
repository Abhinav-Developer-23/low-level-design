package org.example.observers;

import org.example.interfaces.VendingMachineObserver;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Maintenance observer that tracks machine events for maintenance purposes.
 * Keeps logs of important events and alerts when maintenance is needed.
 * Follows Observer Pattern and Single Responsibility Principle.
 */
public class MaintenanceObserver implements VendingMachineObserver {
    private final List<String> maintenanceLogs;
    private int transactionCount;
    private int failureCount;
    private static final DateTimeFormatter FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MaintenanceObserver() {
        this.maintenanceLogs = new ArrayList<>();
        this.transactionCount = 0;
        this.failureCount = 0;
    }

    @Override
    public void onProductSelected(String productId) {
        // Track product selection for usage statistics
    }

    @Override
    public void onCoinInserted(int coinValue) {
        // Track coin insertion for revenue tracking
    }

    @Override
    public void onPaymentProcessed(int amount, String method) {
        addLog("Payment processed: $" + String.format("%.2f", amount / 100.0) + " via " + method);
    }

    @Override
    public void onProductDispensed(String productId) {
        transactionCount++;
        addLog("Product dispensed: " + productId + " (Total transactions: " + transactionCount + ")");
    }

    @Override
    public void onTransactionCompleted(String transactionId) {
        // Transaction tracking
    }

    @Override
    public void onTransactionFailed(String transactionId, String reason) {
        failureCount++;
        String id = (transactionId != null && transactionId.length() >= 8) 
            ? transactionId.substring(0, 8) 
            : transactionId;
        addLog("FAILURE: Transaction " + id + " failed - " + reason);
        
        // Alert if failure rate is high
        if (failureCount >= 3) {
            System.out.println("\n[WARNING] [MAINTENANCE ALERT] High failure rate detected!");
            System.out.println("    Failures: " + failureCount + " | Successes: " + 
                             (transactionCount - failureCount));
        }
    }

    @Override
    public void onMaintenanceRequired(String message) {
        addLog("[WARNING] MAINTENANCE ALERT: " + message);
        System.out.println("\n[WARNING] [MAINTENANCE OBSERVER] " + message);
    }

    /**
     * Adds a log entry with timestamp.
     * 
     * @param message the log message
     */
    private void addLog(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logEntry = "[" + timestamp + "] " + message;
        maintenanceLogs.add(logEntry);
    }

    /**
     * Gets all maintenance logs.
     * 
     * @return list of log entries
     */
    public List<String> getMaintenanceLogs() {
        return new ArrayList<>(maintenanceLogs);
    }

    /**
     * Gets the total transaction count.
     * 
     * @return transaction count
     */
    public int getTransactionCount() {
        return transactionCount;
    }

    /**
     * Gets the failure count.
     * 
     * @return failure count
     */
    public int getFailureCount() {
        return failureCount;
    }

    /**
     * Prints all maintenance logs to console.
     */
    public void printLogs() {
        System.out.println("\n=== MAINTENANCE LOGS ===");
        for (String log : maintenanceLogs) {
            System.out.println(log);
        }
        System.out.println("========================\n");
    }
}

