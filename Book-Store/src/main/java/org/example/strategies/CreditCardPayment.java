package org.example.strategies;

import org.example.interfaces.Payment;

public class CreditCardPayment implements Payment {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card.");
        return true;
    }
}

