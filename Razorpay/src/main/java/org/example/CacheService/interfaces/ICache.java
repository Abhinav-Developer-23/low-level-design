package org.example.CacheService.interfaces;

import org.example.CacheService.model.CacheStats;

import java.util.Optional;

/**
 * Interface for Cache operations
 * Defines contract for cache implementations
 * Generic interface supporting any key-value types
 * Follows Interface Segregation Principle (ISP)
 */
public interface ICache<K, V> {
    /**
     * Retrieves value associated with key from cache
     * @param key The key to lookup
     * @return Optional containing value if found, empty otherwise
     */
    Optional<V> get(K key);
    
    /**
     * Stores a key-value pair in the cache
     * @param key The key to store
     * @param value The value to store
     */
    void put(K key, V value);
    
    /**
     * Stores a key-value pair with TTL (Time To Live)
     * @param key The key to store
     * @param value The value to store
     * @param ttlMillis Time to live in milliseconds
     */
    void put(K key, V value, long ttlMillis);
    
    /**
     * Removes a key-value pair from cache
     * @param key The key to remove
     * @return true if removed, false if key not found
     */
    boolean remove(K key);
    
    /**
     * Checks if cache contains a key
     * @param key The key to check
     * @return true if key exists, false otherwise
     */
    boolean containsKey(K key);
    
    /**
     * Clears all entries from the cache
     */
    void clear();
    
    /**
     * Gets the current size of the cache
     * @return Number of entries in cache
     */
    int size();
    
    /**
     * Gets the maximum capacity of the cache
     * @return Maximum number of entries
     */
    int getCapacity();
    
    /**
     * Checks if cache is empty
     * @return true if empty, false otherwise
     */
    boolean isEmpty();
    
    /**
     * Gets cache statistics
     * @return CacheStats object with metrics
     */
    CacheStats getStats();
}




