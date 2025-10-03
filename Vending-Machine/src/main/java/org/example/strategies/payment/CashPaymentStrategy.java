package org.example.strategies.payment;

import org.example.interfaces.PaymentStrategy;
import org.example.model.Transaction;

/**
 * Concrete strategy for handling cash payments.
 */
public class CashPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Transaction transaction) {
        try {
            // Validate that we have enough coins inserted
            if (!transaction.isPaymentComplete()) {
                System.out.println("Insufficient cash inserted for " + transaction.getProductId());
                return false;
            }

            // For cash payment, we just validate the amount
            // In a real system, this would handle coin validation and storage
            System.out.println("Processing cash payment for " + transaction.getProductId());
            System.out.println("Total inserted: " + transaction.getTotalInsertedAmount() + " cents");
            System.out.println("Product price: " + transaction.getProductPrice() + " cents");

            return true;
        } catch (Exception e) {
            System.err.println("Error processing cash payment: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getPaymentMethod() {
        return "CASH";
    }
}
