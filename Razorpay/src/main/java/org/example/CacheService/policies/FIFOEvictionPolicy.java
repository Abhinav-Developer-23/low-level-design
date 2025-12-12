package org.example.CacheService.policies;

import org.example.CacheService.interfaces.IEvictionPolicy;
import org.example.CacheService.model.CacheEntry;

import java.util.LinkedHashMap;

/**
 * First In First Out (FIFO) eviction policy implementation
 * Evicts the oldest entry (first inserted) when cache is full
 * 
 * Data Structure: LinkedHashMap with insertion-order
 * Time Complexity: O(1) for all operations
 * Space Complexity: O(n) where n is number of entries
 * 
 * Pros:
 * - Simple and predictable
 * - Constant time operations
 * - Fair eviction based on insertion time
 * 
 * Cons:
 * - Doesn't consider access patterns
 * - May evict frequently used items
 * - Not optimal for most real-world cache scenarios
 */
public class FIFOEvictionPolicy<K, V> implements IEvictionPolicy<K, V> {
    private final LinkedHashMap<K, Boolean> insertionOrder;
    
    public FIFOEvictionPolicy() {
        // LinkedHashMap with insertion-order (false parameter, which is default)
        // Maintains order based on insertion time
        this.insertionOrder = new LinkedHashMap<>(16, 0.75f, false);
    }
    
    @Override
    public void recordAccess(K key) {
        // FIFO doesn't care about access patterns
        // No operation needed
    }
    
    @Override
    public void recordPut(K key, CacheEntry<K, V> entry) {
        insertionOrder.put(key, Boolean.TRUE);
    }
    
    @Override
    public void recordRemoval(K key) {
        insertionOrder.remove(key);
    }
    
    @Override
    public K evict() {
        // First entry is the oldest (first inserted)
        if (insertionOrder.isEmpty()) {
            return null;
        }
        K keyToEvict = insertionOrder.keySet().iterator().next();
        insertionOrder.remove(keyToEvict);
        return keyToEvict;
    }
    
    @Override
    public void clear() {
        insertionOrder.clear();
    }
    
    @Override
    public int size() {
        return insertionOrder.size();
    }
}

