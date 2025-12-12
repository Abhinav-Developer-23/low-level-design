package org.example.CacheService.factory;

import org.example.CacheService.enums.EvictionPolicy;
import org.example.CacheService.impl.InMemoryCache;
import org.example.CacheService.interfaces.ICache;
import org.example.CacheService.interfaces.IEvictionPolicy;
import org.example.CacheService.policies.FIFOEvictionPolicy;
import org.example.CacheService.policies.LFUEvictionPolicy;
import org.example.CacheService.policies.LIFOEvictionPolicy;
import org.example.CacheService.policies.LRUEvictionPolicy;

/**
 * Factory class for creating cache instances with different eviction policies
 * Demonstrates Factory Pattern
 * Simplifies cache creation and policy selection
 * Follows Single Responsibility Principle
 */
public class CacheFactory {
    
    /**
     * Creates a cache with specified capacity and eviction policy
     */
    public static <K, V> ICache<K, V> createCache(int capacity, EvictionPolicy policyType) {
        IEvictionPolicy<K, V> policy = createEvictionPolicy(policyType);
        return new InMemoryCache<>(capacity, policy);
    }
    
    /**
     * Creates an eviction policy based on the policy type
     */
    private static <K, V> IEvictionPolicy<K, V> createEvictionPolicy(EvictionPolicy policyType) {
        switch (policyType) {
            case LRU:
                return new LRUEvictionPolicy<>();
            case LFU:
                return new LFUEvictionPolicy<>();
            case FIFO:
                return new FIFOEvictionPolicy<>();
            case LIFO:
                return new LIFOEvictionPolicy<>();
            default:
                throw new IllegalArgumentException("Unknown eviction policy: " + policyType);
        }
    }
    
    /**
     * Creates a cache with custom eviction policy implementation
     * Allows for custom eviction strategies
     */
    public static <K, V> ICache<K, V> createCache(int capacity, IEvictionPolicy<K, V> customPolicy) {
        return new InMemoryCache<>(capacity, customPolicy);
    }
}

