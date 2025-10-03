package org.example.interfaces;

/**
 * Payment Strategy interface for the Strategy Pattern implementation.
 * Defines the contract for different payment methods.
 */
public interface PaymentStrategy {

    /**
     * Processes payment for the given amount.
     * @param amount The amount to be paid in cents
     * @return true if payment was successful, false otherwise
     */
    boolean processPayment(int amount);

    /**
     * Gets the name of the payment method.
     * @return String representation of the payment method
     */
    String getPaymentMethodName();

    /**
     * Validates if the payment method is available.
     * @return true if payment method is available, false otherwise
     */
    boolean isAvailable();
}
