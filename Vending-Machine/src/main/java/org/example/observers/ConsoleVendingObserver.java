package org.example.observers;

import org.example.interfaces.VendingMachineObserver;
import org.example.model.Product;
import org.example.model.Transaction;

/**
 * Console logging observer for vending machine events
 */
public class ConsoleVendingObserver implements VendingMachineObserver {
    private final String observerName;

    public ConsoleVendingObserver(String observerName) {
        this.observerName = observerName;
    }

    @Override
    public void onProductDispensed(Product product, Transaction transaction) {
        System.out.println("📢 [" + observerName + "] Product dispensed: " + product.getName() + 
                          " | Transaction ID: " + transaction.getTransactionId());
    }

    @Override
    public void onPaymentReceived(double amount, String paymentMethod) {
        System.out.println("📢 [" + observerName + "] Payment received: $" + 
                          String.format("%.2f", amount) + " via " + paymentMethod);
    }

    @Override
    public void onChangeReturned(double changeAmount) {
        System.out.println("📢 [" + observerName + "] Change returned: $" + 
                          String.format("%.2f", changeAmount));
    }

    @Override
    public void onProductOutOfStock(Product product) {
        System.out.println("📢 [" + observerName + "] ⚠️  Product out of stock: " + product.getName());
    }

    @Override
    public void onProductLowStock(Product product, int remainingStock) {
        System.out.println("📢 [" + observerName + "] ⚠️  Low stock alert: " + product.getName() + 
                          " - Only " + remainingStock + " remaining");
    }

    @Override
    public void onTransactionCancelled(double refundAmount) {
        System.out.println("📢 [" + observerName + "] Transaction cancelled. Refund: $" + 
                          String.format("%.2f", refundAmount));
    }

    @Override
    public void onError(String errorMessage) {
        System.out.println("📢 [" + observerName + "] ❌ Error: " + errorMessage);
    }
}

