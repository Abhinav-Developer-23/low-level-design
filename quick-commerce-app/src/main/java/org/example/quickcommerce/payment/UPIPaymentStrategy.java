package org.example.quickcommerce.payment;

import org.example.quickcommerce.model.Payment;

import java.util.UUID;

public class UPIPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Payment payment) {
        System.out.println("Processing UPI payment of ₹" + payment.getAmount());
        boolean success = Math.random() < 0.95;
        if (success) {
            String transactionId = "UPI_" + UUID.randomUUID().toString().substring(0, 8);
            payment.markSuccess(transactionId);
            System.out.println("UPI Payment successful. Transaction ID: " + transactionId);
        } else {
            payment.markFailed();
            System.out.println("UPI Payment failed");
        }
        return success;
    }

    @Override
    public boolean validatePayment(Payment payment) {
        return payment.getAmount() > 0;
    }
}

