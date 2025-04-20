package org.lowLevelDesign.LowLevelDesign.ParkingLotAryan;

public class CashPayment implements PaymentStrategy {
  @Override
  public void processPayment(double amount) {
    System.out.println("Processing cash payment of $" + amount);
    // Logic for cash payment processing
  }
} 