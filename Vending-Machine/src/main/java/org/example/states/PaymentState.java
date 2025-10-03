package org.example.states;

import org.example.enums.TransactionStatus;
import org.example.interfaces.State;
import org.example.system.VendingMachineContext;

/**
 * Concrete state class representing the payment processing state.
 * In this state, the payment is being validated and processed.
 */
public class PaymentState implements State {
    private final VendingMachineContext context;

    public PaymentState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void selectProduct(String productId) {
        System.out.println("Payment in progress. Cannot change product selection.");
    }

    @Override
    public void insertCoin(int coinValue) {
        System.out.println("Payment already in progress. Please wait.");
    }

    @Override
    public void processPayment() {
        try {
            // Validate payment
            if (!context.getCurrentTransaction().isPaymentComplete()) {
                context.getCurrentTransaction().setStatus(TransactionStatus.FAILED);
                System.out.println("Payment incomplete. Transaction failed.");
                cancelTransaction();
                return;
            }

            // Process the payment through the payment strategy
            boolean paymentSuccess = context.getPaymentStrategy().processPayment(
                context.getCurrentTransaction()
            );

            if (paymentSuccess) {
                context.getCurrentTransaction().setStatus(TransactionStatus.COMPLETED);
                context.setCurrentState(new DispensingState(context));
                System.out.println("Payment processed successfully. Dispensing product...");
                dispenseProduct();
            } else {
                context.getCurrentTransaction().setStatus(TransactionStatus.FAILED);
                System.out.println("Payment processing failed.");
                cancelTransaction();
            }
        } catch (Exception e) {
            context.getCurrentTransaction().setStatus(TransactionStatus.FAILED);
            System.err.println("Error processing payment: " + e.getMessage());
            cancelTransaction();
        }
    }

    @Override
    public void dispenseProduct() {
        // In payment state, dispensing is handled by processPayment when successful
        System.out.println("Processing payment first...");
    }

    @Override
    public void cancelTransaction() {
        System.out.println("Cancelling transaction and returning payment...");
        context.cancelCurrentTransaction();
        context.setCurrentState(new IdleState(context));
    }

    @Override
    public String getStateName() {
        return "PAYMENT";
    }
}
