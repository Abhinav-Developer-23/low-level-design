package org.example.quickcommerce.payment;

import org.example.quickcommerce.enums.PaymentMethod;
import org.example.quickcommerce.model.Payment;

import java.util.EnumMap;
import java.util.Map;

/**
 * Payment processor using Strategy pattern.
 */
public class PaymentProcessor {
    private final Map<PaymentMethod, PaymentStrategy> strategies;

    public PaymentProcessor() {
        this.strategies = new EnumMap<>(PaymentMethod.class);
        strategies.put(PaymentMethod.UPI, new UPIPaymentStrategy());
        strategies.put(PaymentMethod.CREDIT_CARD, new CardPaymentStrategy());
        strategies.put(PaymentMethod.DEBIT_CARD, new CardPaymentStrategy());
        strategies.put(PaymentMethod.CASH_ON_DELIVERY, new CashOnDeliveryStrategy());
    }

    public boolean processPayment(Payment payment) {
        PaymentStrategy strategy = strategies.get(payment.getMethod());
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy for: " + payment.getMethod());
        }
        if (!strategy.validatePayment(payment)) {
            payment.markFailed();
            return false;
        }
        return strategy.processPayment(payment);
    }
}

