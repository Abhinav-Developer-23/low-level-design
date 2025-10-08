package org.example;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// ========================================================================================
// ATM SYSTEM - LOW LEVEL DESIGN
// ========================================================================================
/**
 * COMPREHENSIVE ATM SYSTEM IMPLEMENTATION
 * 
 * This is a complete Low-Level Design (LLD) implementation of an ATM system featuring:
 * - Card authentication with PIN verification
 * - Core banking operations (withdrawal, deposit, balance inquiry, transfer)
 * - Transaction tracking with timestamps and statuses
 * - Security features (PIN lockout after 3 failed attempts)
 * - Business rules enforcement (daily limits, per-transaction limits, minimum balance)
 * - ATM cash inventory management with denomination tracking
 * - Admin functions for cash refill and system monitoring
 * 
 * ========================================
 * DESIGN PATTERNS IMPLEMENTED
 * ========================================
 * 
 * 1. STATE PATTERN (Primary Pattern)
 *    - ATMState interface with three concrete states:
 *      * IdleState: No card inserted
 *      * CardInsertedState: Waiting for PIN authentication
 *      * AuthenticatedState: User authenticated, can perform transactions
 *    - Each state defines specific behavior for all ATM operations
 *    - State transitions ensure proper flow control
 * 
 * 2. STRATEGY PATTERN
 *    - AuthenticationStrategy: PIN-based authentication (extensible for biometric, OTP, etc.)
 *    - CashDispenseStrategy: Greedy algorithm for denomination distribution
 *    - TransactionValidator: Pluggable validation strategies for different transaction types
 * 
 * 3. OBSERVER PATTERN
 *    - NotificationObserver interface for transaction notifications
 *    - SMSNotificationObserver and EmailNotificationObserver implementations
 *    - BankAccount notifies observers on successful transactions
 * 
 * 4. SINGLETON PATTERN
 *    - ATMSystem class ensures single system-wide controller
 * 
 * 5. TEMPLATE METHOD PATTERN
 *    - AbstractTransactionValidator provides common validation logic
 *    - Specific validators (WithdrawalValidator, TransferValidator) override specific rules
 * 
 * 6. FACTORY PATTERN
 *    - BankAccountFactory creates different types of bank accounts
 *    - Encapsulates object creation logic
 *    - Allows easy addition of new account types
 * 
 * ========================================
 * SOLID PRINCIPLES DEMONSTRATED
 * ========================================
 * 
 * 1. SINGLE RESPONSIBILITY PRINCIPLE (SRP)
 *    - Bank: Manages accounts only
 *    - ATMMachine: Handles ATM-specific operations
 *    - Transaction: Represents transaction data
 *    - Each validator handles one type of validation
 * 
 * 2. OPEN/CLOSED PRINCIPLE (OCP)
 *    - Abstract BankAccount can be extended (SavingsAccount, CurrentAccount)
 *    - Strategy interfaces allow new implementations without modifying existing code
 *    - New ATM states can be added without changing existing states
 * 
 * 3. LISKOV SUBSTITUTION PRINCIPLE (LSP)
 *    - SavingsAccount and CurrentAccount can substitute BankAccount
 *    - All concrete states can substitute ATMState
 *    - All strategies can substitute their respective interfaces
 * 
 * 4. INTERFACE SEGREGATION PRINCIPLE (ISP)
 *    - Focused interfaces: ATMState, AuthenticationStrategy, CashDispenseStrategy
 *    - Clients depend only on methods they use
 * 
 * 5. DEPENDENCY INVERSION PRINCIPLE (DIP)
 *    - High-level ATMMachine depends on ATMState abstraction
 *    - AuthenticatedState depends on TransactionValidator interface
 *    - Dependencies on abstractions, not concrete implementations
 * 
 * ========================================
 * OOP CONCEPTS DEMONSTRATED
 * ========================================
 * 
 * 1. ABSTRACTION
 *    - Abstract BankAccount class
 *    - Abstract User class
 *    - Abstract AbstractTransactionValidator
 *    - Multiple interfaces defining contracts
 * 
 * 2. ENCAPSULATION
 *    - Private fields with controlled public access
 *    - Synchronized methods for thread-safe operations
 *    - Validation logic encapsulated in validators
 * 
 * 3. INHERITANCE
 *    - SavingsAccount extends BankAccount
 *    - CurrentAccount extends BankAccount
 *    - AdminUser extends User
 *    - Validators extend AbstractTransactionValidator
 * 
 * 4. POLYMORPHISM
 *    - Different account types with different limits and behaviors
 *    - State-specific behavior through ATMState implementations
 *    - Strategy-specific algorithms through Strategy implementations
 * 
 * ========================================
 * KEY FEATURES & BUSINESS RULES
 * ========================================
 * 
 * 1. SECURITY
 *    - PIN authentication required for all transactions
 *    - Account lockout after 3 consecutive failed PIN attempts
 *    - Card expiry validation
 *    - Card blocking capability
 * 
 * 2. TRANSACTION LIMITS
 *    - Daily withdrawal limit: Savings $5000, Current $10000
 *    - Per-transaction limit: $2000 for withdrawals
 *    - Transfer limit: $5000 per transaction
 *    - Minimum balance: Savings $100, Current $0
 * 
 * 3. CASH MANAGEMENT
 *    - Multi-denomination support ($100, $50, $20, $10, $5, $1)
 *    - Greedy algorithm for optimal cash dispensing
 *    - Real-time inventory tracking
 *    - Admin-controlled cash refill
 * 
 * 4. TRANSACTION TRACKING
 *    - Unique transaction IDs (UUID)
 *    - Timestamps for all transactions
 *    - Status tracking (SUCCESS, FAILED, PENDING, CANCELLED)
 *    - Complete transaction history per account
 * 
 * 5. NOTIFICATIONS
 *    - Real-time SMS notifications
 *    - Observer pattern allows easy addition of new notification channels
 * 
 * ========================================
 * THREAD SAFETY
 * ========================================
 * 
 * - ConcurrentHashMap for account storage
 * - Synchronized debit/credit operations
 * - Thread-safe transaction processing
 * 
 * ========================================
 * EXTENSIBILITY
 * ========================================
 * 
 * The design supports easy extension:
 * - New account types using Factory Pattern (e.g., FixedDepositAccount demonstrated)
 * - New authentication methods (biometric, OTP) via AuthenticationStrategy
 * - New transaction types (bill payment, etc.) by extending State
 * - New notification channels (push, app notifications) via NotificationObserver
 * - New cash dispensing algorithms via CashDispenseStrategy
 * - New validation rules by extending AbstractTransactionValidator
 * 
 * Factory Pattern Benefits:
 * - Centralized account creation logic
 * - Easy to add new account types without modifying client code
 * - Consistent initialization of account parameters
 * - Supports both type-based and convenience factory methods
 * 
 * @author Low-Level Design Implementation
 * @version 1.0
 */

// ========================================================================================
// MAIN CLASS - Entry Point with Demo
// ========================================================================================

/**
 * Main class demonstrating the ATM System functionality
 * Runs comprehensive demos covering all features and edge cases
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== ATM System Demo ===\n");
        
        // Initialize the ATM System
        ATMSystem atmSystem = ATMSystem.getInstance();
        
        // Create bank and accounts
        Bank bank = new Bank("National Bank");
        
        // Create accounts using Factory Pattern - demonstrates Factory Pattern for object creation
        BankAccount account1 = BankAccountFactory.createSavingsAccount("ACC001", "1234567890", "1234", 5000.0);
        BankAccount account2 = BankAccountFactory.createCurrentAccount("ACC002", "0987654321", "5678", 10000.0);
        BankAccount account3 = BankAccountFactory.createSavingsAccount("ACC003", "1111111111", "9999", 500.0);
        BankAccount account4 = BankAccountFactory.createFixedDepositAccount("ACC004", "2222222222", "4444", 5000.0); // Fixed deposit account
        
        bank.addAccount(account1);
        bank.addAccount(account2);
        bank.addAccount(account3);
        bank.addAccount(account4);
        
        // Create ATM machines
        ATMMachine atm1 = new ATMMachine("ATM001", "Main Street", bank);
        ATMMachine atm2 = new ATMMachine("ATM002", "Central Plaza", bank);
        
        // Admin refills ATM cash
        AdminUser admin = new AdminUser("ADMIN001", "Admin John");
        System.out.println("--- Admin Refilling ATM ---");
        admin.refillCash(atm1, Denomination.HUNDRED, 100);
        admin.refillCash(atm1, Denomination.FIFTY, 50);
        admin.refillCash(atm1, Denomination.TWENTY, 100);
        admin.refillCash(atm1, Denomination.TEN, 50);
        
        System.out.println("\n--- ATM Status After Refill ---");
        admin.viewATMStatus(atm1);
        
        // Demo 1: Successful balance inquiry
        System.out.println("\n=== Demo 1: Balance Inquiry ===");
        Card card1 = new Card("1234567890", "ACC001", CardType.DEBIT, LocalDateTime.now().plusYears(2));
        atmSystem.insertCard(atm1, card1);
        atmSystem.enterPIN(atm1, "1234");
        atmSystem.checkBalance(atm1);
        atmSystem.ejectCard(atm1);
        
        // Demo 2: Successful withdrawal
        System.out.println("\n=== Demo 2: Cash Withdrawal ===");
        atmSystem.insertCard(atm1, card1);
        atmSystem.enterPIN(atm1, "1234");
        atmSystem.withdrawCash(atm1, 230.0);
        atmSystem.ejectCard(atm1);
        
        // Demo 3: Deposit money
        System.out.println("\n=== Demo 3: Cash Deposit ===");
        atmSystem.insertCard(atm1, card1);
        atmSystem.enterPIN(atm1, "1234");
        atmSystem.depositCash(atm1, 1000.0);
        atmSystem.ejectCard(atm1);
        
        // Demo 4: View transaction history
        System.out.println("\n=== Demo 4: Transaction History ===");
        atmSystem.insertCard(atm1, card1);
        atmSystem.enterPIN(atm1, "1234");
        atmSystem.viewTransactionHistory(atm1);
        atmSystem.ejectCard(atm1);
        
        // Demo 5: Insufficient funds
        System.out.println("\n=== Demo 5: Insufficient Funds ===");
        Card card3 = new Card("1111111111", "ACC003", CardType.DEBIT, LocalDateTime.now().plusYears(2));
        atmSystem.insertCard(atm1, card3);
        atmSystem.enterPIN(atm1, "9999");
        atmSystem.withdrawCash(atm1, 1000.0); // Should fail
        atmSystem.ejectCard(atm1);
        
        // Demo 6: Daily limit exceeded
        System.out.println("\n=== Demo 6: Daily Limit Test ===");
        atmSystem.insertCard(atm1, card1);
        atmSystem.enterPIN(atm1, "1234");
        atmSystem.withdrawCash(atm1, 5000.0); // Large withdrawal
        atmSystem.withdrawCash(atm1, 5000.0); // Should exceed daily limit
        atmSystem.ejectCard(atm1);
        
        // Demo 7: Wrong PIN attempts (account lockout)
        System.out.println("\n=== Demo 7: PIN Lockout Test ===");
        Card card2 = new Card("0987654321", "ACC002", CardType.DEBIT, LocalDateTime.now().plusYears(2));
        
        // Refill ATM2 for testing
        admin.refillCash(atm2, Denomination.HUNDRED, 50);
        
        atmSystem.insertCard(atm2, card2);
        atmSystem.enterPIN(atm2, "0000"); // Wrong PIN 1
        atmSystem.insertCard(atm2, card2);
        atmSystem.enterPIN(atm2, "1111"); // Wrong PIN 2
        atmSystem.insertCard(atm2, card2);
        atmSystem.enterPIN(atm2, "2222"); // Wrong PIN 3 - Should lock account
        atmSystem.insertCard(atm2, card2);
        atmSystem.enterPIN(atm2, "5678"); // Correct PIN but account locked
        
        // Demo 8: Expired card
        System.out.println("\n=== Demo 8: Expired Card Test ===");
        Card expiredCard = new Card("1234567890", "ACC001", CardType.DEBIT, LocalDateTime.now().minusDays(1));
        atmSystem.insertCard(atm1, expiredCard);
        
        // Demo 9: ATM cash inventory check
        System.out.println("\n=== Demo 9: Final ATM Status ===");
        admin.viewATMStatus(atm1);
        admin.viewTransactionLogs(atm1);
        
        // Demo 10: Transfer between accounts (bonus feature)
        System.out.println("\n=== Demo 10: Account Transfer ===");
        atmSystem.insertCard(atm1, card1);
        atmSystem.enterPIN(atm1, "1234");
        atmSystem.transferFunds(atm1, "ACC003", 500.0);
        atmSystem.ejectCard(atm1);
        
        // Demo 11: Fixed Deposit Account (created using Factory Pattern)
        System.out.println("\n=== Demo 11: Fixed Deposit Account Test ===");
        Card card4 = new Card("2222222222", "ACC004", CardType.DEBIT, LocalDateTime.now().plusYears(2));
        atmSystem.insertCard(atm1, card4);
        atmSystem.enterPIN(atm1, "4444");
        System.out.println("Testing Fixed Deposit Account with lower daily limit ($2000) and higher min balance ($1000)");
        atmSystem.checkBalance(atm1);
        atmSystem.withdrawCash(atm1, 500.0); // Should succeed
        atmSystem.withdrawCash(atm1, 4500.0); // Should fail - exceeds minimum balance
        atmSystem.ejectCard(atm1);
        
        System.out.println("\n=== ATM System Demo Complete ===");
    }
}

// ========================================================================================
// ENUMS
// ========================================================================================

/**
 * Enum representing different card types
 */
enum CardType {
    DEBIT, CREDIT, PREPAID
}

/**
 * Enum representing transaction types
 */
enum TransactionType {
    WITHDRAWAL, DEPOSIT, BALANCE_INQUIRY, TRANSFER, PIN_CHANGE
}

/**
 * Enum representing transaction status
 */
enum TransactionStatus {
    SUCCESS, FAILED, PENDING, CANCELLED
}

/**
 * Enum representing cash denominations
 */
enum Denomination {
    HUNDRED(100),
    FIFTY(50),
    TWENTY(20),
    TEN(10),
    FIVE(5),
    ONE(1);
    
    private final int value;
    
    Denomination(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}

/**
 * Enum representing account types
 */
enum AccountType {
    SAVINGS, CURRENT, FIXED_DEPOSIT
}

/**
 * Enum representing user roles
 */
enum UserRole {
    CUSTOMER, ADMIN, TECHNICIAN
}

// ========================================================================================
// FACTORY PATTERN - Account Creation
// ========================================================================================

/**
 * Factory class for creating different types of bank accounts
 * Implements Factory Pattern to encapsulate object creation logic
 * This makes the code more maintainable and follows Open/Closed Principle
 */
class BankAccountFactory {
    /**
     * Creates a bank account based on the account type
     * 
     * @param accountType Type of account to create
     * @param accountNumber Unique account number
     * @param cardNumber Card number linked to account
     * @param pin PIN for authentication
     * @param initialBalance Starting balance
     * @return BankAccount instance of appropriate type
     * @throws IllegalArgumentException if account type is not supported
     */
    public static BankAccount createAccount(AccountType accountType, String accountNumber, 
                                           String cardNumber, String pin, double initialBalance) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        
        switch (accountType) {
            case SAVINGS:
                return new SavingsAccount(accountNumber, cardNumber, pin, initialBalance);
            
            case CURRENT:
                return new CurrentAccount(accountNumber, cardNumber, pin, initialBalance);
            
            case FIXED_DEPOSIT:
                return new FixedDepositAccount(accountNumber, cardNumber, pin, initialBalance);
            
            default:
                throw new IllegalArgumentException("Unsupported account type: " + accountType);
        }
    }
    
    /**
     * Convenience method to create a savings account
     */
    public static BankAccount createSavingsAccount(String accountNumber, String cardNumber, 
                                                   String pin, double initialBalance) {
        return createAccount(AccountType.SAVINGS, accountNumber, cardNumber, pin, initialBalance);
    }
    
    /**
     * Convenience method to create a current account
     */
    public static BankAccount createCurrentAccount(String accountNumber, String cardNumber, 
                                                   String pin, double initialBalance) {
        return createAccount(AccountType.CURRENT, accountNumber, cardNumber, pin, initialBalance);
    }
    
    /**
     * Convenience method to create a fixed deposit account
     */
    public static BankAccount createFixedDepositAccount(String accountNumber, String cardNumber, 
                                                        String pin, double initialBalance) {
        return createAccount(AccountType.FIXED_DEPOSIT, accountNumber, cardNumber, pin, initialBalance);
    }
}

// ========================================================================================
// INTERFACES
// ========================================================================================

/**
 * Interface for ATM state behavior (State Pattern)
 * Defines all possible operations in different ATM states
 */
interface ATMState {
    void insertCard(ATMMachine atm, Card card);
    void ejectCard(ATMMachine atm);
    void enterPIN(ATMMachine atm, String pin);
    void withdraw(ATMMachine atm, double amount);
    void deposit(ATMMachine atm, double amount);
    void checkBalance(ATMMachine atm);
    void viewTransactionHistory(ATMMachine atm);
    void transferFunds(ATMMachine atm, String toAccountNumber, double amount);
}

/**
 * Interface for authentication strategy (Strategy Pattern)
 */
interface AuthenticationStrategy {
    boolean authenticate(Card card, String pin);
}

/**
 * Interface for cash dispensing strategy
 */
interface CashDispenseStrategy {
    Map<Denomination, Integer> dispenseCash(double amount, Map<Denomination, Integer> availableCash);
}

/**
 * Interface for transaction validation
 */
interface TransactionValidator {
    ValidationResult validate(Transaction transaction, BankAccount account);
}

/**
 * Interface for notification observer (Observer Pattern)
 */
interface NotificationObserver {
    void notify(String message, BankAccount account);
}

// ========================================================================================
// STATE PATTERN IMPLEMENTATIONS
// ========================================================================================

/**
 * State: No card inserted
 */
class IdleState implements ATMState {
    @Override
    public void insertCard(ATMMachine atm, Card card) {
        System.out.println("Card inserted: " + card.getCardNumber());
        
        // Validate card
        if (card.isExpired()) {
            System.out.println("ERROR: Card has expired!");
            atm.setState(new IdleState());
            return;
        }
        
        if (card.isBlocked()) {
            System.out.println("ERROR: Card is blocked!");
            atm.setState(new IdleState());
            return;
        }
        
        atm.setCurrentCard(card);
        atm.setState(new CardInsertedState());
        System.out.println("Please enter your PIN.");
    }
    
    @Override
    public void ejectCard(ATMMachine atm) {
        System.out.println("No card to eject.");
    }
    
    @Override
    public void enterPIN(ATMMachine atm, String pin) {
        System.out.println("ERROR: Please insert card first.");
    }
    
    @Override
    public void withdraw(ATMMachine atm, double amount) {
        System.out.println("ERROR: Please insert card and authenticate first.");
    }
    
    @Override
    public void deposit(ATMMachine atm, double amount) {
        System.out.println("ERROR: Please insert card and authenticate first.");
    }
    
    @Override
    public void checkBalance(ATMMachine atm) {
        System.out.println("ERROR: Please insert card and authenticate first.");
    }
    
    @Override
    public void viewTransactionHistory(ATMMachine atm) {
        System.out.println("ERROR: Please insert card and authenticate first.");
    }
    
    @Override
    public void transferFunds(ATMMachine atm, String toAccountNumber, double amount) {
        System.out.println("ERROR: Please insert card and authenticate first.");
    }
}

/**
 * State: Card inserted, waiting for PIN
 */
class CardInsertedState implements ATMState {
    private static final int MAX_ATTEMPTS = 3;
    
    @Override
    public void insertCard(ATMMachine atm, Card card) {
        System.out.println("ERROR: Card already inserted.");
    }
    
    @Override
    public void ejectCard(ATMMachine atm) {
        System.out.println("Card ejected.");
        atm.setCurrentCard(null);
        atm.setState(new IdleState());
    }
    
    @Override
    public void enterPIN(ATMMachine atm, String pin) {
        Card card = atm.getCurrentCard();
        BankAccount account = atm.getBank().getAccount(card.getAccountNumber());
        
        if (account == null) {
            System.out.println("ERROR: Account not found!");
            ejectCard(atm);
            return;
        }
        
        if (account.isLocked()) {
            System.out.println("ERROR: Account is locked due to multiple failed PIN attempts!");
            ejectCard(atm);
            return;
        }
        
        // Authenticate
        AuthenticationStrategy authStrategy = new PINAuthenticationStrategy(atm.getBank());
        if (authStrategy.authenticate(card, pin)) {
            System.out.println("Authentication successful!");
            account.resetFailedAttempts(); // Reset on successful authentication
            atm.setCurrentAccount(account);
            atm.setState(new AuthenticatedState());
        } else {
            account.incrementFailedAttempts();
            int currentAttempts = account.getFailedPinAttempts();
            System.out.println("ERROR: Invalid PIN! Attempt " + currentAttempts + " of " + MAX_ATTEMPTS);
            
            if (currentAttempts >= MAX_ATTEMPTS) {
                account.lock();
                System.out.println("ERROR: Account locked due to multiple failed attempts!");
            }
            ejectCard(atm);
        }
    }
    
    @Override
    public void withdraw(ATMMachine atm, double amount) {
        System.out.println("ERROR: Please authenticate first.");
    }
    
    @Override
    public void deposit(ATMMachine atm, double amount) {
        System.out.println("ERROR: Please authenticate first.");
    }
    
    @Override
    public void checkBalance(ATMMachine atm) {
        System.out.println("ERROR: Please authenticate first.");
    }
    
    @Override
    public void viewTransactionHistory(ATMMachine atm) {
        System.out.println("ERROR: Please authenticate first.");
    }
    
    @Override
    public void transferFunds(ATMMachine atm, String toAccountNumber, double amount) {
        System.out.println("ERROR: Please authenticate first.");
    }
}

/**
 * State: User authenticated, can perform transactions
 */
class AuthenticatedState implements ATMState {
    @Override
    public void insertCard(ATMMachine atm, Card card) {
        System.out.println("ERROR: Please complete current session first.");
    }
    
    @Override
    public void ejectCard(ATMMachine atm) {
        System.out.println("Thank you for using our ATM. Card ejected.");
        atm.setCurrentCard(null);
        atm.setCurrentAccount(null);
        atm.setState(new IdleState());
    }
    
    @Override
    public void enterPIN(ATMMachine atm, String pin) {
        System.out.println("ERROR: Already authenticated.");
    }
    
    @Override
    public void withdraw(ATMMachine atm, double amount) {
        BankAccount account = atm.getCurrentAccount();
        
        // Create transaction
        Transaction transaction = new Transaction(
            TransactionType.WITHDRAWAL,
            amount,
            account.getAccountNumber(),
            atm.getAtmId()
        );
        
        // Validate transaction
        TransactionValidator validator = new WithdrawalValidator();
        ValidationResult result = validator.validate(transaction, account);
        
        if (!result.isValid()) {
            transaction.setStatus(TransactionStatus.FAILED);
            account.addTransaction(transaction);
            System.out.println("WITHDRAWAL FAILED: " + result.getMessage());
            return;
        }
        
        // Check ATM cash availability
        CashDispenseStrategy dispenseStrategy = new GreedyCashDispenseStrategy();
        Map<Denomination, Integer> dispensed = dispenseStrategy.dispenseCash(amount, atm.getCashInventory());
        
        if (dispensed == null || dispensed.isEmpty()) {
            transaction.setStatus(TransactionStatus.FAILED);
            account.addTransaction(transaction);
            System.out.println("WITHDRAWAL FAILED: ATM cannot dispense this amount with available denominations!");
            return;
        }
        
        // Process withdrawal
        if (account.debit(amount)) {
            atm.dispenseCash(dispensed);
            transaction.setStatus(TransactionStatus.SUCCESS);
            account.addTransaction(transaction);
            
            System.out.println("WITHDRAWAL SUCCESSFUL!");
            System.out.println("Amount withdrawn: $" + amount);
            System.out.println("Cash dispensed:");
            dispensed.forEach((denom, count) -> {
                if (count > 0) {
                    System.out.println("  $" + denom.getValue() + " x " + count + " = $" + (denom.getValue() * count));
                }
            });
            System.out.println("Remaining balance: $" + account.getBalance());
            
            // Notify observers
            account.notifyObservers("Withdrawal of $" + amount + " successful");
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            account.addTransaction(transaction);
            System.out.println("WITHDRAWAL FAILED: Unable to process transaction!");
        }
    }
    
    @Override
    public void deposit(ATMMachine atm, double amount) {
        BankAccount account = atm.getCurrentAccount();
        
        if (amount <= 0) {
            System.out.println("DEPOSIT FAILED: Invalid amount!");
            return;
        }
        
        // Create transaction
        Transaction transaction = new Transaction(
            TransactionType.DEPOSIT,
            amount,
            account.getAccountNumber(),
            atm.getAtmId()
        );
        
        // Process deposit
        if (account.credit(amount)) {
            transaction.setStatus(TransactionStatus.SUCCESS);
            account.addTransaction(transaction);
            
            System.out.println("DEPOSIT SUCCESSFUL!");
            System.out.println("Amount deposited: $" + amount);
            System.out.println("New balance: $" + account.getBalance());
            
            // Notify observers
            account.notifyObservers("Deposit of $" + amount + " successful");
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            account.addTransaction(transaction);
            System.out.println("DEPOSIT FAILED: Unable to process transaction!");
        }
    }
    
    @Override
    public void checkBalance(ATMMachine atm) {
        BankAccount account = atm.getCurrentAccount();
        
        // Create transaction
        Transaction transaction = new Transaction(
            TransactionType.BALANCE_INQUIRY,
            0,
            account.getAccountNumber(),
            atm.getAtmId()
        );
        transaction.setStatus(TransactionStatus.SUCCESS);
        account.addTransaction(transaction);
        
        System.out.println("=== Account Balance ===");
        System.out.println("Account Number: " + account.getAccountNumber());
        System.out.println("Account Type: " + account.getAccountType());
        System.out.println("Available Balance: $" + account.getBalance());
        System.out.println("Daily Withdrawal Limit: $" + account.getDailyWithdrawalLimit());
        System.out.println("Withdrawn Today: $" + account.getWithdrawnToday());
        System.out.println("Remaining Daily Limit: $" + (account.getDailyWithdrawalLimit() - account.getWithdrawnToday()));
    }
    
    @Override
    public void viewTransactionHistory(ATMMachine atm) {
        BankAccount account = atm.getCurrentAccount();
        List<Transaction> transactions = account.getTransactionHistory();
        
        System.out.println("=== Transaction History (Last 10) ===");
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            transactions.stream()
                .limit(10)
                .forEach(txn -> System.out.println(txn.toString()));
        }
    }
    
    @Override
    public void transferFunds(ATMMachine atm, String toAccountNumber, double amount) {
        BankAccount fromAccount = atm.getCurrentAccount();
        BankAccount toAccount = atm.getBank().getAccount(toAccountNumber);
        
        if (toAccount == null) {
            System.out.println("TRANSFER FAILED: Destination account not found!");
            return;
        }
        
        if (amount <= 0) {
            System.out.println("TRANSFER FAILED: Invalid amount!");
            return;
        }
        
        // Create transaction
        Transaction transaction = new Transaction(
            TransactionType.TRANSFER,
            amount,
            fromAccount.getAccountNumber(),
            atm.getAtmId()
        );
        transaction.setToAccountNumber(toAccountNumber);
        
        // Validate transaction
        TransactionValidator validator = new TransferValidator();
        ValidationResult result = validator.validate(transaction, fromAccount);
        
        if (!result.isValid()) {
            transaction.setStatus(TransactionStatus.FAILED);
            fromAccount.addTransaction(transaction);
            System.out.println("TRANSFER FAILED: " + result.getMessage());
            return;
        }
        
        // Process transfer
        if (fromAccount.debit(amount) && toAccount.credit(amount)) {
            transaction.setStatus(TransactionStatus.SUCCESS);
            fromAccount.addTransaction(transaction);
            
            // Create corresponding deposit transaction in destination account
            Transaction depositTransaction = new Transaction(
                TransactionType.TRANSFER,
                amount,
                toAccountNumber,
                atm.getAtmId()
            );
            depositTransaction.setFromAccountNumber(fromAccount.getAccountNumber());
            depositTransaction.setStatus(TransactionStatus.SUCCESS);
            toAccount.addTransaction(depositTransaction);
            
            System.out.println("TRANSFER SUCCESSFUL!");
            System.out.println("Amount transferred: $" + amount);
            System.out.println("From: " + fromAccount.getAccountNumber());
            System.out.println("To: " + toAccountNumber);
            System.out.println("Remaining balance: $" + fromAccount.getBalance());
            
            // Notify observers
            fromAccount.notifyObservers("Transfer of $" + amount + " to " + toAccountNumber + " successful");
            toAccount.notifyObservers("Received $" + amount + " from " + fromAccount.getAccountNumber());
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            fromAccount.addTransaction(transaction);
            System.out.println("TRANSFER FAILED: Unable to process transaction!");
        }
    }
}

// ========================================================================================
// STRATEGY PATTERN IMPLEMENTATIONS
// ========================================================================================

/**
 * PIN-based authentication strategy
 */
class PINAuthenticationStrategy implements AuthenticationStrategy {
    private Bank bank;
    
    public PINAuthenticationStrategy(Bank bank) {
        this.bank = bank;
    }
    
    @Override
    public boolean authenticate(Card card, String pin) {
        BankAccount account = bank.getAccount(card.getAccountNumber());
        if (account == null) {
            return false;
        }
        return account.validatePIN(pin);
    }
}

/**
 * Greedy algorithm for cash dispensing
 * Uses largest denominations first
 */
class GreedyCashDispenseStrategy implements CashDispenseStrategy {
    @Override
    public Map<Denomination, Integer> dispenseCash(double amount, Map<Denomination, Integer> availableCash) {
        Map<Denomination, Integer> result = new EnumMap<>(Denomination.class);
        int remainingAmount = (int) amount;
        
        // Sort denominations in descending order
        List<Denomination> sortedDenoms = Arrays.stream(Denomination.values())
            .sorted(Comparator.comparingInt(Denomination::getValue).reversed())
            .collect(Collectors.toList());
        
        for (Denomination denom : sortedDenoms) {
            int availableCount = availableCash.getOrDefault(denom, 0);
            int needed = remainingAmount / denom.getValue();
            int toDispense = Math.min(needed, availableCount);
            
            if (toDispense > 0) {
                result.put(denom, toDispense);
                remainingAmount -= toDispense * denom.getValue();
            }
        }
        
        // If we couldn't dispense exact amount, return null
        if (remainingAmount > 0) {
            return null;
        }
        
        return result;
    }
}

// ========================================================================================
// VALIDATOR IMPLEMENTATIONS
// ========================================================================================

/**
 * Validation result wrapper
 */
class ValidationResult {
    private boolean valid;
    private String message;
    
    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }
    
    public boolean isValid() {
        return valid;
    }
    
    public String getMessage() {
        return message;
    }
}

/**
 * Abstract validator with common validation logic
 */
abstract class AbstractTransactionValidator implements TransactionValidator {
    protected ValidationResult validateBasic(Transaction transaction, BankAccount account) {
        if (transaction.getAmount() <= 0) {
            return new ValidationResult(false, "Invalid transaction amount");
        }
        
        if (account.isLocked()) {
            return new ValidationResult(false, "Account is locked");
        }
        
        return new ValidationResult(true, "");
    }
}

/**
 * Withdrawal transaction validator
 */
class WithdrawalValidator extends AbstractTransactionValidator {
    private static final double MAX_TRANSACTION_AMOUNT = 2000.0;
    
    @Override
    public ValidationResult validate(Transaction transaction, BankAccount account) {
        // Basic validation
        ValidationResult basicResult = validateBasic(transaction, account);
        if (!basicResult.isValid()) {
            return basicResult;
        }
        
        double amount = transaction.getAmount();
        
        // Check maximum transaction limit
        if (amount > MAX_TRANSACTION_AMOUNT) {
            return new ValidationResult(false, "Amount exceeds maximum transaction limit of $" + MAX_TRANSACTION_AMOUNT);
        }
        
        // Check daily withdrawal limit
        if (account.getWithdrawnToday() + amount > account.getDailyWithdrawalLimit()) {
            return new ValidationResult(false, "Daily withdrawal limit exceeded");
        }
        
        // Check minimum balance
        if (account.getBalance() - amount < account.getMinimumBalance()) {
            return new ValidationResult(false, "Insufficient funds. Minimum balance requirement: $" + account.getMinimumBalance());
        }
        
        return new ValidationResult(true, "Validation successful");
    }
}

/**
 * Transfer transaction validator
 */
class TransferValidator extends AbstractTransactionValidator {
    private static final double MAX_TRANSFER_AMOUNT = 5000.0;
    
    @Override
    public ValidationResult validate(Transaction transaction, BankAccount account) {
        // Basic validation
        ValidationResult basicResult = validateBasic(transaction, account);
        if (!basicResult.isValid()) {
            return basicResult;
        }
        
        double amount = transaction.getAmount();
        
        // Check maximum transfer limit
        if (amount > MAX_TRANSFER_AMOUNT) {
            return new ValidationResult(false, "Amount exceeds maximum transfer limit of $" + MAX_TRANSFER_AMOUNT);
        }
        
        // Check balance
        if (account.getBalance() - amount < account.getMinimumBalance()) {
            return new ValidationResult(false, "Insufficient funds");
        }
        
        return new ValidationResult(true, "Validation successful");
    }
}

// ========================================================================================
// OBSERVER PATTERN IMPLEMENTATIONS
// ========================================================================================

/**
 * SMS notification observer
 */
class SMSNotificationObserver implements NotificationObserver {
    @Override
    public void notify(String message, BankAccount account) {
        System.out.println("[SMS] Notification sent to " + account.getAccountNumber() + ": " + message);
    }
}

/**
 * Email notification observer
 */
class EmailNotificationObserver implements NotificationObserver {
    @Override
    public void notify(String message, BankAccount account) {
        System.out.println("[EMAIL] Notification sent to " + account.getAccountNumber() + ": " + message);
    }
}

// ========================================================================================
// CORE MODEL CLASSES
// ========================================================================================

/**
 * Represents a bank card
 */
class Card {
    private String cardNumber;
    private String accountNumber;
    private CardType cardType;
    private LocalDateTime expiryDate;
    private boolean blocked;
    
    public Card(String cardNumber, String accountNumber, CardType cardType, LocalDateTime expiryDate) {
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
        this.cardType = cardType;
        this.expiryDate = expiryDate;
        this.blocked = false;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public CardType getCardType() {
        return cardType;
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
    
    public boolean isBlocked() {
        return blocked;
    }
    
    public void block() {
        this.blocked = true;
    }
}

/**
 * Represents a transaction
 */
class Transaction {
    private String transactionId;
    private TransactionType type;
    private double amount;
    private String accountNumber;
    private String fromAccountNumber;
    private String toAccountNumber;
    private LocalDateTime timestamp;
    private TransactionStatus status;
    private String atmId;
    
    public Transaction(TransactionType type, double amount, String accountNumber, String atmId) {
        this.transactionId = UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.PENDING;
        this.atmId = atmId;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public TransactionStatus getStatus() {
        return status;
    }
    
    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
    
    public String getAtmId() {
        return atmId;
    }
    
    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }
    
    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s] %s - %s: $%.2f", 
            timestamp.toString(), transactionId.substring(0, 8), type, amount));
        if (toAccountNumber != null) {
            sb.append(" to ").append(toAccountNumber);
        }
        if (fromAccountNumber != null) {
            sb.append(" from ").append(fromAccountNumber);
        }
        sb.append(String.format(" - Status: %s", status));
        return sb.toString();
    }
}

/**
 * Abstract base class for bank accounts
 * Demonstrates abstraction and encapsulation
 */
abstract class BankAccount {
    protected String accountNumber;
    protected String cardNumber;
    protected String pin;
    protected double balance;
    protected AccountType accountType;
    protected List<Transaction> transactionHistory;
    protected boolean locked;
    protected double dailyWithdrawalLimit;
    protected double withdrawnToday;
    protected LocalDateTime lastWithdrawalDate;
    protected double minimumBalance;
    protected List<NotificationObserver> observers;
    protected int failedPinAttempts;
    
    public BankAccount(String accountNumber, String cardNumber, String pin, double initialBalance, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = initialBalance;
        this.accountType = accountType;
        this.transactionHistory = new ArrayList<>();
        this.locked = false;
        this.withdrawnToday = 0;
        this.lastWithdrawalDate = LocalDateTime.now();
        this.observers = new ArrayList<>();
        this.failedPinAttempts = 0;
        
        // Add default observers
        this.observers.add(new SMSNotificationObserver());
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getCardNumber() {
        return cardNumber;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public boolean isLocked() {
        return locked;
    }
    
    public void lock() {
        this.locked = true;
    }
    
    public void unlock() {
        this.locked = false;
    }
    
    public boolean validatePIN(String pin) {
        return this.pin.equals(pin);
    }
    
    public int getFailedPinAttempts() {
        return failedPinAttempts;
    }
    
    public void incrementFailedAttempts() {
        this.failedPinAttempts++;
    }
    
    public void resetFailedAttempts() {
        this.failedPinAttempts = 0;
    }
    
    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }
    
    public double getWithdrawnToday() {
        resetDailyLimitIfNeeded();
        return withdrawnToday;
    }
    
    public double getMinimumBalance() {
        return minimumBalance;
    }
    
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }
    
    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
    
    /**
     * Debit money from account
     */
    public synchronized boolean debit(double amount) {
        if (locked || balance - amount < minimumBalance) {
            return false;
        }
        
        resetDailyLimitIfNeeded();
        balance -= amount;
        withdrawnToday += amount;
        return true;
    }
    
    /**
     * Credit money to account
     */
    public synchronized boolean credit(double amount) {
        if (locked) {
            return false;
        }
        balance += amount;
        return true;
    }
    
    /**
     * Reset daily withdrawal limit if new day
     */
    private void resetDailyLimitIfNeeded() {
        if (lastWithdrawalDate.toLocalDate().isBefore(LocalDateTime.now().toLocalDate())) {
            withdrawnToday = 0;
            lastWithdrawalDate = LocalDateTime.now();
        }
    }
    
    /**
     * Add notification observer
     */
    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Notify all observers
     */
    public void notifyObservers(String message) {
        for (NotificationObserver observer : observers) {
            observer.notify(message, this);
        }
    }
}

/**
 * Savings account implementation
 * Demonstrates inheritance and polymorphism
 */
class SavingsAccount extends BankAccount {
    private static final double SAVINGS_DAILY_LIMIT = 5000.0;
    private static final double SAVINGS_MIN_BALANCE = 100.0;
    
    public SavingsAccount(String accountNumber, String cardNumber, String pin, double initialBalance) {
        super(accountNumber, cardNumber, pin, initialBalance, AccountType.SAVINGS);
        this.dailyWithdrawalLimit = SAVINGS_DAILY_LIMIT;
        this.minimumBalance = SAVINGS_MIN_BALANCE;
    }
}

/**
 * Current account implementation
 * Demonstrates inheritance and polymorphism
 */
class CurrentAccount extends BankAccount {
    private static final double CURRENT_DAILY_LIMIT = 10000.0;
    private static final double CURRENT_MIN_BALANCE = 0.0;
    
    public CurrentAccount(String accountNumber, String cardNumber, String pin, double initialBalance) {
        super(accountNumber, cardNumber, pin, initialBalance, AccountType.CURRENT);
        this.dailyWithdrawalLimit = CURRENT_DAILY_LIMIT;
        this.minimumBalance = CURRENT_MIN_BALANCE;
    }
}

/**
 * Fixed Deposit account implementation
 * Demonstrates inheritance and polymorphism
 * Has lower withdrawal limits and higher minimum balance
 */
class FixedDepositAccount extends BankAccount {
    private static final double FD_DAILY_LIMIT = 2000.0;
    private static final double FD_MIN_BALANCE = 1000.0;
    
    public FixedDepositAccount(String accountNumber, String cardNumber, String pin, double initialBalance) {
        super(accountNumber, cardNumber, pin, initialBalance, AccountType.FIXED_DEPOSIT);
        this.dailyWithdrawalLimit = FD_DAILY_LIMIT;
        this.minimumBalance = FD_MIN_BALANCE;
    }
}

/**
 * Bank class managing all accounts
 * Demonstrates Single Responsibility Principle
 */
class Bank {
    private String bankName;
    private Map<String, BankAccount> accounts;
    
    public Bank(String bankName) {
        this.bankName = bankName;
        this.accounts = new ConcurrentHashMap<>();
    }
    
    public String getBankName() {
        return bankName;
    }
    
    public void addAccount(BankAccount account) {
        accounts.put(account.getAccountNumber(), account);
    }
    
    public BankAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    
    public BankAccount getAccountByCardNumber(String cardNumber) {
        return accounts.values().stream()
            .filter(acc -> acc.getCardNumber().equals(cardNumber))
            .findFirst()
            .orElse(null);
    }
}

/**
 * ATM Machine class
 * Core class managing ATM operations with State Pattern
 */
class ATMMachine {
    private String atmId;
    private String location;
    private Bank bank;
    private ATMState currentState;
    private Card currentCard;
    private BankAccount currentAccount;
    private Map<Denomination, Integer> cashInventory;
    private List<Transaction> atmTransactionLog;
    
    public ATMMachine(String atmId, String location, Bank bank) {
        this.atmId = atmId;
        this.location = location;
        this.bank = bank;
        this.currentState = new IdleState();
        this.cashInventory = new EnumMap<>(Denomination.class);
        this.atmTransactionLog = new ArrayList<>();
        
        // Initialize cash inventory to zero
        for (Denomination denom : Denomination.values()) {
            cashInventory.put(denom, 0);
        }
    }
    
    public String getAtmId() {
        return atmId;
    }
    
    public String getLocation() {
        return location;
    }
    
    public Bank getBank() {
        return bank;
    }
    
    public ATMState getCurrentState() {
        return currentState;
    }
    
    public void setState(ATMState state) {
        this.currentState = state;
    }
    
    public Card getCurrentCard() {
        return currentCard;
    }
    
    public void setCurrentCard(Card card) {
        this.currentCard = card;
    }
    
    public BankAccount getCurrentAccount() {
        return currentAccount;
    }
    
    public void setCurrentAccount(BankAccount account) {
        this.currentAccount = account;
    }
    
    public Map<Denomination, Integer> getCashInventory() {
        return new EnumMap<>(cashInventory);
    }
    
    /**
     * Add cash to ATM inventory
     */
    public void addCash(Denomination denomination, int count) {
        cashInventory.put(denomination, cashInventory.get(denomination) + count);
    }
    
    /**
     * Dispense cash from ATM inventory
     */
    public void dispenseCash(Map<Denomination, Integer> toDispense) {
        for (Map.Entry<Denomination, Integer> entry : toDispense.entrySet()) {
            Denomination denom = entry.getKey();
            int count = entry.getValue();
            int currentCount = cashInventory.get(denom);
            cashInventory.put(denom, currentCount - count);
        }
    }
    
    /**
     * Get total cash available in ATM
     */
    public double getTotalCash() {
        return cashInventory.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getValue() * entry.getValue())
            .sum();
    }
    
    /**
     * Add transaction to ATM log
     */
    public void logTransaction(Transaction transaction) {
        atmTransactionLog.add(transaction);
    }
    
    public List<Transaction> getTransactionLog() {
        return new ArrayList<>(atmTransactionLog);
    }
    
    // Delegate methods to current state
    public void insertCard(Card card) {
        currentState.insertCard(this, card);
    }
    
    public void ejectCard() {
        currentState.ejectCard(this);
    }
    
    public void enterPIN(String pin) {
        currentState.enterPIN(this, pin);
    }
    
    public void withdraw(double amount) {
        currentState.withdraw(this, amount);
    }
    
    public void deposit(double amount) {
        currentState.deposit(this, amount);
    }
    
    public void checkBalance() {
        currentState.checkBalance(this);
    }
    
    public void viewTransactionHistory() {
        currentState.viewTransactionHistory(this);
    }
    
    public void transferFunds(String toAccountNumber, double amount) {
        currentState.transferFunds(this, toAccountNumber, amount);
    }
}

/**
 * Abstract User class
 * Demonstrates abstraction
 */
abstract class User {
    protected String userId;
    protected String name;
    protected UserRole role;
    
    public User(String userId, String name, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getName() {
        return name;
    }
    
    public UserRole getRole() {
        return role;
    }
}

/**
 * Admin user with special privileges
 * Demonstrates inheritance and Open/Closed Principle
 */
class AdminUser extends User {
    public AdminUser(String userId, String name) {
        super(userId, name, UserRole.ADMIN);
    }
    
    /**
     * Refill ATM cash inventory
     */
    public void refillCash(ATMMachine atm, Denomination denomination, int count) {
        atm.addCash(denomination, count);
        System.out.println("Admin " + name + " refilled " + count + " x $" + denomination.getValue() + " notes in ATM " + atm.getAtmId());
    }
    
    /**
     * View ATM status
     */
    public void viewATMStatus(ATMMachine atm) {
        System.out.println("=== ATM Status: " + atm.getAtmId() + " ===");
        System.out.println("Location: " + atm.getLocation());
        System.out.println("Total Cash: $" + atm.getTotalCash());
        System.out.println("Cash Inventory:");
        Map<Denomination, Integer> inventory = atm.getCashInventory();
        for (Denomination denom : Denomination.values()) {
            int count = inventory.get(denom);
            System.out.println("  $" + denom.getValue() + " x " + count + " = $" + (denom.getValue() * count));
        }
    }
    
    /**
     * View transaction logs
     */
    public void viewTransactionLogs(ATMMachine atm) {
        System.out.println("=== Transaction Logs for ATM: " + atm.getAtmId() + " ===");
        List<Transaction> logs = atm.getTransactionLog();
        if (logs.isEmpty()) {
            System.out.println("No transactions recorded.");
        } else {
            logs.forEach(System.out::println);
        }
    }
    
    /**
     * Unlock account
     */
    public void unlockAccount(BankAccount account) {
        account.unlock();
        System.out.println("Admin " + name + " unlocked account " + account.getAccountNumber());
    }
}

/**
 * ATM System - Singleton pattern
 * Central controller for ATM operations
 */
class ATMSystem {
    private static ATMSystem instance;
    private Map<String, ATMMachine> atmMachines;
    
    private ATMSystem() {
        this.atmMachines = new ConcurrentHashMap<>();
    }
    
    /**
     * Get singleton instance (Singleton Pattern)
     */
    public static synchronized ATMSystem getInstance() {
        if (instance == null) {
            instance = new ATMSystem();
        }
        return instance;
    }
    
    /**
     * Register ATM machine
     */
    public void registerATM(ATMMachine atm) {
        atmMachines.put(atm.getAtmId(), atm);
    }
    
    /**
     * Get ATM machine by ID
     */
    public ATMMachine getATM(String atmId) {
        return atmMachines.get(atmId);
    }
    
    // Convenience methods delegating to ATM
    public void insertCard(ATMMachine atm, Card card) {
        atm.insertCard(card);
    }
    
    public void ejectCard(ATMMachine atm) {
        atm.ejectCard();
    }
    
    public void enterPIN(ATMMachine atm, String pin) {
        atm.enterPIN(pin);
    }
    
    public void withdrawCash(ATMMachine atm, double amount) {
        atm.withdraw(amount);
    }
    
    public void depositCash(ATMMachine atm, double amount) {
        atm.deposit(amount);
    }
    
    public void checkBalance(ATMMachine atm) {
        atm.checkBalance();
    }
    
    public void viewTransactionHistory(ATMMachine atm) {
        atm.viewTransactionHistory();
    }
    
    public void transferFunds(ATMMachine atm, String toAccountNumber, double amount) {
        atm.transferFunds(toAccountNumber, amount);
    }
}