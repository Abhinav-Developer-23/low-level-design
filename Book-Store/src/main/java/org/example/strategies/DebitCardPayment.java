package org.example.strategies;

import org.example.interfaces.Payment;

public class DebitCardPayment implements Payment {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " using Debit Card.");
        return true;
    }
}

