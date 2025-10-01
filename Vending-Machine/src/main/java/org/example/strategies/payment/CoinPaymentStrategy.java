package org.example.strategies.payment;

import org.example.enums.TransactionStatus;
import org.example.interfaces.PaymentStrategy;
import org.example.model.Coin;
import org.example.model.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy Pattern: Coin-based payment processing
 * Handles coin validation, change calculation, and refunds
 */
public class CoinPaymentStrategy implements PaymentStrategy {

    @Override
    public boolean processPayment(Transaction transaction) {
        double requiredAmount = transaction.getRequiredAmount();
        double paidAmount = transaction.getAmountPaid();

        if (paidAmount >= requiredAmount) {
            transaction.setStatus(TransactionStatus.COMPLETED);

            // Calculate and set change
            double changeAmount = paidAmount - requiredAmount;
            if (changeAmount > 0) {
                List<Coin> changeCoins = calculateChange(changeAmount);
                transaction.setChangeCoins(changeCoins);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean processRefund(Transaction transaction) {
        if (transaction.getAmountPaid() > 0) {
            transaction.setStatus(TransactionStatus.REFUNDED);
            // Return all inserted coins as change
            transaction.setChangeCoins(new ArrayList<>(transaction.getInsertedCoins()));
            return true;
        }
        return false;
    }

    /**
     * Calculate change using greedy algorithm with available coin denominations
     */
    private List<Coin> calculateChange(double changeAmount) {
        List<Coin> changeCoins = new ArrayList<>();
        int remainingCents = (int) (changeAmount * 100); // Convert to cents

        // Available coin denominations in cents (sorted descending)
        int[] denominations = {25, 10, 5, 1};

        for (int denom : denominations) {
            while (remainingCents >= denom) {
                // Create appropriate coin type
                switch (denom) {
                    case 25:
                        changeCoins.add(new Coin(org.example.enums.CoinType.TWENTY_FIVE));
                        break;
                    case 10:
                        changeCoins.add(new Coin(org.example.enums.CoinType.TEN));
                        break;
                    case 5:
                        changeCoins.add(new Coin(org.example.enums.CoinType.FIVE));
                        break;
                    case 1:
                        changeCoins.add(new Coin(org.example.enums.CoinType.ONE));
                        break;
                }
                remainingCents -= denom;
            }
        }

        return changeCoins;
    }
}
