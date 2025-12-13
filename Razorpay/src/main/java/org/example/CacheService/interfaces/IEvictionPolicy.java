package org.example.CacheService.interfaces;

import org.example.CacheService.model.CacheEntry;

/**
 * Interface for cache eviction policies
 * Defines contract for eviction strategies (Strategy Pattern)
 * Allows different eviction algorithms to be plugged in
 * Follows Open/Closed Principle - open for extension, closed for modification
 */
public interface IEvictionPolicy<K, V> {
    /**
     * Records that a key was accessed
     * Used by eviction policies to track access patterns
     */
    void recordAccess(K key);
    
    /**
     * Notifies policy that a new entry was added
     */
    void recordPut(K key, CacheEntry<K, V> entry);
    
    /**
     * Notifies policy that an entry was removed
     */
    void recordRemoval(K key);
    
    /**
     * Selects a key to evict based on the policy
     * @return The key to evict, or null if no key available
     */
    K evict();
    
    /**
     * Clears all policy state
     */
    void clear();
    
    /**
     * Gets the size of tracked entries
     */
    int size();
}


