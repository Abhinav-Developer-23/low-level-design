package org.example.observers;

import org.example.interfaces.VendingMachineObserver;
import org.example.model.Product;
import org.example.model.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Maintenance tracking observer
 * Tracks sales, revenue, and maintenance alerts
 */
public class MaintenanceObserver implements VendingMachineObserver {
    private final AtomicInteger totalSales;
    private double totalRevenue;
    private final ConcurrentHashMap<String, Integer> productSalesCount;
    private final DateTimeFormatter timeFormatter;

    public MaintenanceObserver() {
        this.totalSales = new AtomicInteger(0);
        this.totalRevenue = 0.0;
        this.productSalesCount = new ConcurrentHashMap<>();
        this.timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public void onProductDispensed(Product product, Transaction transaction) {
        totalSales.incrementAndGet();
        synchronized (this) {
            totalRevenue += product.getPrice();
        }
        productSalesCount.merge(product.getName(), 1, Integer::sum);
        
        log("✓ Sale completed: " + product.getName() + " | Total sales: " + totalSales.get());
    }

    @Override
    public void onPaymentReceived(double amount, String paymentMethod) {
        log("Payment received: $" + String.format("%.2f", amount) + " via " + paymentMethod);
    }

    @Override
    public void onChangeReturned(double changeAmount) {
        log("Change returned: $" + String.format("%.2f", changeAmount));
    }

    @Override
    public void onProductOutOfStock(Product product) {
        logAlert("CRITICAL - Out of Stock: " + product.getName() + " - RESTOCK REQUIRED");
    }

    @Override
    public void onProductLowStock(Product product, int remainingStock) {
        logAlert("WARNING - Low Stock: " + product.getName() + " - " + 
                remainingStock + " units remaining");
    }

    @Override
    public void onTransactionCancelled(double refundAmount) {
        log("Transaction cancelled. Refund issued: $" + String.format("%.2f", refundAmount));
    }

    @Override
    public void onError(String errorMessage) {
        logAlert("ERROR: " + errorMessage);
    }

    /**
     * Get sales statistics
     */
    public String getStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + "=".repeat(60) + "\n");
        sb.append("📊 MAINTENANCE REPORT\n");
        sb.append("=".repeat(60) + "\n");
        sb.append(String.format("Total Sales: %d\n", totalSales.get()));
        sb.append(String.format("Total Revenue: $%.2f\n", totalRevenue));
        sb.append("\nProduct Sales Breakdown:\n");
        productSalesCount.forEach((product, count) -> 
            sb.append(String.format("  - %-20s : %d units\n", product, count)));
        sb.append("=".repeat(60) + "\n");
        return sb.toString();
    }

    private void log(String message) {
        String timestamp = LocalDateTime.now().format(timeFormatter);
        System.out.println("[MAINTENANCE " + timestamp + "] " + message);
    }

    private void logAlert(String message) {
        String timestamp = LocalDateTime.now().format(timeFormatter);
        System.out.println("[MAINTENANCE ALERT " + timestamp + "] ⚠️  " + message);
    }

    public int getTotalSales() {
        return totalSales.get();
    }

    public synchronized double getTotalRevenue() {
        return totalRevenue;
    }
}

