package org.example.states;

import org.example.system.VendingMachineContext;
import org.example.model.Transaction;
import org.example.enums.TransactionStatus;
import org.example.interfaces.PaymentStrategy;

/**
 * PaymentState represents the state when payment is being processed.
 * In this state, the payment strategy validates and processes the payment.
 * Valid transitions: PaymentState -> DispensingState or PaymentState -> IdleState (on failure)
 */
public class PaymentState extends AbstractState {

    public PaymentState(VendingMachineContext context) {
        super(context);
    }

    @Override
    public void processPayment() {
        Transaction transaction = context.getCurrentTransaction();
        if (transaction == null) {
            System.out.println("ERROR: No active transaction.");
            context.setCurrentState(new IdleState(context));
            return;
        }

        System.out.println("[PAYMENT] Processing payment using " + 
                         transaction.getPaymentMethod() + "...");
        
        transaction.setStatus(TransactionStatus.PROCESSING);
        
        // Get payment strategy and process payment
        PaymentStrategy paymentStrategy = context.getPaymentStrategy();
        boolean paymentSuccessful = paymentStrategy.processPayment(transaction);
        
        if (paymentSuccessful) {
            System.out.println("[PAYMENT] Payment successful!");
            transaction.setStatus(TransactionStatus.COMPLETED);
            
            // Notify observers
            context.notifyObservers("PAYMENT_PROCESSED", 
                                  transaction.getProductPrice(), 
                                  paymentStrategy.getPaymentMethod());
            
            // Transition to DispensingState
            context.setCurrentState(new DispensingState(context));
            context.getCurrentState().dispenseProduct();
        } else {
            System.out.println("[PAYMENT] Payment failed! Refunding...");
            transaction.setStatus(TransactionStatus.FAILED);
            
            // Return money if cash payment
            if (transaction.needsRefund()) {
                context.returnChange(transaction.getTotalInsertedAmount());
            }
            
            // Notify observers
            context.notifyObservers("TRANSACTION_FAILED", 
                                  transaction.getTransactionId(), 
                                  "Payment processing failed");
            
            context.cancelCurrentTransaction();
            context.setCurrentState(new IdleState(context));
        }
    }

    @Override
    public void cancelTransaction() {
        System.out.println("\n[PAYMENT] Cancelling transaction during payment...");
        
        Transaction transaction = context.getCurrentTransaction();
        if (transaction != null && transaction.needsRefund()) {
            context.returnChange(transaction.getTotalInsertedAmount());
        }
        
        context.cancelCurrentTransaction();
        context.setCurrentState(new IdleState(context));
        System.out.println("Transaction cancelled. Returning to idle state.");
    }

    @Override
    public String getStateName() {
        return "PAYMENT";
    }
}

