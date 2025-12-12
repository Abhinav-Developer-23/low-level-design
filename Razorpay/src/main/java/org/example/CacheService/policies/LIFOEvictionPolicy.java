package org.example.CacheService.policies;

import org.example.CacheService.interfaces.IEvictionPolicy;
import org.example.CacheService.model.CacheEntry;

import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Last In First Out (LIFO) eviction policy implementation
 * Evicts the most recently added entry when cache is full
 * 
 * Data Structure: Stack for tracking insertion order
 * Time Complexity: O(1) for all operations
 * Space Complexity: O(n) where n is number of entries
 * 
 * Pros:
 * - Simple stack-based implementation
 * - Constant time operations
 * - Useful for specific use cases (e.g., undo operations)
 * 
 * Cons:
 * - Rarely used in practice for general caching
 * - Counter-intuitive for most cache scenarios
 * - May evict recently added useful data
 */
public class LIFOEvictionPolicy<K, V> implements IEvictionPolicy<K, V> {
    private final Stack<K> insertionStack;
    private final ConcurrentHashMap<K, Boolean> keySet;
    
    public LIFOEvictionPolicy() {
        this.insertionStack = new Stack<>();
        this.keySet = new ConcurrentHashMap<>();
    }
    
    @Override
    public void recordAccess(K key) {
        // LIFO doesn't care about access patterns
        // No operation needed
    }
    
    @Override
    public synchronized void recordPut(K key, CacheEntry<K, V> entry) {
        if (!keySet.containsKey(key)) {
            insertionStack.push(key);
            keySet.put(key, Boolean.TRUE);
        }
    }
    
    @Override
    public synchronized void recordRemoval(K key) {
        keySet.remove(key);
        insertionStack.remove(key);
    }
    
    @Override
    public synchronized K evict() {
        // Last entry is the most recently added
        if (insertionStack.isEmpty()) {
            return null;
        }
        K keyToEvict = insertionStack.pop();
        keySet.remove(keyToEvict);
        return keyToEvict;
    }
    
    @Override
    public synchronized void clear() {
        insertionStack.clear();
        keySet.clear();
    }
    
    @Override
    public int size() {
        return insertionStack.size();
    }
}

