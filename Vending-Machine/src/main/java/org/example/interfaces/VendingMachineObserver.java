package org.example.interfaces;

import org.example.model.Product;
import org.example.model.Transaction;

/**
 * Observer Pattern: Observers get notified of vending machine events
 */
public interface VendingMachineObserver {
    /**
     * Called when a product is dispensed
     */
    void onProductDispensed(Product product, Transaction transaction);
    
    /**
     * Called when payment is received
     */
    void onPaymentReceived(double amount, String paymentMethod);
    
    /**
     * Called when change is returned
     */
    void onChangeReturned(double changeAmount);
    
    /**
     * Called when a product is out of stock
     */
    void onProductOutOfStock(Product product);
    
    /**
     * Called when a product is low in stock
     */
    void onProductLowStock(Product product, int remainingStock);
    
    /**
     * Called when transaction is cancelled
     */
    void onTransactionCancelled(double refundAmount);
    
    /**
     * Called when an error occurs
     */
    void onError(String errorMessage);
}

