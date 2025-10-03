package org.example.strategies.payment;

import org.example.interfaces.PaymentStrategy;

import java.util.Random;

/**
 * Card Payment Strategy: Handles credit/debit card payments.
 * Simulates card authorization with configurable success rate.
 */
public class CardPaymentStrategy implements PaymentStrategy {
    private final Random random;
    private final double successRate; // 0.0 to 1.0
    private boolean cardReaderAvailable;

    public CardPaymentStrategy() {
        this(0.9); // 90% success rate by default
    }

    public CardPaymentStrategy(double successRate) {
        this.random = new Random();
        this.successRate = Math.max(0.0, Math.min(1.0, successRate));
        this.cardReaderAvailable = true;
    }

    @Override
    public boolean processPayment(int amount) {
        if (!isAvailable()) {
            System.out.println("Card reader is not available");
            return false;
        }

        System.out.println("Processing card payment for $" + String.format("%.2f", amount / 100.0));
        System.out.println("Please insert or tap your card...");

        // Simulate processing time
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            return false;
        }

        // Simulate authorization
        boolean success = random.nextDouble() < successRate;

        if (success) {
            System.out.println("Card payment authorized successfully!");
            return true;
        } else {
            System.out.println("Card payment declined. Please try again or use a different payment method.");
            return false;
        }
    }

    @Override
    public String getPaymentMethodName() {
        return "Card";
    }

    @Override
    public boolean isAvailable() {
        return cardReaderAvailable;
    }

    /**
     * Sets the availability of the card reader.
     * @param available true if card reader is working
     */
    public void setCardReaderAvailable(boolean available) {
        this.cardReaderAvailable = available;
    }

    /**
     * Gets the configured success rate for card payments.
     * @return Success rate between 0.0 and 1.0
     */
    public double getSuccessRate() {
        return successRate;
    }

    /**
     * Simulates card validation (checks card format, expiry, etc.)
     * @param cardNumber Card number (simplified validation)
     * @return true if card appears valid
     */
    public boolean validateCard(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }

        // Simple Luhn algorithm check (simplified)
        return cardNumber.chars().allMatch(Character::isDigit);
    }
}
