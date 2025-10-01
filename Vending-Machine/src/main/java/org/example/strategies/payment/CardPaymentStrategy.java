package org.example.strategies.payment;

import org.example.enums.TransactionStatus;
import org.example.interfaces.PaymentStrategy;
import org.example.model.Transaction;

/**
 * Strategy Pattern: Card-based payment processing
 * Simulates card payment validation and processing
 */
public class CardPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Transaction transaction) {
        // Simulate card payment processing
        // In a real implementation, this would integrate with payment gateway

        double requiredAmount = transaction.getRequiredAmount();

        // Simulate payment processing with 95% success rate
        boolean paymentSuccessful = Math.random() > 0.05;

        if (paymentSuccessful && requiredAmount > 0) {
            transaction.setStatus(TransactionStatus.COMPLETED);
            return true;
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            return false;
        }
    }

    @Override
    public boolean processRefund(Transaction transaction) {
        // Simulate refund processing
        // In a real implementation, this would process refund through payment gateway

        if (transaction.getAmountPaid() > 0) {
            transaction.setStatus(TransactionStatus.REFUNDED);
            // For card payments, refund goes back to card (no physical change)
            return true;
        }
        return false;
    }

    /**
     * Validate card details (simplified validation)
     */
    public boolean validateCard(String cardNumber, String expiryDate, String cvv) {
        // Basic validation - in real implementation would be more comprehensive
        return cardNumber != null && cardNumber.length() >= 13 &&
               expiryDate != null && expiryDate.length() == 5 &&
               cvv != null && cvv.length() >= 3;
    }
}
