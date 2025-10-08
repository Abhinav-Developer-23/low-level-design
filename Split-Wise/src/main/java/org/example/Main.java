package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Splitwise System - Expense Management System
 * This system manages shared expenses among groups of friends
 * Follows SOLID principles and uses Strategy Pattern for split calculations
 */

// ========================= ENUMS =========================

/**
 * Enum for different types of expense splits
 */
enum SplitType {
    EQUAL,      // Split equally among all members
    EXACT,      // Split according to exact amounts
    PERCENTAGE  // Split according to percentages
}

/**
 * Enum for transaction types
 */
enum TransactionType {
    EXPENSE,
    SETTLEMENT
}

// ========================= MODELS =========================

/**
 * Represents a User in the system
 */
class User {
    private final String userId;
    private final String name;
    private final String email;
    private final Set<String> groupIds; // Groups this user belongs to

    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.groupIds = new HashSet<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getGroupIds() {
        return new HashSet<>(groupIds);
    }

    public void addGroup(String groupId) {
        groupIds.add(groupId);
    }

    public void removeGroup(String groupId) {
        groupIds.remove(groupId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

/**
 * Represents a Group of users
 */
class Group {
    private final String groupId;
    private final String groupName;
    private final Set<String> memberIds; // User IDs in this group
    private final List<String> expenseIds; // Expenses in this group
    private final LocalDateTime createdAt;

    public Group(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.memberIds = new HashSet<>();
        this.expenseIds = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Set<String> getMemberIds() {
        return new HashSet<>(memberIds);
    }

    public List<String> getExpenseIds() {
        return new ArrayList<>(expenseIds);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addMember(String userId) {
        if (memberIds.size() >= 100) {
            throw new IllegalStateException("Group cannot have more than 100 members");
        }
        memberIds.add(userId);
    }

    public void removeMember(String userId) {
        memberIds.remove(userId);
    }

    public void addExpense(String expenseId) {
        expenseIds.add(expenseId);
    }

    public boolean hasMember(String userId) {
        return memberIds.contains(userId);
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", memberCount=" + memberIds.size() +
                '}';
    }
}

/**
 * Represents a split detail for a specific user in an expense
 */
class Split {
    private final String userId;
    private final BigDecimal amount;

    public Split(String userId, BigDecimal amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Split{" +
                "userId='" + userId + '\'' +
                ", amount=" + amount +
                '}';
    }
}

/**
 * Represents an Expense in the system
 */
class Expense {
    private final String expenseId;
    private final String groupId;
    private final String description;
    private final BigDecimal totalAmount;
    private final String paidBy; // User ID who paid
    private final SplitType splitType;
    private final Map<String, BigDecimal> splits; // userId -> amount owed
    private final LocalDateTime createdAt;

    public Expense(String expenseId, String groupId, String description, 
                   BigDecimal totalAmount, String paidBy, SplitType splitType,
                   Map<String, BigDecimal> splits) {
        this.expenseId = expenseId;
        this.groupId = groupId;
        this.description = description;
        this.totalAmount = totalAmount;
        this.paidBy = paidBy;
        this.splitType = splitType;
        this.splits = new HashMap<>(splits);
        this.createdAt = LocalDateTime.now();
    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public Map<String, BigDecimal> getSplits() {
        return new HashMap<>(splits);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId='" + expenseId + '\'' +
                ", description='" + description + '\'' +
                ", totalAmount=" + totalAmount +
                ", paidBy='" + paidBy + '\'' +
                ", splitType=" + splitType +
                '}';
    }
}

/**
 * Represents a settlement transaction between two users
 */
class Settlement {
    private final String settlementId;
    private final String groupId;
    private final String fromUserId; // User paying
    private final String toUserId;   // User receiving
    private final BigDecimal amount;
    private final LocalDateTime settledAt;

    public Settlement(String settlementId, String groupId, String fromUserId, 
                     String toUserId, BigDecimal amount) {
        this.settlementId = settlementId;
        this.groupId = groupId;
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.amount = amount;
        this.settledAt = LocalDateTime.now();
    }

    public String getSettlementId() {
        return settlementId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getSettledAt() {
        return settledAt;
    }

    @Override
    public String toString() {
        return "Settlement{" +
                "settlementId='" + settlementId + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", amount=" + amount +
                ", settledAt=" + settledAt +
                '}';
    }
}

/**
 * Represents balance between two users
 */
class Balance {
    private final String userId1;
    private final String userId2;
    private BigDecimal amount; // Positive means userId1 owes userId2, negative means userId2 owes userId1

    public Balance(String userId1, String userId2, BigDecimal amount) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.amount = amount;
    }

    public String getUserId1() {
        return userId1;
    }

    public String getUserId2() {
        return userId2;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void addAmount(BigDecimal delta) {
        this.amount = this.amount.add(delta);
    }

    @Override
    public String toString() {
        return "Balance{" +
                "userId1='" + userId1 + '\'' +
                ", userId2='" + userId2 + '\'' +
                ", amount=" + amount +
                '}';
    }
}

// ========================= INTERFACES =========================

/**
 * Strategy interface for calculating expense splits
 * Follows Strategy Pattern for different split calculations
 */
interface SplitStrategy {
    /**
     * Calculate splits for an expense
     * @param totalAmount Total expense amount
     * @param memberIds List of members to split among
     * @param splitData Additional data for split calculation (amounts, percentages, etc.)
     * @return Map of userId to amount owed
     */
    Map<String, BigDecimal> calculateSplit(BigDecimal totalAmount, 
                                          List<String> memberIds, 
                                          Map<String, BigDecimal> splitData);
    
    /**
     * Validate split data
     * @param totalAmount Total expense amount
     * @param memberIds List of members
     * @param splitData Split data to validate
     * @return true if valid, false otherwise
     */
    boolean validateSplit(BigDecimal totalAmount, 
                         List<String> memberIds, 
                         Map<String, BigDecimal> splitData);
}

/**
 * Interface for balance calculation
 */
interface BalanceCalculator {
    /**
     * Calculate balances for a group
     * @param groupId Group ID
     * @return Map of user pairs to balance amounts
     */
    Map<String, Map<String, BigDecimal>> calculateGroupBalances(String groupId);
    
    /**
     * Get balance for a specific user in a group
     * @param userId User ID
     * @param groupId Group ID
     * @return Map of other users to amounts (positive = they owe user, negative = user owes them)
     */
    Map<String, BigDecimal> getUserBalanceInGroup(String userId, String groupId);
}

/**
 * Interface for notification service
 * Follows Open/Closed Principle - can be extended without modifying existing code
 */
interface NotificationService {
    void notifyExpenseAdded(Expense expense, List<User> users);
    void notifySettlement(Settlement settlement, User fromUser, User toUser);
}

// ========================= STRATEGY IMPLEMENTATIONS =========================

/**
 * Equal split strategy - splits amount equally among all members
 */
class EqualSplitStrategy implements SplitStrategy {
    
    @Override
    public Map<String, BigDecimal> calculateSplit(BigDecimal totalAmount, 
                                                  List<String> memberIds, 
                                                  Map<String, BigDecimal> splitData) {
        Map<String, BigDecimal> splits = new HashMap<>();
        int memberCount = memberIds.size();
        
        if (memberCount == 0) {
            throw new IllegalArgumentException("Cannot split among 0 members");
        }
        
        // Calculate equal split with proper rounding
        BigDecimal equalShare = totalAmount.divide(
            BigDecimal.valueOf(memberCount), 
            2, 
            RoundingMode.HALF_UP
        );
        
        // Assign equal share to each member
        for (int i = 0; i < memberCount - 1; i++) {
            splits.put(memberIds.get(i), equalShare);
        }
        
        // Last member gets the remainder to handle rounding differences
        BigDecimal sumSoFar = equalShare.multiply(BigDecimal.valueOf(memberCount - 1));
        BigDecimal lastShare = totalAmount.subtract(sumSoFar);
        splits.put(memberIds.get(memberCount - 1), lastShare);
        
        return splits;
    }
    
    @Override
    public boolean validateSplit(BigDecimal totalAmount, 
                                List<String> memberIds, 
                                Map<String, BigDecimal> splitData) {
        return memberIds != null && !memberIds.isEmpty() && totalAmount.compareTo(BigDecimal.ZERO) > 0;
    }
}

/**
 * Exact split strategy - splits according to exact amounts specified
 */
class ExactSplitStrategy implements SplitStrategy {
    
    @Override
    public Map<String, BigDecimal> calculateSplit(BigDecimal totalAmount, 
                                                  List<String> memberIds, 
                                                  Map<String, BigDecimal> splitData) {
        if (splitData == null || splitData.isEmpty()) {
            throw new IllegalArgumentException("Exact split requires split amounts for each member");
        }
        
        Map<String, BigDecimal> splits = new HashMap<>();
        
        for (String memberId : memberIds) {
            if (!splitData.containsKey(memberId)) {
                throw new IllegalArgumentException("Missing split amount for member: " + memberId);
            }
            splits.put(memberId, splitData.get(memberId));
        }
        
        return splits;
    }
    
    @Override
    public boolean validateSplit(BigDecimal totalAmount, 
                                List<String> memberIds, 
                                Map<String, BigDecimal> splitData) {
        if (splitData == null || splitData.size() != memberIds.size()) {
            return false;
        }
        
        // Check if all members have amounts
        for (String memberId : memberIds) {
            if (!splitData.containsKey(memberId)) {
                return false;
            }
        }
        
        // Check if sum of amounts equals total
        BigDecimal sum = splitData.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sum.compareTo(totalAmount) == 0;
    }
}

/**
 * Percentage split strategy - splits according to percentages
 */
class PercentageSplitStrategy implements SplitStrategy {
    
    @Override
    public Map<String, BigDecimal> calculateSplit(BigDecimal totalAmount, 
                                                  List<String> memberIds, 
                                                  Map<String, BigDecimal> splitData) {
        if (splitData == null || splitData.isEmpty()) {
            throw new IllegalArgumentException("Percentage split requires percentages for each member");
        }
        
        Map<String, BigDecimal> splits = new HashMap<>();
        
        for (int i = 0; i < memberIds.size() - 1; i++) {
            String memberId = memberIds.get(i);
            if (!splitData.containsKey(memberId)) {
                throw new IllegalArgumentException("Missing percentage for member: " + memberId);
            }
            
            BigDecimal percentage = splitData.get(memberId);
            BigDecimal amount = totalAmount.multiply(percentage)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            splits.put(memberId, amount);
        }
        
        // Last member gets the remainder to handle rounding differences
        String lastMember = memberIds.get(memberIds.size() - 1);
        BigDecimal sumSoFar = splits.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal lastAmount = totalAmount.subtract(sumSoFar);
        splits.put(lastMember, lastAmount);
        
        return splits;
    }
    
    @Override
    public boolean validateSplit(BigDecimal totalAmount, 
                                List<String> memberIds, 
                                Map<String, BigDecimal> splitData) {
        if (splitData == null || splitData.size() != memberIds.size()) {
            return false;
        }
        
        // Check if all members have percentages
        for (String memberId : memberIds) {
            if (!splitData.containsKey(memberId)) {
                return false;
            }
        }
        
        // Check if sum of percentages equals 100
        BigDecimal sum = splitData.values().stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return sum.compareTo(BigDecimal.valueOf(100)) == 0;
    }
}

/**
 * Factory for creating split strategies
 * Follows Factory Pattern
 */
class SplitStrategyFactory {
    private static final Map<SplitType, SplitStrategy> strategies = new HashMap<>();
    
    static {
        strategies.put(SplitType.EQUAL, new EqualSplitStrategy());
        strategies.put(SplitType.EXACT, new ExactSplitStrategy());
        strategies.put(SplitType.PERCENTAGE, new PercentageSplitStrategy());
    }
    
    public static SplitStrategy getStrategy(SplitType splitType) {
        SplitStrategy strategy = strategies.get(splitType);
        if (strategy == null) {
            throw new IllegalArgumentException("Unknown split type: " + splitType);
        }
        return strategy;
    }
}

// ========================= SERVICE IMPLEMENTATIONS =========================

/**
 * Console notification service implementation
 */
class ConsoleNotificationService implements NotificationService {
    
    @Override
    public void notifyExpenseAdded(Expense expense, List<User> users) {
        System.out.println("\n[NOTIFICATION] New expense added:");
        System.out.println("  Description: " + expense.getDescription());
        System.out.println("  Amount: $" + expense.getTotalAmount());
        System.out.println("  Notifying " + users.size() + " users");
    }
    
    @Override
    public void notifySettlement(Settlement settlement, User fromUser, User toUser) {
        System.out.println("\n[NOTIFICATION] Settlement recorded:");
        System.out.println("  " + fromUser.getName() + " paid " + toUser.getName() + 
                         " $" + settlement.getAmount());
    }
}

/**
 * Balance calculator implementation
 */
class BalanceCalculatorImpl implements BalanceCalculator {
    private final Map<String, Expense> expenses;
    private final Map<String, Settlement> settlements;
    private final Map<String, Group> groups;
    
    public BalanceCalculatorImpl(Map<String, Expense> expenses, 
                                Map<String, Settlement> settlements,
                                Map<String, Group> groups) {
        this.expenses = expenses;
        this.settlements = settlements;
        this.groups = groups;
    }
    
    @Override
    public Map<String, Map<String, BigDecimal>> calculateGroupBalances(String groupId) {
        // Map of userId1 -> (userId2 -> amount)
        // Positive amount means userId1 owes userId2
        Map<String, Map<String, BigDecimal>> balances = new HashMap<>();
        
        Group group = groups.get(groupId);
        if (group == null) {
            return balances;
        }
        
        // Initialize balances for all members
        for (String memberId : group.getMemberIds()) {
            balances.put(memberId, new HashMap<>());
        }
        
        // Process all expenses in the group
        for (String expenseId : group.getExpenseIds()) {
            Expense expense = expenses.get(expenseId);
            if (expense == null) continue;
            
            String paidBy = expense.getPaidBy();
            Map<String, BigDecimal> splits = expense.getSplits();
            
            // For each split, update the balance
            for (Map.Entry<String, BigDecimal> entry : splits.entrySet()) {
                String userId = entry.getKey();
                BigDecimal amount = entry.getValue();
                
                // Skip if user paid for themselves
                if (userId.equals(paidBy)) {
                    continue;
                }
                
                // userId owes paidBy the amount
                updateBalance(balances, userId, paidBy, amount);
            }
        }
        
        // Process settlements
        for (Settlement settlement : settlements.values()) {
            if (!settlement.getGroupId().equals(groupId)) continue;
            
            String fromUser = settlement.getFromUserId();
            String toUser = settlement.getToUserId();
            BigDecimal amount = settlement.getAmount();
            
            // Settlement reduces the debt
            updateBalance(balances, fromUser, toUser, amount.negate());
        }
        
        // Simplify balances - keep only non-zero balances in one direction
        simplifyBalances(balances);
        
        return balances;
    }
    
    @Override
    public Map<String, BigDecimal> getUserBalanceInGroup(String userId, String groupId) {
        Map<String, Map<String, BigDecimal>> groupBalances = calculateGroupBalances(groupId);
        
        Map<String, BigDecimal> userBalance = new HashMap<>();
        
        // Get amounts this user owes others
        if (groupBalances.containsKey(userId)) {
            for (Map.Entry<String, BigDecimal> entry : groupBalances.get(userId).entrySet()) {
                userBalance.put(entry.getKey(), entry.getValue().negate()); // Negate because we show from user's perspective
            }
        }
        
        // Get amounts others owe this user
        for (Map.Entry<String, Map<String, BigDecimal>> entry : groupBalances.entrySet()) {
            String otherUser = entry.getKey();
            if (otherUser.equals(userId)) continue;
            
            if (entry.getValue().containsKey(userId)) {
                BigDecimal amount = entry.getValue().get(userId);
                userBalance.put(otherUser, amount); // Positive means they owe us
            }
        }
        
        return userBalance;
    }
    
    private void updateBalance(Map<String, Map<String, BigDecimal>> balances, 
                              String fromUser, String toUser, BigDecimal amount) {
        balances.putIfAbsent(fromUser, new HashMap<>());
        Map<String, BigDecimal> fromUserBalances = balances.get(fromUser);
        
        BigDecimal currentBalance = fromUserBalances.getOrDefault(toUser, BigDecimal.ZERO);
        fromUserBalances.put(toUser, currentBalance.add(amount));
    }
    
    private void simplifyBalances(Map<String, Map<String, BigDecimal>> balances) {
        // Remove zero balances and consolidate bidirectional debts
        for (Map.Entry<String, Map<String, BigDecimal>> entry : balances.entrySet()) {
            String user1 = entry.getKey();
            Map<String, BigDecimal> user1Balances = entry.getValue();
            
            List<String> toRemove = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> balanceEntry : user1Balances.entrySet()) {
                String user2 = balanceEntry.getKey();
                BigDecimal amount = balanceEntry.getValue();
                
                // Check if there's a reverse balance
                if (balances.containsKey(user2) && balances.get(user2).containsKey(user1)) {
                    BigDecimal reverseAmount = balances.get(user2).get(user1);
                    BigDecimal netAmount = amount.subtract(reverseAmount);
                    
                    if (netAmount.compareTo(BigDecimal.ZERO) > 0) {
                        // user1 owes user2
                        user1Balances.put(user2, netAmount);
                        balances.get(user2).remove(user1);
                    } else if (netAmount.compareTo(BigDecimal.ZERO) < 0) {
                        // user2 owes user1
                        balances.get(user2).put(user1, netAmount.negate());
                        toRemove.add(user2);
                    } else {
                        // Balanced
                        toRemove.add(user2);
                        balances.get(user2).remove(user1);
                    }
                } else if (amount.compareTo(BigDecimal.ZERO) == 0) {
                    toRemove.add(user2);
                }
            }
            
            for (String userId : toRemove) {
                user1Balances.remove(userId);
            }
        }
    }
}

// ========================= MAIN SYSTEM =========================

/**
 * Main Splitwise System - Manages all operations
 * Follows Singleton Pattern for system management
 */
class SplitwiseSystem {
    private static SplitwiseSystem instance;
    
    // Data stores
    private final Map<String, User> users;
    private final Map<String, Group> groups;
    private final Map<String, Expense> expenses;
    private final Map<String, Settlement> settlements;
    
    // ID generators
    private final AtomicInteger userIdCounter;
    private final AtomicInteger groupIdCounter;
    private final AtomicInteger expenseIdCounter;
    private final AtomicInteger settlementIdCounter;
    
    // Services
    private final NotificationService notificationService;
    private final BalanceCalculator balanceCalculator;
    
    private SplitwiseSystem() {
        this.users = new ConcurrentHashMap<>();
        this.groups = new ConcurrentHashMap<>();
        this.expenses = new ConcurrentHashMap<>();
        this.settlements = new ConcurrentHashMap<>();
        
        this.userIdCounter = new AtomicInteger(1);
        this.groupIdCounter = new AtomicInteger(1);
        this.expenseIdCounter = new AtomicInteger(1);
        this.settlementIdCounter = new AtomicInteger(1);
        
        this.notificationService = new ConsoleNotificationService();
        this.balanceCalculator = new BalanceCalculatorImpl(expenses, settlements, groups);
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized SplitwiseSystem getInstance() {
        if (instance == null) {
            instance = new SplitwiseSystem();
        }
        return instance;
    }
    
    /**
     * Create a new user
     */
    public User createUser(String name, String email) {
        String userId = "U" + userIdCounter.getAndIncrement();
        User user = new User(userId, name, email);
        users.put(userId, user);
        System.out.println("Created user: " + user);
        return user;
    }
    
    /**
     * Create a new group
     */
    public Group createGroup(String groupName) {
        if (groups.size() >= 50) {
            throw new IllegalStateException("Cannot create more than 50 groups");
        }
        
        String groupId = "G" + groupIdCounter.getAndIncrement();
        Group group = new Group(groupId, groupName);
        groups.put(groupId, group);
        System.out.println("Created group: " + group);
        return group;
    }
    
    /**
     * Add user to a group
     */
    public void addUserToGroup(String userId, String groupId) {
        User user = users.get(userId);
        Group group = groups.get(groupId);
        
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        
        group.addMember(userId);
        user.addGroup(groupId);
        System.out.println("Added " + user.getName() + " to group " + group.getGroupName());
    }
    
    /**
     * Add an expense to a group
     */
    public Expense addExpense(String groupId, String description, BigDecimal totalAmount,
                             String paidBy, SplitType splitType, 
                             Map<String, BigDecimal> splitData) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        
        User payer = users.get(paidBy);
        if (payer == null) {
            throw new IllegalArgumentException("Payer not found: " + paidBy);
        }
        
        if (!group.hasMember(paidBy)) {
            throw new IllegalArgumentException("Payer is not a member of the group");
        }
        
        // Get all group members
        List<String> memberIds = new ArrayList<>(group.getMemberIds());
        
        // Get appropriate split strategy
        SplitStrategy strategy = SplitStrategyFactory.getStrategy(splitType);
        
        // Validate split
        if (!strategy.validateSplit(totalAmount, memberIds, splitData)) {
            throw new IllegalArgumentException("Invalid split data for " + splitType + " split");
        }
        
        // Calculate splits
        Map<String, BigDecimal> splits = strategy.calculateSplit(totalAmount, memberIds, splitData);
        
        // Create expense
        String expenseId = "E" + expenseIdCounter.getAndIncrement();
        Expense expense = new Expense(expenseId, groupId, description, totalAmount, 
                                     paidBy, splitType, splits);
        expenses.put(expenseId, expense);
        group.addExpense(expenseId);
        
        System.out.println("\nAdded expense: " + expense);
        System.out.println("Splits:");
        for (Map.Entry<String, BigDecimal> entry : splits.entrySet()) {
            User user = users.get(entry.getKey());
            System.out.println("  " + user.getName() + ": $" + entry.getValue());
        }
        
        // Notify users
        List<User> groupUsers = memberIds.stream()
            .map(users::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        notificationService.notifyExpenseAdded(expense, groupUsers);
        
        return expense;
    }
    
    /**
     * Settle debt between two users in a group
     */
    public Settlement settleDebt(String groupId, String fromUserId, String toUserId, 
                                BigDecimal amount) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        
        User fromUser = users.get(fromUserId);
        User toUser = users.get(toUserId);
        
        if (fromUser == null || toUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        
        if (!group.hasMember(fromUserId) || !group.hasMember(toUserId)) {
            throw new IllegalArgumentException("Both users must be members of the group");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Settlement amount must be positive");
        }
        
        String settlementId = "S" + settlementIdCounter.getAndIncrement();
        Settlement settlement = new Settlement(settlementId, groupId, fromUserId, 
                                             toUserId, amount);
        settlements.put(settlementId, settlement);
        
        System.out.println("\nRecorded settlement: " + settlement);
        notificationService.notifySettlement(settlement, fromUser, toUser);
        
        return settlement;
    }
    
    /**
     * Get user balance in a group
     */
    public Map<String, BigDecimal> getUserBalanceInGroup(String userId, String groupId) {
        return balanceCalculator.getUserBalanceInGroup(userId, groupId);
    }
    
    /**
     * View balances for a specific user in a group
     */
    public void viewUserBalanceInGroup(String userId, String groupId) {
        User user = users.get(userId);
        Group group = groups.get(groupId);
        
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        
        System.out.println("\n========================================");
        System.out.println("Balance for " + user.getName() + " in " + group.getGroupName());
        System.out.println("========================================");
        
        Map<String, BigDecimal> balances = balanceCalculator.getUserBalanceInGroup(userId, groupId);
        
        if (balances.isEmpty()) {
            System.out.println("All settled up!");
        } else {
            for (Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
                User otherUser = users.get(entry.getKey());
                BigDecimal amount = entry.getValue();
                
                if (amount.compareTo(BigDecimal.ZERO) > 0) {
                    System.out.println(otherUser.getName() + " owes you: $" + amount);
                } else if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("You owe " + otherUser.getName() + ": $" + amount.negate());
                }
            }
        }
        System.out.println("========================================\n");
    }
    
    /**
     * View all balances in a group
     */
    public void viewGroupBalances(String groupId) {
        Group group = groups.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found: " + groupId);
        }
        
        System.out.println("\n========================================");
        System.out.println("All Balances in " + group.getGroupName());
        System.out.println("========================================");
        
        Map<String, Map<String, BigDecimal>> balances = 
            balanceCalculator.calculateGroupBalances(groupId);
        
        boolean hasBalances = false;
        for (Map.Entry<String, Map<String, BigDecimal>> entry : balances.entrySet()) {
            String user1Id = entry.getKey();
            User user1 = users.get(user1Id);
            
            for (Map.Entry<String, BigDecimal> balanceEntry : entry.getValue().entrySet()) {
                String user2Id = balanceEntry.getKey();
                User user2 = users.get(user2Id);
                BigDecimal amount = balanceEntry.getValue();
                
                if (amount.compareTo(BigDecimal.ZERO) != 0) {
                    System.out.println(user1.getName() + " owes " + user2.getName() + ": $" + amount);
                    hasBalances = true;
                }
            }
        }
        
        if (!hasBalances) {
            System.out.println("All settled up!");
        }
        System.out.println("========================================\n");
    }
    
    /**
     * Get user by ID
     */
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    /**
     * Get group by ID
     */
    public Group getGroup(String groupId) {
        return groups.get(groupId);
    }
    
    /**
     * Get all users
     */
    public Collection<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    /**
     * Get all groups
     */
    public Collection<Group> getAllGroups() {
        return new ArrayList<>(groups.values());
    }
}

// ========================= MAIN CLASS =========================

/**
 * Main class with demo scenarios
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("    SPLITWISE - EXPENSE MANAGEMENT SYSTEM");
        System.out.println("===========================================\n");
        
        SplitwiseSystem system = SplitwiseSystem.getInstance();
        
        // Demo 1: Create users
        System.out.println("\n--- DEMO 1: Creating Users ---");
        User alice = system.createUser("Alice", "alice@example.com");
        User bob = system.createUser("Bob", "bob@example.com");
        User charlie = system.createUser("Charlie", "charlie@example.com");
        User david = system.createUser("David", "david@example.com");
        
        // Demo 2: Create groups
        System.out.println("\n--- DEMO 2: Creating Groups ---");
        Group roommates = system.createGroup("Roommates");
        Group tripGroup = system.createGroup("Vegas Trip");
        
        // Demo 3: Add users to groups
        System.out.println("\n--- DEMO 3: Adding Users to Groups ---");
        system.addUserToGroup(alice.getUserId(), roommates.getGroupId());
        system.addUserToGroup(bob.getUserId(), roommates.getGroupId());
        system.addUserToGroup(charlie.getUserId(), roommates.getGroupId());
        
        system.addUserToGroup(alice.getUserId(), tripGroup.getGroupId());
        system.addUserToGroup(bob.getUserId(), tripGroup.getGroupId());
        system.addUserToGroup(charlie.getUserId(), tripGroup.getGroupId());
        system.addUserToGroup(david.getUserId(), tripGroup.getGroupId());
        
        // Demo 4: Add equal split expense
        System.out.println("\n--- DEMO 4: Equal Split Expense ---");
        system.addExpense(
            roommates.getGroupId(),
            "Electricity Bill",
            new BigDecimal("150.00"),
            alice.getUserId(),
            SplitType.EQUAL,
            null // No additional data needed for equal split
        );
        
        // Demo 5: View balances after first expense
        System.out.println("\n--- DEMO 5: Viewing Balances ---");
        system.viewGroupBalances(roommates.getGroupId());
        system.viewUserBalanceInGroup(bob.getUserId(), roommates.getGroupId());
        
        // Demo 6: Add exact split expense
        System.out.println("\n--- DEMO 6: Exact Split Expense ---");
        Map<String, BigDecimal> exactSplits = new HashMap<>();
        exactSplits.put(alice.getUserId(), new BigDecimal("30.00"));
        exactSplits.put(bob.getUserId(), new BigDecimal("40.00"));
        exactSplits.put(charlie.getUserId(), new BigDecimal("30.00"));
        
        system.addExpense(
            roommates.getGroupId(),
            "Grocery Shopping",
            new BigDecimal("100.00"),
            bob.getUserId(),
            SplitType.EXACT,
            exactSplits
        );
        
        // Demo 7: Add percentage split expense
        System.out.println("\n--- DEMO 7: Percentage Split Expense ---");
        Map<String, BigDecimal> percentageSplits = new HashMap<>();
        percentageSplits.put(alice.getUserId(), new BigDecimal("25"));
        percentageSplits.put(bob.getUserId(), new BigDecimal("25"));
        percentageSplits.put(charlie.getUserId(), new BigDecimal("25"));
        percentageSplits.put(david.getUserId(), new BigDecimal("25"));
        
        system.addExpense(
            tripGroup.getGroupId(),
            "Hotel Booking",
            new BigDecimal("400.00"),
            charlie.getUserId(),
            SplitType.PERCENTAGE,
            percentageSplits
        );
        
        // Demo 8: View balances in trip group
        System.out.println("\n--- DEMO 8: Trip Group Balances ---");
        system.viewGroupBalances(tripGroup.getGroupId());
        
        // Demo 9: Multiple expenses in roommates group
        System.out.println("\n--- DEMO 9: Adding More Expenses ---");
        system.addExpense(
            roommates.getGroupId(),
            "Internet Bill",
            new BigDecimal("60.00"),
            charlie.getUserId(),
            SplitType.EQUAL,
            null
        );
        
        system.viewGroupBalances(roommates.getGroupId());
        
        // Demo 10: Settle debt
        System.out.println("\n--- DEMO 10: Settling Debts ---");
        Map<String, BigDecimal> bobBalance = system.getUserBalanceInGroup(
            bob.getUserId(), roommates.getGroupId());
        
        // Bob settles with Alice
        for (Map.Entry<String, BigDecimal> entry : bobBalance.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
                // Bob owes this person
                system.settleDebt(
                    roommates.getGroupId(),
                    bob.getUserId(),
                    entry.getKey(),
                    entry.getValue().negate()
                );
            }
        }
        
        // Demo 11: View balances after settlement
        System.out.println("\n--- DEMO 11: Balances After Settlement ---");
        system.viewGroupBalances(roommates.getGroupId());
        system.viewUserBalanceInGroup(bob.getUserId(), roommates.getGroupId());
        
        // Demo 12: Complex scenario with multiple expenses
        System.out.println("\n--- DEMO 12: Complex Scenario ---");
        
        Map<String, BigDecimal> dinnerSplits = new HashMap<>();
        dinnerSplits.put(alice.getUserId(), new BigDecimal("20"));
        dinnerSplits.put(bob.getUserId(), new BigDecimal("30"));
        dinnerSplits.put(charlie.getUserId(), new BigDecimal("25"));
        dinnerSplits.put(david.getUserId(), new BigDecimal("25"));
        
        system.addExpense(
            tripGroup.getGroupId(),
            "Group Dinner",
            new BigDecimal("200.00"),
            david.getUserId(),
            SplitType.PERCENTAGE,
            dinnerSplits
        );
        
        system.addExpense(
            tripGroup.getGroupId(),
            "Taxi to Airport",
            new BigDecimal("80.00"),
            alice.getUserId(),
            SplitType.EQUAL,
            null
        );
        
        system.viewGroupBalances(tripGroup.getGroupId());
        
        // Demo 13: Individual user balance view
        System.out.println("\n--- DEMO 13: Individual Balance Views ---");
        system.viewUserBalanceInGroup(alice.getUserId(), tripGroup.getGroupId());
        system.viewUserBalanceInGroup(bob.getUserId(), tripGroup.getGroupId());
        system.viewUserBalanceInGroup(charlie.getUserId(), tripGroup.getGroupId());
        system.viewUserBalanceInGroup(david.getUserId(), tripGroup.getGroupId());
        
        // Demo 14: Partial settlement
        System.out.println("\n--- DEMO 14: Partial Settlement ---");
        system.settleDebt(
            tripGroup.getGroupId(),
            alice.getUserId(),
            charlie.getUserId(),
            new BigDecimal("50.00")
        );
        
        system.viewGroupBalances(tripGroup.getGroupId());
        
        System.out.println("\n===========================================");
        System.out.println("         DEMO COMPLETED SUCCESSFULLY");
        System.out.println("===========================================");
    }
}
