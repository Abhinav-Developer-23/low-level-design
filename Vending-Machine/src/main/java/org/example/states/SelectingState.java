package org.example.states;

import org.example.enums.CoinType;
import org.example.interfaces.State;
import org.example.model.Coin;
import org.example.system.VendingMachineContext;

/**
 * Concrete state class representing the product selection state.
 * In this state, the user has selected a product and can insert coins or change selection.
 */
public class SelectingState implements State {
    private final VendingMachineContext context;

    public SelectingState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void selectProduct(String productId) {
        if (context.getInventory().isProductAvailable(productId)) {
            // Cancel current transaction and start new one
            if (context.getCurrentTransaction() != null) {
                context.cancelCurrentTransaction();
            }
            context.setCurrentTransaction(context.createTransaction(productId));
            System.out.println("Product changed to " + productId + ". Please proceed to payment.");
        } else {
            System.out.println("Product " + productId + " is not available.");
        }
    }

    @Override
    public void insertCoin(int coinValue) {
        // Convert coin value to CoinType for validation
        CoinType coinType = getCoinTypeFromValue(coinValue);
        if (coinType != null) {
            Coin coin = new Coin(coinType);
            context.getCurrentTransaction().addCoin(coin);
            System.out.println("Inserted: " + coin);

            // Check if payment is complete
            if (context.getCurrentTransaction().isPaymentComplete()) {
                context.setCurrentState(new PaymentState(context));
                System.out.println("Payment complete. Processing transaction...");
                processPayment();
            } else {
                int remaining = context.getCurrentTransaction().getProductPrice() -
                              context.getCurrentTransaction().getTotalInsertedAmount();
                System.out.println("Please insert " + remaining + " more cents.");
            }
        } else {
            System.out.println("Invalid coin value: " + coinValue + " cents");
        }
    }

    @Override
    public void processPayment() {
        if (context.getCurrentTransaction().isPaymentComplete()) {
            context.setCurrentState(new PaymentState(context));
            context.getCurrentState().processPayment();
        } else {
            System.out.println("Payment not complete. Please insert more coins.");
        }
    }

    @Override
    public void dispenseProduct() {
        System.out.println("Payment required before dispensing product.");
    }

    @Override
    public void cancelTransaction() {
        System.out.println("Transaction cancelled. Returning coins...");
        context.cancelCurrentTransaction();
        context.setCurrentState(new IdleState(context));
    }

    @Override
    public String getStateName() {
        return "SELECTING";
    }

    private CoinType getCoinTypeFromValue(int value) {
        for (CoinType type : CoinType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }
}
