package org.example.strategies.payment;

import org.example.interfaces.PaymentStrategy;
import org.example.model.Transaction;

/**
 * Cash payment strategy that validates coin-based payments.
 * Follows Strategy Pattern and Single Responsibility Principle.
 */
public class CashPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Transaction transaction) {
        System.out.println("Processing cash payment...");
        
        // Validate that sufficient coins have been inserted
        if (transaction.getTotalInsertedAmount() < transaction.getProductPrice()) {
            System.out.println("ERROR: Insufficient cash inserted.");
            System.out.println("Required: $" + 
                String.format("%.2f", transaction.getProductPrice() / 100.0));
            System.out.println("Inserted: $" + 
                String.format("%.2f", transaction.getTotalInsertedAmount() / 100.0));
            return false;
        }
        
        // Cash payment is always successful if amount is sufficient
        System.out.println("Cash payment validated: $" + 
            String.format("%.2f", transaction.getTotalInsertedAmount() / 100.0));
        
        if (transaction.getChangeAmount() > 0) {
            System.out.println("Change to return: $" + 
                String.format("%.2f", transaction.getChangeAmount() / 100.0));
        }
        
        return true;
    }

    @Override
    public String getPaymentMethod() {
        return "CASH";
    }
}

