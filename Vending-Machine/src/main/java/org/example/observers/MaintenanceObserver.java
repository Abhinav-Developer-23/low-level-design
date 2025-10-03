package org.example.observers;

import org.example.enums.CoinType;
import org.example.enums.MachineState;
import org.example.enums.TransactionStatus;
import org.example.interfaces.VendingMachineObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Maintenance Observer: Monitors machine health and maintenance needs.
 * Tracks inventory levels, error rates, and generates maintenance alerts.
 */
public class MaintenanceObserver implements VendingMachineObserver {
    private int lowStockThreshold;
    private int errorCount;
    private int transactionCount;
    private LocalDateTime lastMaintenanceCheck;
    private final List<String> maintenanceLog;

    public MaintenanceObserver() {
        this(5); // Default low stock threshold of 5 items
    }

    public MaintenanceObserver(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
        this.errorCount = 0;
        this.transactionCount = 0;
        this.lastMaintenanceCheck = LocalDateTime.now();
        this.maintenanceLog = new ArrayList<>();
    }

    @Override
    public void onStateChange(MachineState oldState, MachineState newState) {
        // Monitor for stuck states or frequent state changes
        if (newState == MachineState.OUT_OF_SERVICE) {
            logMaintenanceEvent("Machine entered maintenance mode");
        }
    }

    @Override
    public void onTransactionUpdate(String transactionId, TransactionStatus status) {
        transactionCount++;

        if (status == TransactionStatus.ERROR || status == TransactionStatus.CANCELLED) {
            errorCount++;
            double errorRate = (double) errorCount / transactionCount * 100;

            if (errorRate > 10.0) { // More than 10% error rate
                logMaintenanceEvent("High error rate detected: " + String.format("%.1f%%", errorRate));
            }
        }

        // Periodic maintenance reminder
        if (transactionCount % 100 == 0) {
            logMaintenanceEvent("Maintenance reminder: " + transactionCount + " transactions processed");
        }
    }

    @Override
    public void onProductDispensed(String slotId, String productName) {
        // Could track product popularity here
    }

    @Override
    public void onCoinInserted(Object coinType, int amount) {
        // Monitor coin hopper levels if implemented
    }

    @Override
    public void onMaintenanceAlert(String message) {
        logMaintenanceEvent("Alert received: " + message);
        System.out.println("[MAINTENANCE] " + message);
    }

    @Override
    public String getObserverName() {
        return "Maintenance Observer";
    }

    /**
     * Checks if maintenance is needed based on various factors.
     * @return true if maintenance is recommended
     */
    public boolean isMaintenanceNeeded() {
        return errorCount > 5 ||
               transactionCount > 500 ||
               !maintenanceLog.isEmpty();
    }

    /**
     * Gets the current error rate as a percentage.
     * @return Error rate (0.0 to 100.0)
     */
    public double getErrorRate() {
        if (transactionCount == 0) return 0.0;
        return (double) errorCount / transactionCount * 100.0;
    }

    /**
     * Gets the low stock threshold.
     * @return Current threshold value
     */
    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    /**
     * Sets the low stock threshold.
     * @param threshold New threshold value
     */
    public void setLowStockThreshold(int threshold) {
        this.lowStockThreshold = threshold;
    }

    /**
     * Gets the maintenance log.
     * @return List of maintenance events
     */
    public List<String> getMaintenanceLog() {
        return new ArrayList<>(maintenanceLog);
    }

    /**
     * Clears the maintenance log and resets counters.
     */
    public void resetMaintenanceData() {
        errorCount = 0;
        transactionCount = 0;
        maintenanceLog.clear();
        lastMaintenanceCheck = LocalDateTime.now();
        logMaintenanceEvent("Maintenance data reset");
    }

    /**
     * Generates a maintenance report.
     * @return Formatted maintenance report
     */
    public String generateMaintenanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== MAINTENANCE REPORT ===\n");
        report.append("Generated: ").append(LocalDateTime.now()).append("\n");
        report.append("Transactions processed: ").append(transactionCount).append("\n");
        report.append("Errors encountered: ").append(errorCount).append("\n");
        report.append("Error rate: ").append(String.format("%.1f%%", getErrorRate())).append("\n");
        report.append("Low stock threshold: ").append(lowStockThreshold).append("\n");
        report.append("Maintenance needed: ").append(isMaintenanceNeeded() ? "YES" : "NO").append("\n");

        if (!maintenanceLog.isEmpty()) {
            report.append("\nMaintenance Events:\n");
            for (String event : maintenanceLog) {
                report.append("  - ").append(event).append("\n");
            }
        }

        return report.toString();
    }

    private void logMaintenanceEvent(String event) {
        String logEntry = String.format("[%s] %s",
                                      LocalDateTime.now().toString(),
                                      event);
        maintenanceLog.add(logEntry);
    }
}
