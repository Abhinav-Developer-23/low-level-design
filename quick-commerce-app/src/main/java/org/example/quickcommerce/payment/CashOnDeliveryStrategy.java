package org.example.quickcommerce.payment;

import org.example.quickcommerce.model.Payment;

import java.util.UUID;

public class CashOnDeliveryStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Payment payment) {
        System.out.println("Processing Cash on Delivery for ₹" + payment.getAmount());
        String transactionId = "COD_" + UUID.randomUUID().toString().substring(0, 8);
        payment.markSuccess(transactionId);
        System.out.println("COD order confirmed. Reference: " + transactionId);
        return true;
    }

    @Override
    public boolean validatePayment(Payment payment) {
        return payment.getAmount() > 0 && payment.getAmount() <= 5000;
    }
}

