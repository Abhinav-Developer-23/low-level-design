package org.lowLevelDesign.LowLevelDesign.ParkingLotAryan;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Demonstrates the usage of the flexible parking lot builder.
public class ParkingLotMain {
  public static void main(String[] args) {
    // Create a parking lot with multiple floors and varied spot configurations
    ParkingLot parkingLot =
        new ParkingLotBuilder()
            // First floor: 2 car spots, 2 bike spots
            .createFloor(1, 2, 2)
            // Second floor: 3 car spots, 1 bike spot, 1 other vehicle spot
            .createFloor(2, 3, 1, 1)
            .build();
    
    // Initialize fee strategies
    ParkingFeeStrategy basicHourlyRateStrategy = new BasicHourlyRateStrategy();
    ParkingFeeStrategy premiumRateStrategy = new PremiumRateStrategy();
    
    // Create vehicles using Factory Pattern with fee strategies
    Vehicle car1 = VehicleFactory.createVehicle("Car", "CAR123", basicHourlyRateStrategy);
    Vehicle bike1 = VehicleFactory.createVehicle("Bike", "BIKE456", premiumRateStrategy);
    
    // Park vehicles
    ParkingSpot carSpot = parkingLot.parkVehicle(car1);
    ParkingSpot bikeSpot = parkingLot.parkVehicle(bike1);
    
    Scanner scanner = new Scanner(System.in);
    System.out.println("Select payment method for your vehicle:");
    System.out.println("1. Credit Card");
    System.out.println("2. Cash");
    int paymentMethod = scanner.nextInt();
    
    // Process payments using Strategy Patterns
    if (carSpot != null) {
      // Calculate fee using the specific strategy for the vehicle
      double carFee = car1.calculateFee(2, DurationType.HOURS);
      PaymentStrategy carPaymentStrategy = getPaymentStrategy(paymentMethod, carFee);
      carPaymentStrategy.processPayment(carFee);
      parkingLot.vacateSpot(carSpot, car1);
    }
    
    if (bikeSpot != null) {
      // Calculate fee using the specific strategy for the vehicle
      double bikeFee = bike1.calculateFee(3, DurationType.HOURS);
      PaymentStrategy bikePaymentStrategy = getPaymentStrategy(paymentMethod, bikeFee);
      bikePaymentStrategy.processPayment(bikeFee);
      parkingLot.vacateSpot(bikeSpot, bike1);
    }
    
    scanner.close();
  }
  
  private static PaymentStrategy getPaymentStrategy(int paymentMethod, double fee) {
    switch (paymentMethod) {
      case 1:
        return new CreditCardPayment();
      case 2:
        return new CashPayment();
      default:
        System.out.println("Invalid choice! Default to Credit card payment.");
        return new CreditCardPayment();
    }
  }
} 