package org.lowLevelDesign.DesignPatterns;

import lombok.extern.slf4j.Slf4j;

// Step 1: Define the Strategy Interface
// This interface defines the method that all payment strategies must implement.
interface PaymentStrategy {
    void pay(double amount);
}

// Step 2: Implement Concrete Strategies (Payment Methods)

// Concrete Strategy 1: Credit Card Payment
class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Credit Card [Card Holder: " + cardHolderName + ", Card Number: " + cardNumber + "]");
    }
}

// Concrete Strategy 2: PayPal Payment
class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using PayPal [Email: " + email + "]");
    }
}

// Concrete Strategy 3: Bitcoin Payment
class BitcoinPayment implements PaymentStrategy {
    private String walletAddress;

    public BitcoinPayment(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid $" + amount + " using Bitcoin [Wallet Address: " + walletAddress + "]");
    }
}

// Step 3: Context Class (PaymentProcessor)
// The context class does not know or care which specific payment method is being used.
// It only interacts with the PaymentStrategy interface.
class PaymentProcessor {
    private PaymentStrategy paymentStrategy;

    // Constructor takes a PaymentStrategy object (dependency injection)
    public PaymentProcessor(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    // The method to execute the payment process
    public void processPayment(double amount) {
        paymentStrategy.pay(amount);
    }

    // Method to change the payment strategy dynamically
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
}
// Main Class to Demonstrate the Strategy Pattern
public class StrategyPatternExample {
    public static void main(String[] args) {

        // Create a PaymentProcessor using a Credit Card strategy
        PaymentProcessor processor = new PaymentProcessor(new CreditCardPayment("1234-5678-9876", "John Doe"));
        processor.processPayment(250.0); // Payment with Credit Card

        // Change the payment method to PayPal and process another payment
        processor.setPaymentStrategy(new PayPalPayment("john.doe@example.com"));
        processor.processPayment(150.0); // Payment with PayPal

        // Change the payment method to Bitcoin and process another payment
        processor.setPaymentStrategy(new BitcoinPayment("1A2B3C4D5E6F"));
        processor.processPayment(500.0); // Payment with Bitcoin
    }
}
