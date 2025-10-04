package org.example.strategies;

import org.example.interfaces.Payment;

public class UpiPayment implements Payment {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " using UPI.");
        return true;
    }
}

