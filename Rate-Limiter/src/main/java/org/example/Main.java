package org.example;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

// ==================== ENUMS ====================

/**
 * Enum representing different rate limiter algorithms
 */
enum RateLimiterType {
    TOKEN_BUCKET,
    FIXED_WINDOW_COUNTER
}

/**
 * Enum representing request status
 */
enum RequestStatus {
    ALLOWED,
    REJECTED,
    THROTTLED
}

// ==================== INTERFACES ====================

/**
 * Strategy Pattern: Interface for different rate limiting algorithms
 * Follows Open/Closed Principle - open for extension, closed for modification
 */
interface RateLimiterStrategy {
    boolean allowRequest(String clientId);
    RequestStatus getRequestStatus(String clientId);
    void reset(String clientId);
    String getStrategyName();
}

/**
 * Interface for rate limiter configuration
 */
interface RateLimiterConfig {
    int getMaxRequests();
    long getTimeWindowMillis();
    RateLimiterType getType();
}

/**
 * Observer Pattern: Interface for monitoring rate limiter events
 */
interface RateLimiterObserver {
    void onRequestAllowed(String clientId, String strategy);
    void onRequestRejected(String clientId, String strategy);
    void onRateLimitExceeded(String clientId, String strategy, int attempts);
}

// ==================== ABSTRACT CLASSES ====================

/**
 * Abstract base class for rate limiter strategies
 * Provides common functionality and template for concrete implementations
 */
abstract class AbstractRateLimiterStrategy implements RateLimiterStrategy {
    protected final int maxRequests;
    protected final long timeWindowMillis;
    protected final Map<String, Object> clientData;
    protected final ReentrantLock lock;
    protected final List<RateLimiterObserver> observers;

    public AbstractRateLimiterStrategy(int maxRequests, long timeWindowMillis) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeWindowMillis;
        this.clientData = new ConcurrentHashMap<>();
        this.lock = new ReentrantLock();
        this.observers = new ArrayList<>();
    }

    public void addObserver(RateLimiterObserver observer) {
        observers.add(observer);
    }

    protected void notifyRequestAllowed(String clientId) {
        for (RateLimiterObserver observer : observers) {
            observer.onRequestAllowed(clientId, getStrategyName());
        }
    }

    protected void notifyRequestRejected(String clientId) {
        for (RateLimiterObserver observer : observers) {
            observer.onRequestRejected(clientId, getStrategyName());
        }
    }

    @Override
    public void reset(String clientId) {
        clientData.remove(clientId);
    }

    @Override
    public RequestStatus getRequestStatus(String clientId) {
        return allowRequest(clientId) ? RequestStatus.ALLOWED : RequestStatus.REJECTED;
    }
}

// ==================== MODEL CLASSES ====================

/**
 * Model class representing token bucket state for a client
 * Encapsulates token bucket data
 */
class TokenBucketState {
    private double tokens;
    private long lastRefillTimestamp;
    private final double refillRate;
    private final int capacity;

    public TokenBucketState(int capacity, long timeWindowMillis) {
        this.tokens = capacity;
        this.capacity = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
        // Calculate refill rate: tokens per millisecond
        this.refillRate = (double) capacity / timeWindowMillis;
    }

    public synchronized boolean consumeToken() {
        refillTokens();
        if (tokens >= 1.0) {
            tokens -= 1.0;
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - lastRefillTimestamp;
        double tokensToAdd = timePassed * refillRate;
        
        tokens = Math.min(capacity, tokens + tokensToAdd);
        lastRefillTimestamp = currentTime;
    }

    public double getTokens() {
        refillTokens();
        return tokens;
    }

    public int getCapacity() {
        return capacity;
    }
}

/**
 * Model class representing fixed window counter state
 * Encapsulates window counter data
 */
class FixedWindowState {
    private int requestCount;
    private long windowStartTime;
    private final long windowDuration;

    public FixedWindowState(long windowDuration) {
        this.requestCount = 0;
        this.windowStartTime = System.currentTimeMillis();
        this.windowDuration = windowDuration;
    }

    public synchronized boolean allowRequest(int maxRequests) {
        long currentTime = System.currentTimeMillis();
        
        // Check if window has expired
        if (currentTime - windowStartTime >= windowDuration) {
            // Start new window
            windowStartTime = currentTime;
            requestCount = 0;
        }

        // Check if request can be allowed
        if (requestCount < maxRequests) {
            requestCount++;
            return true;
        }
        return false;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public long getTimeUntilReset() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - windowStartTime;
        return Math.max(0, windowDuration - elapsed);
    }
}

/**
 * Configuration class for rate limiter
 * Follows Builder Pattern for flexible configuration
 */
class RateLimiterConfiguration implements RateLimiterConfig {
    private final int maxRequests;
    private final long timeWindowMillis;
    private final RateLimiterType type;

    private RateLimiterConfiguration(Builder builder) {
        this.maxRequests = builder.maxRequests;
        this.timeWindowMillis = builder.timeWindowMillis;
        this.type = builder.type;
    }

    @Override
    public int getMaxRequests() {
        return maxRequests;
    }

    @Override
    public long getTimeWindowMillis() {
        return timeWindowMillis;
    }

    @Override
    public RateLimiterType getType() {
        return type;
    }

    /**
     * Builder Pattern for constructing RateLimiterConfiguration
     */
    public static class Builder {
        private int maxRequests = 10;
        private long timeWindowMillis = 60000; // 1 minute default
        private RateLimiterType type = RateLimiterType.TOKEN_BUCKET;

        public Builder maxRequests(int maxRequests) {
            this.maxRequests = maxRequests;
            return this;
        }

        public Builder timeWindowMillis(long timeWindowMillis) {
            this.timeWindowMillis = timeWindowMillis;
            return this;
        }

        public Builder timeWindowSeconds(long seconds) {
            this.timeWindowMillis = seconds * 1000;
            return this;
        }

        public Builder type(RateLimiterType type) {
            this.type = type;
            return this;
        }

        public RateLimiterConfiguration build() {
            return new RateLimiterConfiguration(this);
        }
    }
}

/**
 * Model class representing rate limiter statistics
 */
class RateLimiterStats {
    private final String clientId;
    private int allowedRequests;
    private int rejectedRequests;
    private long lastRequestTime;

    public RateLimiterStats(String clientId) {
        this.clientId = clientId;
        this.allowedRequests = 0;
        this.rejectedRequests = 0;
        this.lastRequestTime = System.currentTimeMillis();
    }

    public void incrementAllowed() {
        allowedRequests++;
        lastRequestTime = System.currentTimeMillis();
    }

    public void incrementRejected() {
        rejectedRequests++;
        lastRequestTime = System.currentTimeMillis();
    }

    public String getClientId() {
        return clientId;
    }

    public int getAllowedRequests() {
        return allowedRequests;
    }

    public int getRejectedRequests() {
        return rejectedRequests;
    }

    public int getTotalRequests() {
        return allowedRequests + rejectedRequests;
    }

    public double getRejectionRate() {
        int total = getTotalRequests();
        return total == 0 ? 0.0 : (double) rejectedRequests / total * 100;
    }
}

// ==================== CONCRETE STRATEGY IMPLEMENTATIONS ====================

/**
 * Token Bucket Algorithm Implementation
 * Allows burst traffic while maintaining average rate limit
 */
class TokenBucketStrategy extends AbstractRateLimiterStrategy {
    
    public TokenBucketStrategy(int maxRequests, long timeWindowMillis) {
        super(maxRequests, timeWindowMillis);
    }

    @Override
    public boolean allowRequest(String clientId) {
        TokenBucketState bucket = (TokenBucketState) clientData.computeIfAbsent(
            clientId, 
            k -> new TokenBucketState(maxRequests, timeWindowMillis)
        );

        boolean allowed = bucket.consumeToken();
        
        if (allowed) {
            notifyRequestAllowed(clientId);
        } else {
            notifyRequestRejected(clientId);
        }
        
        return allowed;
    }

    @Override
    public String getStrategyName() {
        return "Token Bucket";
    }

    public double getAvailableTokens(String clientId) {
        TokenBucketState bucket = (TokenBucketState) clientData.get(clientId);
        return bucket != null ? bucket.getTokens() : maxRequests;
    }
}

/**
 * Fixed Window Counter Algorithm Implementation
 * Simple and memory efficient, but can allow burst at window boundaries
 */
class FixedWindowCounterStrategy extends AbstractRateLimiterStrategy {
    
    public FixedWindowCounterStrategy(int maxRequests, long timeWindowMillis) {
        super(maxRequests, timeWindowMillis);
    }

    @Override
    public boolean allowRequest(String clientId) {
        FixedWindowState window = (FixedWindowState) clientData.computeIfAbsent(
            clientId,
            k -> new FixedWindowState(timeWindowMillis)
        );

        boolean allowed = window.allowRequest(maxRequests);
        
        if (allowed) {
            notifyRequestAllowed(clientId);
        } else {
            notifyRequestRejected(clientId);
        }
        
        return allowed;
    }

    @Override
    public String getStrategyName() {
        return "Fixed Window Counter";
    }

    public int getCurrentRequestCount(String clientId) {
        FixedWindowState window = (FixedWindowState) clientData.get(clientId);
        return window != null ? window.getRequestCount() : 0;
    }

    public long getTimeUntilReset(String clientId) {
        FixedWindowState window = (FixedWindowState) clientData.get(clientId);
        return window != null ? window.getTimeUntilReset() : timeWindowMillis;
    }
}

// ==================== FACTORY PATTERN ====================

/**
 * Factory Pattern: Creates appropriate rate limiter strategy based on configuration
 * Follows Single Responsibility Principle
 */
class RateLimiterStrategyFactory {
    
    public static RateLimiterStrategy createStrategy(RateLimiterConfig config) {
        switch (config.getType()) {
            case TOKEN_BUCKET:
                return new TokenBucketStrategy(
                    config.getMaxRequests(), 
                    config.getTimeWindowMillis()
                );
            case FIXED_WINDOW_COUNTER:
                return new FixedWindowCounterStrategy(
                    config.getMaxRequests(), 
                    config.getTimeWindowMillis()
                );
            default:
                throw new IllegalArgumentException("Unknown rate limiter type: " + config.getType());
        }
    }
}

// ==================== OBSERVER IMPLEMENTATIONS ====================

/**
 * Logging observer for rate limiter events
 */
class LoggingRateLimiterObserver implements RateLimiterObserver {
    
    @Override
    public void onRequestAllowed(String clientId, String strategy) {
        System.out.println("[" + Instant.now() + "] ✓ Request ALLOWED for client: " + clientId + " | Strategy: " + strategy);
    }

    @Override
    public void onRequestRejected(String clientId, String strategy) {
        System.out.println("[" + Instant.now() + "] ✗ Request REJECTED for client: " + clientId + " | Strategy: " + strategy);
    }

    @Override
    public void onRateLimitExceeded(String clientId, String strategy, int attempts) {
        System.out.println("[" + Instant.now() + "] ⚠ Rate limit EXCEEDED for client: " + clientId + 
                         " | Attempts: " + attempts + " | Strategy: " + strategy);
    }
}

/**
 * Statistics tracking observer
 */
class StatsTrackingObserver implements RateLimiterObserver {
    private final Map<String, RateLimiterStats> statsMap;

    public StatsTrackingObserver() {
        this.statsMap = new ConcurrentHashMap<>();
    }

    @Override
    public void onRequestAllowed(String clientId, String strategy) {
        getStats(clientId).incrementAllowed();
    }

    @Override
    public void onRequestRejected(String clientId, String strategy) {
        getStats(clientId).incrementRejected();
    }

    @Override
    public void onRateLimitExceeded(String clientId, String strategy, int attempts) {
        // Additional tracking can be added here
    }

    private RateLimiterStats getStats(String clientId) {
        return statsMap.computeIfAbsent(clientId, RateLimiterStats::new);
    }

    public RateLimiterStats getClientStats(String clientId) {
        return statsMap.get(clientId);
    }

    public void printAllStats() {
        System.out.println("\n========== RATE LIMITER STATISTICS ==========");
        for (RateLimiterStats stats : statsMap.values()) {
            System.out.printf("Client: %s | Allowed: %d | Rejected: %d | Total: %d | Rejection Rate: %.2f%%\n",
                stats.getClientId(),
                stats.getAllowedRequests(),
                stats.getRejectedRequests(),
                stats.getTotalRequests(),
                stats.getRejectionRate()
            );
        }
        System.out.println("===========================================\n");
    }
}

// ==================== MAIN RATE LIMITER CLASS ====================

/**
 * Main Rate Limiter class
 * Facade Pattern: Provides simplified interface to complex rate limiting subsystem
 * Follows Dependency Inversion Principle - depends on abstractions (interfaces)
 */
class RateLimiter {
    private final RateLimiterStrategy strategy;
    private final RateLimiterConfig config;
    private final StatsTrackingObserver statsObserver;

    public RateLimiter(RateLimiterConfig config) {
        this.config = config;
        this.strategy = RateLimiterStrategyFactory.createStrategy(config);
        this.statsObserver = new StatsTrackingObserver();
        
        // Add observers
        if (strategy instanceof AbstractRateLimiterStrategy) {
            AbstractRateLimiterStrategy abstractStrategy = (AbstractRateLimiterStrategy) strategy;
            abstractStrategy.addObserver(new LoggingRateLimiterObserver());
            abstractStrategy.addObserver(statsObserver);
        }
    }

    /**
     * Check if request is allowed for given client
     */
    public boolean allowRequest(String clientId) {
        return strategy.allowRequest(clientId);
    }

    /**
     * Get request status for given client
     */
    public RequestStatus getRequestStatus(String clientId) {
        return strategy.getRequestStatus(clientId);
    }

    /**
     * Reset rate limiter for specific client
     */
    public void resetClient(String clientId) {
        strategy.reset(clientId);
    }

    /**
     * Get statistics for specific client
     */
    public RateLimiterStats getClientStats(String clientId) {
        return statsObserver.getClientStats(clientId);
    }

    /**
     * Print all statistics
     */
    public void printStatistics() {
        statsObserver.printAllStats();
    }

    public String getStrategyName() {
        return strategy.getStrategyName();
    }

    public RateLimiterConfig getConfig() {
        return config;
    }
}

// ==================== RATE LIMITER MANAGER ====================

/**
 * Manager class to handle multiple rate limiters
 * Singleton Pattern: Ensures single instance of manager
 */
class RateLimiterManager {
    private static RateLimiterManager instance;
    private final Map<String, RateLimiter> rateLimiters;

    private RateLimiterManager() {
        this.rateLimiters = new ConcurrentHashMap<>();
    }

    public static synchronized RateLimiterManager getInstance() {
        if (instance == null) {
            instance = new RateLimiterManager();
        }
        return instance;
    }

    public void addRateLimiter(String name, RateLimiter rateLimiter) {
        rateLimiters.put(name, rateLimiter);
    }

    public RateLimiter getRateLimiter(String name) {
        return rateLimiters.get(name);
    }

    public boolean allowRequest(String rateLimiterName, String clientId) {
        RateLimiter rateLimiter = rateLimiters.get(rateLimiterName);
        if (rateLimiter == null) {
            throw new IllegalArgumentException("Rate limiter not found: " + rateLimiterName);
        }
        return rateLimiter.allowRequest(clientId);
    }

    public void printAllStatistics() {
        System.out.println("\n========== ALL RATE LIMITERS STATISTICS ==========");
        for (Map.Entry<String, RateLimiter> entry : rateLimiters.entrySet()) {
            System.out.println("\n>>> Rate Limiter: " + entry.getKey() + 
                             " | Strategy: " + entry.getValue().getStrategyName());
            entry.getValue().printStatistics();
        }
    }
}

// ==================== DEMO / MAIN CLASS ====================

/**
 * Main class to demonstrate Rate Limiter functionality
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("========== RATE LIMITER DEMO ==========\n");
        
        // Demo 1: Token Bucket Strategy
        demoTokenBucket();
        
        // Demo 2: Fixed Window Counter Strategy
        demoFixedWindowCounter();
        
        // Demo 3: Multiple Clients
        demoMultipleClients();
        
        // Demo 4: Rate Limiter Manager
        demoRateLimiterManager();
        
        System.out.println("\n========== DEMO COMPLETED ==========");
    }

    /**
     * Demo: Token Bucket Strategy
     * Allows burst traffic but maintains average rate
     */
    private static void demoTokenBucket() {
        System.out.println("\n========== DEMO 1: TOKEN BUCKET STRATEGY ==========");
        
        RateLimiterConfig config = new RateLimiterConfiguration.Builder()
            .maxRequests(5)
            .timeWindowSeconds(10)
            .type(RateLimiterType.TOKEN_BUCKET)
            .build();
        
        RateLimiter rateLimiter = new RateLimiter(config);
        String clientId = "client-token-bucket-1";
        
        System.out.println("Config: " + config.getMaxRequests() + " requests per " + 
                         (config.getTimeWindowMillis() / 1000) + " seconds");
        System.out.println("Testing burst of 8 requests...\n");
        
        // Burst of requests
        for (int i = 1; i <= 8; i++) {
            System.out.println("Request #" + i + ":");
            rateLimiter.allowRequest(clientId);
            sleep(100); // Small delay between requests
        }
        
        rateLimiter.printStatistics();
        
        System.out.println("Waiting 3 seconds for token refill...\n");
        sleep(3000);
        
        System.out.println("Trying 3 more requests after refill:");
        for (int i = 9; i <= 11; i++) {
            System.out.println("Request #" + i + ":");
            rateLimiter.allowRequest(clientId);
            sleep(100);
        }
        
        rateLimiter.printStatistics();
    }

    /**
     * Demo: Fixed Window Counter Strategy
     * Simple counter that resets after time window
     */
    private static void demoFixedWindowCounter() {
        System.out.println("\n========== DEMO 2: FIXED WINDOW COUNTER STRATEGY ==========");
        
        RateLimiterConfig config = new RateLimiterConfiguration.Builder()
            .maxRequests(3)
            .timeWindowSeconds(5)
            .type(RateLimiterType.FIXED_WINDOW_COUNTER)
            .build();
        
        RateLimiter rateLimiter = new RateLimiter(config);
        String clientId = "client-fixed-window-1";
        
        System.out.println("Config: " + config.getMaxRequests() + " requests per " + 
                         (config.getTimeWindowMillis() / 1000) + " seconds");
        System.out.println("Testing 5 requests in current window...\n");
        
        // Try requests within window
        for (int i = 1; i <= 5; i++) {
            System.out.println("Request #" + i + ":");
            rateLimiter.allowRequest(clientId);
            sleep(100);
        }
        
        rateLimiter.printStatistics();
        
        System.out.println("Waiting for window reset (6 seconds)...\n");
        sleep(6000);
        
        System.out.println("Trying 3 more requests in new window:");
        for (int i = 6; i <= 8; i++) {
            System.out.println("Request #" + i + ":");
            rateLimiter.allowRequest(clientId);
            sleep(100);
        }
        
        rateLimiter.printStatistics();
    }

    /**
     * Demo: Multiple Clients
     * Each client has independent rate limit
     */
    private static void demoMultipleClients() {
        System.out.println("\n========== DEMO 3: MULTIPLE CLIENTS ==========");
        
        RateLimiterConfig config = new RateLimiterConfiguration.Builder()
            .maxRequests(3)
            .timeWindowSeconds(5)
            .type(RateLimiterType.TOKEN_BUCKET)
            .build();
        
        RateLimiter rateLimiter = new RateLimiter(config);
        
        System.out.println("Config: " + config.getMaxRequests() + " requests per " + 
                         (config.getTimeWindowMillis() / 1000) + " seconds per client");
        System.out.println("Testing 3 different clients...\n");
        
        String[] clients = {"client-A", "client-B", "client-C"};
        
        // Each client makes 4 requests
        for (String client : clients) {
            System.out.println("\n--- Testing " + client + " ---");
            for (int i = 1; i <= 4; i++) {
                System.out.println(client + " - Request #" + i + ":");
                rateLimiter.allowRequest(client);
                sleep(50);
            }
        }
        
        rateLimiter.printStatistics();
    }

    /**
     * Demo: Rate Limiter Manager
     * Managing multiple rate limiters with different configs
     */
    private static void demoRateLimiterManager() {
        System.out.println("\n========== DEMO 4: RATE LIMITER MANAGER ==========");
        
        RateLimiterManager manager = RateLimiterManager.getInstance();
        
        // Create API rate limiter (Token Bucket)
        RateLimiterConfig apiConfig = new RateLimiterConfiguration.Builder()
            .maxRequests(10)
            .timeWindowSeconds(60)
            .type(RateLimiterType.TOKEN_BUCKET)
            .build();
        RateLimiter apiLimiter = new RateLimiter(apiConfig);
        manager.addRateLimiter("API", apiLimiter);
        
        // Create Login rate limiter (Fixed Window)
        RateLimiterConfig loginConfig = new RateLimiterConfiguration.Builder()
            .maxRequests(3)
            .timeWindowSeconds(10)
            .type(RateLimiterType.FIXED_WINDOW_COUNTER)
            .build();
        RateLimiter loginLimiter = new RateLimiter(loginConfig);
        manager.addRateLimiter("LOGIN", loginLimiter);
        
        System.out.println("Created two rate limiters: API and LOGIN\n");
        
        String userId = "user-123";
        
        // Test API rate limiter
        System.out.println("--- Testing API Rate Limiter (10 requests) ---");
        for (int i = 1; i <= 12; i++) {
            System.out.println("API Request #" + i + ":");
            manager.allowRequest("API", userId);
            sleep(50);
        }
        
        System.out.println("\n--- Testing LOGIN Rate Limiter (3 requests) ---");
        for (int i = 1; i <= 5; i++) {
            System.out.println("Login Attempt #" + i + ":");
            manager.allowRequest("LOGIN", userId);
            sleep(100);
        }
        
        manager.printAllStatistics();
    }

    /**
     * Helper method to sleep without checked exception
     */
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}