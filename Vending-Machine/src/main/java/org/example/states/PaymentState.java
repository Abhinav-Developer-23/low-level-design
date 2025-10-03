package org.example.states;

import org.example.interfaces.State;
import org.example.model.Product;
import org.example.system.VendingMachineContext;

/**
 * Payment State: Payment received, ready to dispense
 * Valid operations: dispenseProduct
 * Invalid operations: selectProduct, insertCoin, insertCard, cancel
 */
public class PaymentState implements State {
    private final VendingMachineContext context;

    public PaymentState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void selectProduct(Product product) {
        System.out.println("❌ Transaction in progress. Please wait.");
    }

    @Override
    public void insertCoin(double amount) {
        System.out.println("❌ Payment already received");
    }

    @Override
    public void insertCard(String cardNumber, double amount) {
        System.out.println("❌ Payment already received");
    }

    @Override
    public void insertMobilePayment(String paymentId, double amount) {
        System.out.println("❌ Payment already received");
    }

    @Override
    public void dispenseProduct() {
        // Valid operation - transition to Dispensing state
        System.out.println("✓ Payment verified. Preparing to dispense...");
        context.setState(new DispensingState(context));
        
        // Delegate to dispensing state
        context.dispenseProduct();
    }

    @Override
    public void cancel() {
        System.out.println("❌ Cannot cancel - payment already processed. Dispensing in progress.");
    }

    @Override
    public String getStateName() {
        return "PAYMENT";
    }
}

