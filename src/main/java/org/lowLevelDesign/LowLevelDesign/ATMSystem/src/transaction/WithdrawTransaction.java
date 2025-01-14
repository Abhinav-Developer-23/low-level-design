package org.lowLevelDesign.LowLevelDesign.ATMSystem.src.transaction;

import org.lowLevelDesign.LowLevelDesign.ATMSystem.src.ATM;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.src.enums.TransactionType;
import org.lowLevelDesign.LowLevelDesign.ATMSystem.src.model.Account;

public class WithdrawTransaction extends Transaction {
    private double amount;

    public WithdrawTransaction(ATM atm, Account account, double amount) {
        super(atm, account, TransactionType.CASH_WITHDRAWAL);
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        if (account.withdraw(amount)) {
            return atm.getCashDispenser().dispenseCash(amount);
        }
        return false;
    }
} 