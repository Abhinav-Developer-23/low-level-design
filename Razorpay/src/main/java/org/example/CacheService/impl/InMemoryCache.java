package org.example.CacheService.impl;

import org.example.CacheService.interfaces.ICache;
import org.example.CacheService.interfaces.IEvictionPolicy;
import org.example.CacheService.model.CacheEntry;
import org.example.CacheService.model.CacheStats;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe in-memory cache implementation
 * Uses ConcurrentHashMap for storage and ReadWriteLock for eviction operations
 * Supports configurable eviction policies via dependency injection
 * 
 * Design Patterns:
 * - Strategy Pattern: Pluggable eviction policies
 * - Dependency Injection: Eviction policy injected via constructor
 * 
 * Thread Safety:
 * - ConcurrentHashMap for concurrent reads/writes
 * - ReadWriteLock for coordinating eviction operations
 * 
 * SOLID Principles:
 * - Single Responsibility: Manages cache operations only
 * - Open/Closed: Open for extension via IEvictionPolicy
 * - Dependency Inversion: Depends on IEvictionPolicy abstraction
 */
public class InMemoryCache<K, V> implements ICache<K, V> {
    private final int capacity;
    private final ConcurrentHashMap<K, CacheEntry<K, V>> cache;
    private final IEvictionPolicy<K, V> evictionPolicy;
    private final CacheStats stats;
    private final ReadWriteLock lock;
    
    /**
     * Constructor with configurable capacity and eviction policy
     * Demonstrates Dependency Injection principle
     */
    public InMemoryCache(int capacity, IEvictionPolicy<K, V> evictionPolicy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Cache capacity must be positive");
        }
        if (evictionPolicy == null) {
            throw new IllegalArgumentException("Eviction policy cannot be null");
        }
        
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>();
        this.evictionPolicy = evictionPolicy;
        this.stats = new CacheStats();
        this.lock = new ReentrantReadWriteLock();
    }
    
    @Override
    public Optional<V> get(K key) {
        if (key == null) {
            return Optional.empty();
        }
        
        lock.readLock().lock();
        try {
            CacheEntry<K, V> entry = cache.get(key);
            
            // Check if entry exists and is not expired
            if (entry == null) {
                stats.recordMiss();
                System.out.println("[Cache] MISS: Key '" + key + "' not found");
                return Optional.empty();
            }
            
            // Check for expiration
            if (entry.isExpired()) {
                stats.recordExpiration();
                stats.recordMiss();
                System.out.println("[Cache] EXPIRED: Key '" + key + "' has expired");
                // Remove expired entry asynchronously
                removeExpiredEntry(key);
                return Optional.empty();
            }
            
            // Record access for eviction policy
            entry.recordAccess();
            evictionPolicy.recordAccess(key);
            stats.recordHit();
            
            System.out.println("[Cache] HIT: Key '" + key + "' = " + entry.getValue());
            return Optional.of(entry.getValue());
            
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public void put(K key, V value) {
        put(key, value, -1);
    }
    
    @Override
    public void put(K key, V value, long ttlMillis) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Key and value cannot be null");
        }
        
        lock.writeLock().lock();
        try {
            // Check if key already exists (update case)
            if (cache.containsKey(key)) {
                CacheEntry<K, V> existingEntry = cache.get(key);
                existingEntry.updateValue(value);
                evictionPolicy.recordAccess(key);
                stats.recordPut();
                System.out.println("[Cache] UPDATED: Key '" + key + "' = " + value);
                return;
            }
            
            // Check if cache is full - need to evict
            if (cache.size() >= capacity) {
                evict();
            }
            
            // Add new entry
            CacheEntry<K, V> newEntry = new CacheEntry<>(key, value, ttlMillis);
            cache.put(key, newEntry);
            evictionPolicy.recordPut(key, newEntry);
            stats.recordPut();
            
            if (ttlMillis > 0) {
                System.out.println("[Cache] PUT: Key '" + key + "' = " + value + " (TTL: " + ttlMillis + "ms)");
            } else {
                System.out.println("[Cache] PUT: Key '" + key + "' = " + value);
            }
            
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean remove(K key) {
        if (key == null) {
            return false;
        }
        
        lock.writeLock().lock();
        try {
            CacheEntry<K, V> removed = cache.remove(key);
            if (removed != null) {
                evictionPolicy.recordRemoval(key);
                stats.recordRemove();
                System.out.println("[Cache] REMOVED: Key '" + key + "'");
                return true;
            }
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        
        lock.readLock().lock();
        try {
            CacheEntry<K, V> entry = cache.get(key);
            return entry != null && !entry.isExpired();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            cache.clear();
            evictionPolicy.clear();
            System.out.println("[Cache] CLEARED: All entries removed");
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    @Override
    public int size() {
        return cache.size();
    }
    
    @Override
    public int getCapacity() {
        return capacity;
    }
    
    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }
    
    @Override
    public CacheStats getStats() {
        return stats;
    }
    
    /**
     * Private method to handle eviction
     * Called when cache is full and new entry needs to be added
     */
    private void evict() {
        K keyToEvict = evictionPolicy.evict();
        if (keyToEvict != null) {
            cache.remove(keyToEvict);
            stats.recordEviction();
            System.out.println("[Cache] EVICTED: Key '" + keyToEvict + "' removed due to capacity limit");
        }
    }
    
    /**
     * Removes an expired entry
     * Called when get() finds an expired entry
     */
    private void removeExpiredEntry(K key) {
        // Use a separate thread to avoid blocking the read operation
        new Thread(() -> {
            lock.writeLock().lock();
            try {
                cache.remove(key);
                evictionPolicy.recordRemoval(key);
            } finally {
                lock.writeLock().unlock();
            }
        }).start();
    }
    
    /**
     * Prints cache contents for debugging
     */
    public void printCache() {
        System.out.println("\n===== Cache Contents =====");
        System.out.println("Size: " + size() + "/" + capacity);
        lock.readLock().lock();
        try {
            cache.forEach((key, entry) -> {
                System.out.println("  " + key + " -> " + entry.getValue() + 
                                 " (accessCount=" + entry.getAccessCount() + ")");
            });
        } finally {
            lock.readLock().unlock();
        }
        System.out.println("==========================\n");
    }
}



