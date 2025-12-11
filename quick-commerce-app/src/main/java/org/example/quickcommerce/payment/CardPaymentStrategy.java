package org.example.quickcommerce.payment;

import org.example.quickcommerce.enums.PaymentMethod;
import org.example.quickcommerce.model.Payment;

import java.util.UUID;

public class CardPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Payment payment) {
        String cardType = payment.getMethod() == PaymentMethod.CREDIT_CARD ? "Credit Card" : "Debit Card";
        System.out.println("Processing " + cardType + " payment of ₹" + payment.getAmount());
        boolean success = Math.random() < 0.90;
        if (success) {
            String transactionId = "CARD_" + UUID.randomUUID().toString().substring(0, 8);
            payment.markSuccess(transactionId);
            System.out.println(cardType + " Payment successful. Transaction ID: " + transactionId);
        } else {
            payment.markFailed();
            System.out.println(cardType + " Payment failed");
        }
        return success;
    }

    @Override
    public boolean validatePayment(Payment payment) {
        return payment.getAmount() > 0;
    }
}

