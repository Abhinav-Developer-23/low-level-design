package org.example.states;

import org.example.system.VendingMachineContext;

/**
 * OutOfServiceState represents the state when the machine is under maintenance.
 * In this state, no operations are allowed except returning to service.
 * Valid transitions: OutOfServiceState -> IdleState (when service is complete)
 */
public class OutOfServiceState extends AbstractState {

    public OutOfServiceState(VendingMachineContext context) {
        super(context);
    }

    @Override
    public void selectProduct(String productId) {
        System.out.println("[OUT OF SERVICE] Machine is currently under maintenance.");
        System.out.println("Please try again later.");
    }

    @Override
    public void insertCoin(int coinValue) {
        System.out.println("[OUT OF SERVICE] Machine is currently under maintenance.");
        System.out.println("Cannot accept coins at this time.");
    }

    @Override
    public void processPayment() {
        System.out.println("[OUT OF SERVICE] Machine is currently under maintenance.");
        System.out.println("Cannot process payments at this time.");
    }

    @Override
    public void dispenseProduct() {
        System.out.println("[OUT OF SERVICE] Machine is currently under maintenance.");
        System.out.println("Cannot dispense products at this time.");
    }

    @Override
    public void cancelTransaction() {
        System.out.println("[OUT OF SERVICE] Machine is currently under maintenance.");
        System.out.println("No active transactions to cancel.");
    }

    /**
     * Returns the machine to service (transitions to IdleState).
     */
    public void returnToService() {
        System.out.println("\n[OUT OF SERVICE] Maintenance complete. Returning to service...");
        context.setOperational(true);
        context.setCurrentState(new IdleState(context));
        System.out.println("Machine is now operational.\n");
    }

    @Override
    public String getStateName() {
        return "OUT_OF_SERVICE";
    }
}

