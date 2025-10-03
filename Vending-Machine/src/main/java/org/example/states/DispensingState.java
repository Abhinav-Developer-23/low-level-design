package org.example.states;

import org.example.enums.TransactionStatus;
import org.example.interfaces.State;
import org.example.model.Product;
import org.example.model.Transaction;
import org.example.system.VendingMachineContext;

/**
 * Dispensing State: Actively dispensing product
 * Valid operations: dispenseProduct (actual dispensing)
 * Invalid operations: all other operations (must wait)
 */
public class DispensingState implements State {
    private final VendingMachineContext context;

    public DispensingState(VendingMachineContext context) {
        this.context = context;
    }

    @Override
    public void selectProduct(Product product) {
        System.out.println("❌ Please wait - dispensing in progress");
    }

    @Override
    public void insertCoin(double amount) {
        System.out.println("❌ Please wait - dispensing in progress");
    }

    @Override
    public void insertCard(String cardNumber, double amount) {
        System.out.println("❌ Please wait - dispensing in progress");
    }

    @Override
    public void insertMobilePayment(String paymentId, double amount) {
        System.out.println("❌ Please wait - dispensing in progress");
    }

    @Override
    public void dispenseProduct() {
        Product product = context.getSelectedProduct();
        double totalPaid = context.getTotalPaid();
        double price = product.getPrice();
        double change = totalPaid - price;
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🎁 DISPENSING: " + product.getName());
        System.out.println("=".repeat(50));
        
        // Simulate dispensing
        try {
            Thread.sleep(500); // Simulate mechanical dispensing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Decrement inventory
        if (context.getInventory().decrementStock(product.getProductId())) {
            System.out.println("✓ Product dispensed successfully!");
            
            // Return change if any
            if (change > 0) {
                System.out.println("💰 Change returned: $" + String.format("%.2f", change));
                context.notifyChangeReturned(change);
            }
            
            // Check if stock is low
            int remainingStock = context.getInventory().getStockCount(product.getProductId());
            if (context.getInventory().isLowStock(product.getProductId())) {
                context.notifyProductLowStock(product, remainingStock);
            }
            
            // Create transaction record
            Transaction transaction = context.createTransaction(
                    product, totalPaid, change, TransactionStatus.COMPLETED);
            
            context.notifyProductDispensed(product, transaction);
            
            System.out.println("=".repeat(50));
            System.out.println("Thank you for your purchase! 😊");
            System.out.println("=".repeat(50) + "\n");
        } else {
            System.out.println("❌ Error: Failed to dispense product");
            // Refund
            System.out.println("💰 Refunding: $" + String.format("%.2f", totalPaid));
            context.notifyTransactionCancelled(totalPaid);
        }
        
        // Reset and return to idle state
        context.resetTransaction();
        context.setState(new IdleState(context));
    }

    @Override
    public void cancel() {
        System.out.println("❌ Cannot cancel - dispensing in progress");
    }

    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}

