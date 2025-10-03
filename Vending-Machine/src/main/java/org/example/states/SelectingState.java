package org.example.states;

import org.example.system.VendingMachineContext;
import org.example.model.Coin;
import org.example.model.Transaction;
import org.example.enums.CoinType;
import org.example.enums.PaymentMethod;

/**
 * SelectingState represents the state after a product has been selected.
 * In this state, the machine accepts coins or processes non-cash payments.
 * Valid transitions: SelectingState -> PaymentState or SelectingState -> IdleState (on cancel)
 */
public class SelectingState extends AbstractState {

    public SelectingState(VendingMachineContext context) {
        super(context);
    }

    @Override
    public void insertCoin(int coinValue) {
        Transaction transaction = context.getCurrentTransaction();
        if (transaction == null) {
            System.out.println("ERROR: No active transaction.");
            context.setCurrentState(new IdleState(context));
            return;
        }

        // Validate coin
        CoinType coinType = CoinType.fromValue(coinValue);
        if (coinType == null) {
            System.out.println("ERROR: Invalid coin value: " + coinValue + " cents");
            return;
        }

        // Add coin to transaction
        Coin coin = new Coin(coinType);
        transaction.addCoin(coin);
        
        System.out.println("[SELECTING] Inserted: " + coin.getType().name() + 
                         " (" + coinValue + " cents)");
        System.out.println("Total inserted: $" + 
                         String.format("%.2f", transaction.getTotalInsertedAmount() / 100.0));
        
        // Notify observers
        context.notifyObservers("COIN_INSERTED", coinValue);
        
        // Check if payment is complete
        if (transaction.isPaymentComplete()) {
            System.out.println("Payment complete! Remaining: $" + 
                             String.format("%.2f", transaction.getChangeAmount() / 100.0) + " change");
        } else {
            System.out.println("Remaining: $" + 
                             String.format("%.2f", transaction.getRemainingAmount() / 100.0));
        }
    }

    @Override
    public void processPayment() {
        Transaction transaction = context.getCurrentTransaction();
        if (transaction == null) {
            System.out.println("ERROR: No active transaction.");
            context.setCurrentState(new IdleState(context));
            return;
        }

        System.out.println("\n[SELECTING] Processing payment...");
        
        // For cash payments, check if sufficient coins inserted
        if (transaction.getPaymentMethod() == PaymentMethod.CASH) {
            if (!transaction.isPaymentComplete()) {
                System.out.println("ERROR: Insufficient payment. Need $" + 
                                 String.format("%.2f", transaction.getRemainingAmount() / 100.0) + " more.");
                return;
            }
        }
        
        // Transition to PaymentState
        context.setCurrentState(new PaymentState(context));
        context.getCurrentState().processPayment();
    }

    @Override
    public void cancelTransaction() {
        System.out.println("\n[SELECTING] Cancelling transaction...");
        
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
        return "SELECTING";
    }
}

