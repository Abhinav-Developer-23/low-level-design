package org.lowLevelDesign.LowLevelDesign.ATMSystem.hardware;

public class Screen {
    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayAmount(double amount) {
        System.out.printf("Amount: $%.2f%n", amount);
    }

    public void displayError(String error) {
        System.err.println("Error: " + error);
    }
} 