package org.lowLevelDesign.LowLevelDesign.ATMSystem.src.hardware;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Printer {
    private boolean hasPaper;
    private boolean hasInk;

    public Printer() {
        this.hasPaper = true;
        this.hasInk = true;
    }

    public boolean printReceipt(String transactionId, String accountNumber,
                                String transactionType, double amount) {
        if (!hasPaper || !hasInk) {
            return false;
        }

        System.out.println("\n=== Transaction Receipt ===");
        System.out.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Account: " + accountNumber);
        System.out.println("Type: " + transactionType);
        System.out.printf("Amount: $%.2f%n", amount);
        System.out.println("========================\n");

        return true;
    }

    public void refillPaper() {
        this.hasPaper = true;
    }

    public void refillInk() {
        this.hasInk = true;
    }

    public boolean hasPaper() {
        return hasPaper;
    }

    public boolean hasInk() {
        return hasInk;
    }
} 