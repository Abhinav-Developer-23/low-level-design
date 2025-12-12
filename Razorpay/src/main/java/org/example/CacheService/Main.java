package org.example.CacheService;

import org.example.CacheService.enums.EvictionPolicy;
import org.example.CacheService.factory.CacheFactory;
import org.example.CacheService.impl.InMemoryCache;
import org.example.CacheService.interfaces.ICache;
import org.example.CacheService.model.CacheStats;

/**
 * Main class demonstrating the Cache Service implementation
 * 
 * Features Demonstrated:
 * 1. Cache creation with different eviction policies (LRU, LFU, FIFO, LIFO)
 * 2. Basic cache operations (put, get, remove, clear)
 * 3. Eviction behavior when cache is full
 * 4. TTL (Time To Live) support for entries
 * 5. Cache statistics and metrics
 * 6. Thread-safe operations
 * 7. Configurable eviction policies via Dependency Injection
 * 
 * Assumptions:
 * 1. Cache capacity is fixed at creation time
 * 2. Null keys and values are not allowed
 * 3. Eviction happens synchronously on put operations
 * 4. Expired entries are removed lazily on access
 * 5. All operations are thread-safe
 * 
 * Data Structures Used:
 * 1. ConcurrentHashMap: Thread-safe storage, O(1) get/put
 *    - Pros: Fast, thread-safe, no locking for reads
 *    - Cons: No ordering guarantees
 * 
 * 2. LinkedHashMap (LRU/FIFO): Maintains insertion/access order
 *    - Pros: O(1) operations, built-in ordering
 *    - Cons: Not thread-safe by default
 * 
 * 3. TreeMap + HashMap (LFU): Frequency-based ordering
 *    - Pros: Efficient frequency tracking
 *    - Cons: O(log n) operations, more complex
 * 
 * Design Patterns Used:
 * - Strategy Pattern: Pluggable eviction policies
 * - Factory Pattern: Cache creation
 * - Dependency Injection: Eviction policy injection
 * - Singleton Pattern: Can be applied for global cache
 * 
 * SOLID Principles:
 * - Single Responsibility: Each class has one clear purpose
 * - Open/Closed: Open for extension via IEvictionPolicy
 * - Liskov Substitution: All eviction policies are substitutable
 * - Interface Segregation: Focused interfaces (ICache, IEvictionPolicy)
 * - Dependency Inversion: Depend on abstractions (interfaces)
 * 
 * Eviction Process:
 * GET:
 *   1. Check if key exists in cache
 *   2. Check if entry has expired (TTL)
 *   3. If valid, record access and return value
 *   4. Update eviction policy metadata
 * 
 * PUT:
 *   1. Check if key already exists (update case)
 *   2. If cache is full, evict based on policy
 *   3. Add new entry to cache
 *   4. Update eviction policy metadata
 *   5. Record statistics
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Cache Service Demo");
        System.out.println("========================================\n");
        
        try {
            // Demo 1: LRU Cache
            System.out.println("\n----- Demo 1: LRU (Least Recently Used) Cache -----");
            demonstrateLRUCache();
            
            Thread.sleep(1000);
            
            // Demo 2: LFU Cache
            System.out.println("\n----- Demo 2: LFU (Least Frequently Used) Cache -----");
            demonstrateLFUCache();
            
            Thread.sleep(1000);
            
            // Demo 3: FIFO Cache
            System.out.println("\n----- Demo 3: FIFO (First In First Out) Cache -----");
            demonstrateFIFOCache();
            
            Thread.sleep(1000);
            
            // Demo 4: TTL (Time To Live)
            System.out.println("\n----- Demo 4: TTL (Time To Live) Feature -----");
            demonstrateTTL();
            
            Thread.sleep(1000);
            
            // Demo 5: Cache Statistics
            System.out.println("\n----- Demo 5: Cache Statistics and Metrics -----");
            demonstrateCacheStats();
            
            Thread.sleep(1000);
            
            // Demo 6: LIFO Cache
            System.out.println("\n----- Demo 6: LIFO (Last In First Out) Cache -----");
            demonstrateLIFOCache();
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Demo interrupted: " + e.getMessage());
        }
        
        System.out.println("\n========================================");
        System.out.println("Demo completed successfully!");
        System.out.println("========================================");
    }
    
    /**
     * Demonstrates LRU eviction policy
     * Evicts least recently accessed item when cache is full
     */
    private static void demonstrateLRUCache() {
        System.out.println("Creating LRU cache with capacity 3");
        ICache<String, String> cache = CacheFactory.createCache(3, EvictionPolicy.LRU);
        
        // Add entries
        System.out.println("\nAdding entries to cache:");
        cache.put("user1", "Alice");
        cache.put("user2", "Bob");
        cache.put("user3", "Charlie");
        
        // Access user1 to make it recently used
        System.out.println("\nAccessing user1 to update LRU order:");
        cache.get("user1");
        
        // Add new entry - should evict user2 (least recently used)
        System.out.println("\nAdding user4 (cache is full, will evict least recently used):");
        cache.put("user4", "David");
        
        // Try to get evicted entry
        System.out.println("\nTrying to get user2 (should be evicted):");
        cache.get("user2");
        
        // Verify remaining entries
        System.out.println("\nVerifying remaining entries:");
        cache.get("user1");
        cache.get("user3");
        cache.get("user4");
        
        printCacheContents(cache);
    }
    
    /**
     * Demonstrates LFU eviction policy
     * Evicts least frequently accessed item when cache is full
     */
    private static void demonstrateLFUCache() {
        System.out.println("Creating LFU cache with capacity 3");
        ICache<String, Integer> cache = CacheFactory.createCache(3, EvictionPolicy.LFU);
        
        // Add entries
        System.out.println("\nAdding entries to cache:");
        cache.put("product1", 100);
        cache.put("product2", 200);
        cache.put("product3", 300);
        
        // Access product1 multiple times (high frequency)
        System.out.println("\nAccessing product1 multiple times (increasing frequency):");
        cache.get("product1");
        cache.get("product1");
        cache.get("product1");
        
        // Access product2 once
        System.out.println("\nAccessing product2 once:");
        cache.get("product2");
        
        // Add new entry - should evict product3 (least frequently used)
        System.out.println("\nAdding product4 (cache is full, will evict least frequently used):");
        cache.put("product4", 400);
        
        // Try to get evicted entry
        System.out.println("\nTrying to get product3 (should be evicted):");
        cache.get("product3");
        
        // Verify remaining entries
        System.out.println("\nVerifying remaining entries:");
        cache.get("product1");
        cache.get("product2");
        cache.get("product4");
        
        printCacheContents(cache);
    }
    
    /**
     * Demonstrates FIFO eviction policy
     * Evicts oldest (first inserted) item when cache is full
     */
    private static void demonstrateFIFOCache() {
        System.out.println("Creating FIFO cache with capacity 3");
        ICache<Integer, String> cache = CacheFactory.createCache(3, EvictionPolicy.FIFO);
        
        // Add entries
        System.out.println("\nAdding entries to cache:");
        cache.put(1, "Order-001");
        cache.put(2, "Order-002");
        cache.put(3, "Order-003");
        
        // Access all entries (FIFO doesn't care about access patterns)
        System.out.println("\nAccessing all entries (FIFO ignores access patterns):");
        cache.get(1);
        cache.get(2);
        cache.get(3);
        
        // Add new entry - should evict entry 1 (first in)
        System.out.println("\nAdding entry 4 (cache is full, will evict first inserted):");
        cache.put(4, "Order-004");
        
        // Try to get evicted entry
        System.out.println("\nTrying to get entry 1 (should be evicted):");
        cache.get(1);
        
        // Verify remaining entries
        System.out.println("\nVerifying remaining entries:");
        cache.get(2);
        cache.get(3);
        cache.get(4);
        
        printCacheContents(cache);
    }
    
    /**
     * Demonstrates TTL (Time To Live) feature
     * Entries expire after specified time
     */
    private static void demonstrateTTL() throws InterruptedException {
        System.out.println("Creating LRU cache with capacity 5");
        ICache<String, String> cache = CacheFactory.createCache(5, EvictionPolicy.LRU);
        
        // Add entry without TTL
        System.out.println("\nAdding session1 without TTL:");
        cache.put("session1", "permanent-token");
        
        // Add entry with 2 second TTL
        System.out.println("Adding session2 with 2 second TTL:");
        cache.put("session2", "temporary-token", 2000);
        
        // Get entry immediately
        System.out.println("\nGetting session2 immediately:");
        cache.get("session2");
        
        // Wait for expiration
        System.out.println("\nWaiting 3 seconds for session2 to expire...");
        Thread.sleep(3000);
        
        // Try to get expired entry
        System.out.println("Getting session2 after expiration:");
        cache.get("session2");
        
        // Verify permanent entry still exists
        System.out.println("\nVerifying session1 still exists:");
        cache.get("session1");
        
        printCacheContents(cache);
    }
    
    /**
     * Demonstrates cache statistics and metrics
     */
    private static void demonstrateCacheStats() {
        System.out.println("Creating LRU cache with capacity 3 for statistics demo");
        ICache<String, String> cache = CacheFactory.createCache(3, EvictionPolicy.LRU);
        
        System.out.println("\nPerforming various cache operations:");
        
        // Puts
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        
        // Hits
        cache.get("key1");
        cache.get("key2");
        cache.get("key1");
        
        // Misses
        cache.get("key4");
        cache.get("key5");
        
        // Eviction
        cache.put("key4", "value4");
        
        // Remove
        cache.remove("key4");
        
        // Get statistics
        CacheStats stats = cache.getStats();
        System.out.println("\n===== Cache Statistics =====");
        System.out.println(stats);
        System.out.println("============================");
    }
    
    /**
     * Demonstrates LIFO eviction policy
     * Evicts most recently added item when cache is full
     */
    private static void demonstrateLIFOCache() {
        System.out.println("Creating LIFO cache with capacity 3");
        ICache<String, String> cache = CacheFactory.createCache(3, EvictionPolicy.LIFO);
        
        // Add entries
        System.out.println("\nAdding entries to cache:");
        cache.put("task1", "First Task");
        cache.put("task2", "Second Task");
        cache.put("task3", "Third Task");
        
        // Add new entry - should evict task3 (last in)
        System.out.println("\nAdding task4 (cache is full, will evict last inserted):");
        cache.put("task4", "Fourth Task");
        
        // Try to get evicted entry
        System.out.println("\nTrying to get task3 (should be evicted):");
        cache.get("task3");
        
        // Verify remaining entries
        System.out.println("\nVerifying remaining entries:");
        cache.get("task1");
        cache.get("task2");
        cache.get("task4");
        
        printCacheContents(cache);
    }
    
    /**
     * Helper method to print cache contents
     */
    private static <K, V> void printCacheContents(ICache<K, V> cache) {
        if (cache instanceof InMemoryCache) {
            ((InMemoryCache<K, V>) cache).printCache();
        }
    }
}
