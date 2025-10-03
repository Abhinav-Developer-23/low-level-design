package org.example.strategies.payment;

import org.example.enums.CoinType;
import org.example.interfaces.PaymentStrategy;

import java.util.*;

/**
 * Cash Payment Strategy: Handles coin-based payments.
 * Validates coin insertion and calculates change using greedy algorithm.
 */
public class CashPaymentStrategy implements PaymentStrategy {
    private final List<CoinType> availableCoins;
    private final Map<CoinType, Integer> coinInventory;

    public CashPaymentStrategy() {
        // Initialize with standard US coin denominations
        this.availableCoins = Arrays.asList(
            CoinType.DOLLAR,
            CoinType.HALF_DOLLAR,
            CoinType.QUARTER,
            CoinType.DIME,
            CoinType.NICKEL,
            CoinType.PENNY
        );

        // Initialize coin inventory (simulating coin hopper)
        this.coinInventory = new HashMap<>();
        for (CoinType coin : availableCoins) {
            coinInventory.put(coin, 100); // 100 coins of each type available
        }
    }

    @Override
    public boolean processPayment(int amount) {
        System.out.println("Processing cash payment for $" + String.format("%.2f", amount / 100.0));
        // Cash payment is always considered successful if amount > 0
        // In a real system, this would validate the inserted coins
        return amount > 0;
    }

    @Override
    public String getPaymentMethodName() {
        return "Cash";
    }

    @Override
    public boolean isAvailable() {
        return true; // Cash is always available
    }

    /**
     * Calculates change using greedy algorithm.
     * @param changeAmount Amount to return in cents
     * @return List of coins to return, or empty list if exact change not possible
     */
    public List<CoinType> calculateChange(int changeAmount) {
        List<CoinType> change = new ArrayList<>();
        int remaining = changeAmount;

        // Sort coins in descending order for greedy algorithm
        List<CoinType> sortedCoins = new ArrayList<>(availableCoins);
        sortedCoins.sort((a, b) -> Integer.compare(b.getValue(), a.getValue()));

        for (CoinType coin : sortedCoins) {
            int coinValue = coin.getValue();
            int availableCount = coinInventory.getOrDefault(coin, 0);

            while (remaining >= coinValue && availableCount > 0) {
                change.add(coin);
                remaining -= coinValue;
                availableCount--;
                coinInventory.put(coin, availableCount);
            }
        }

        // If we couldn't make exact change, return empty list
        if (remaining > 0) {
            // Restore coin inventory since change couldn't be made
            for (CoinType coin : change) {
                coinInventory.put(coin, coinInventory.get(coin) + 1);
            }
            return Collections.emptyList();
        }

        return change;
    }

    /**
     * Adds coins to the cash inventory.
     * @param coinType Type of coin to add
     * @param count Number of coins to add
     */
    public void addCoinsToInventory(CoinType coinType, int count) {
        coinInventory.put(coinType, coinInventory.getOrDefault(coinType, 0) + count);
    }

    /**
     * Gets the current coin inventory.
     * @return Map of coin types to their available counts
     */
    public Map<CoinType, Integer> getCoinInventory() {
        return new HashMap<>(coinInventory);
    }

    /**
     * Checks if exact change can be made for the given amount.
     * @param amount Amount in cents
     * @return true if exact change is possible
     */
    public boolean canMakeExactChange(int amount) {
        return !calculateChange(amount).isEmpty() || amount == 0;
    }
}
