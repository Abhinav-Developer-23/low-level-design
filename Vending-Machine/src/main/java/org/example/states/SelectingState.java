package org.example.states;

import org.example.enums.PaymentMethod;
import org.example.interfaces.State;
import org.example.model.Product;
import org.example.system.VendingMachineContext;

/**
 * Selecting State: Product selected, waiting for payment
 * Valid operations: insertCoin, insertCard, insertMobilePayment, cancel
 * Invalid operations: selectProduct (must cancel first), dispenseProduct
 */
public class SelectingState implements State {
    private final VendingMachineContext context;

    public SelectingState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void selectProduct(Product product) {
        System.out.println("❌ Product already selected. Please complete payment or cancel transaction.");
    }

    @Override
    public void insertCoin(double amount) {
        if (amount <= 0) {
            System.out.println("❌ Invalid amount");
            return;
        }

        // Add to total paid
        context.addPayment(amount);
        context.setPaymentMethod(PaymentMethod.CASH);
        
        double totalPaid = context.getTotalPaid();
        double price = context.getSelectedProduct().getPrice();
        
        System.out.println("✓ Inserted: $" + String.format("%.2f", amount) + 
                          " | Total: $" + String.format("%.2f", totalPaid));
        
        context.notifyPaymentReceived(amount, "CASH");
        
        if (totalPaid >= price) {
            // Payment complete, transition to Payment state
            System.out.println("✓ Payment complete!");
            context.setState(new PaymentState(context));
            
            // Automatically proceed to dispensing
            context.dispenseProduct();
        } else {
            double remaining = price - totalPaid;
            System.out.println("💡 Please insert $" + String.format("%.2f", remaining) + " more");
        }
    }

    @Override
    public void insertCard(String cardNumber, double amount) {
        Product product = context.getSelectedProduct();
        double price = product.getPrice();
        
        if (amount < price) {
            System.out.println("❌ Insufficient amount. Required: $" + String.format("%.2f", price));
            return;
        }
        
        System.out.println("✓ Processing card payment: $" + String.format("%.2f", amount));
        
        // Process card payment through strategy
        if (context.processCardPayment(cardNumber, amount)) {
            context.setTotalPaid(amount);
            context.setPaymentMethod(PaymentMethod.CARD);
            context.notifyPaymentReceived(amount, "CARD");
            
            System.out.println("✓ Card payment successful!");
            
            // Transition to Payment state
            context.setState(new PaymentState(context));
            
            // Automatically proceed to dispensing
            context.dispenseProduct();
        } else {
            System.out.println("❌ Card payment failed. Please try again.");
        }
    }

    @Override
    public void insertMobilePayment(String paymentId, double amount) {
        Product product = context.getSelectedProduct();
        double price = product.getPrice();
        
        if (amount < price) {
            System.out.println("❌ Insufficient amount. Required: $" + String.format("%.2f", price));
            return;
        }
        
        System.out.println("✓ Processing mobile payment: $" + String.format("%.2f", amount));
        
        // Process mobile payment through strategy
        if (context.processMobilePayment(paymentId, amount)) {
            context.setTotalPaid(amount);
            context.setPaymentMethod(PaymentMethod.MOBILE);
            context.notifyPaymentReceived(amount, "MOBILE");
            
            System.out.println("✓ Mobile payment successful!");
            
            // Transition to Payment state
            context.setState(new PaymentState(context));
            
            // Automatically proceed to dispensing
            context.dispenseProduct();
        } else {
            System.out.println("❌ Mobile payment failed. Please try again.");
        }
    }

    @Override
    public void dispenseProduct() {
        System.out.println("❌ Please complete payment first");
    }

    @Override
    public void cancel() {
        double refund = context.getTotalPaid();
        
        if (refund > 0) {
            System.out.println("✓ Transaction cancelled. Refunding: $" + String.format("%.2f", refund));
            context.notifyTransactionCancelled(refund);
        } else {
            System.out.println("✓ Transaction cancelled");
        }
        
        // Reset and return to idle
        context.resetTransaction();
        context.setState(new IdleState(context));
    }

    @Override
    public String getStateName() {
        return "SELECTING";
    }
}

