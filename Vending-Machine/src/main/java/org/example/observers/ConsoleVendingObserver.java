package org.example.observers;

import org.example.interfaces.VendingMachineObserver;
import org.example.model.Product;
import org.example.model.Transaction;

import java.time.LocalDateTime;

/**
 * Observer Pattern: Console-based observer for vending machine events
 * Logs all vending machine activities to console
 */
public class ConsoleVendingObserver implements VendingMachineObserver {

    private final String observerId;

    public ConsoleVendingObserver(String observerId) {
        this.observerId = observerId;
    }

    @Override
    public void onProductDispensed(Product product, Transaction transaction) {
        System.out.printf("[%s] [%s] PRODUCT DISPENSED: %s (ID: %s) for transaction %s%n",
                LocalDateTime.now(), observerId, product.getName(), product.getProductId(), transaction.getTransactionId());
        System.out.printf("[%s] [%s] Remaining quantity: %d%n",
                LocalDateTime.now(), observerId, product.getQuantity());
    }

    @Override
    public void onPaymentReceived(Transaction transaction) {
        System.out.printf("[%s] [%s] PAYMENT RECEIVED: $%.2f for %s (Transaction: %s)%n",
                LocalDateTime.now(), observerId, transaction.getAmountPaid(),
                transaction.getProduct().getName(), transaction.getTransactionId());
    }

    @Override
    public void onTransactionFailed(Transaction transaction, String reason) {
        System.out.printf("[%s] [%s] TRANSACTION FAILED: %s (Transaction: %s, Reason: %s)%n",
                LocalDateTime.now(), observerId, transaction.getProduct().getName(),
                transaction.getTransactionId(), reason);
    }

    @Override
    public void onRefundProcessed(Transaction transaction, double amount) {
        System.out.printf("[%s] [%s] REFUND PROCESSED: $%.2f refunded for transaction %s%n",
                LocalDateTime.now(), observerId, amount, transaction.getTransactionId());
        System.out.printf("[%s] [%s] Change coins returned: %d coins%n",
                LocalDateTime.now(), observerId, transaction.getChangeCoins().size());
    }

    public String getObserverId() {
        return observerId;
    }
}
