package org.example.interfaces;

import org.example.model.Product;
import org.example.model.Transaction;

/**
 * Observer Pattern: Interface for vending machine event notifications
 * Follows Interface Segregation Principle (ISP)
 */
public interface VendingMachineObserver {
    /**
     * Called when a product is dispensed
     * @param product The product that was dispensed
     * @param transaction The associated transaction
     */
    void onProductDispensed(Product product, Transaction transaction);

    /**
     * Called when payment is received
     * @param transaction The transaction for which payment was received
     */
    void onPaymentReceived(Transaction transaction);

    /**
     * Called when a transaction fails
     * @param transaction The failed transaction
     * @param reason The reason for failure
     */
    void onTransactionFailed(Transaction transaction, String reason);

    /**
     * Called when money is refunded
     * @param transaction The transaction being refunded
     * @param amount The refund amount
     */
    void onRefundProcessed(Transaction transaction, double amount);
}
