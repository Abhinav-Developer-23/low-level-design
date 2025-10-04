package org.example.strategies;

import org.example.interfaces.Payment;

public class NetBankingPayment implements Payment {
    @Override
    public boolean pay(double amount) {
        System.out.println("Paid " + amount + " using Net Banking.");
        return true;
    }
}

