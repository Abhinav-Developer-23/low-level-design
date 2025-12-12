package org.example.CacheService.policies;

import org.example.CacheService.interfaces.IEvictionPolicy;
import org.example.CacheService.model.CacheEntry;

import java.util.LinkedHashMap;

/**
 * Least Recently Used (LRU) eviction policy implementation
 * Evicts the least recently accessed entry when cache is full
 * 
 * Data Structure: LinkedHashMap with access-order
 * Time Complexity: O(1) for all operations
 * Space Complexity: O(n) where n is number of entries
 * 
 * Pros:
 * - Constant time operations
 * - Good for temporal locality patterns
 * - Simple to understand and implement
 * 
 * Cons:
 * - Doesn't consider access frequency
 * - May evict frequently used items that weren't accessed recently
 */
public class LRUEvictionPolicy<K, V> implements IEvictionPolicy<K, V> {
    private final LinkedHashMap<K, Boolean> accessOrder;
    
    public LRUEvictionPolicy() {
        // LinkedHashMap with access-order (true parameter)
        // Maintains order based on access time
        this.accessOrder = new LinkedHashMap<>(16, 0.75f, true);
    }
    
    @Override
    public void recordAccess(K key) {
        // Access the entry to update its position in LinkedHashMap
        accessOrder.put(key, Boolean.TRUE);
    }
    
    @Override
    public void recordPut(K key, CacheEntry<K, V> entry) {
        accessOrder.put(key, Boolean.TRUE);
    }
    
    @Override
    public void recordRemoval(K key) {
        accessOrder.remove(key);
    }
    
    @Override
    public K evict() {
        // First entry is the least recently used
        if (accessOrder.isEmpty()) {
            return null;
        }
        K keyToEvict = accessOrder.keySet().iterator().next();
        accessOrder.remove(keyToEvict);
        return keyToEvict;
    }
    
    @Override
    public void clear() {
        accessOrder.clear();
    }
    
    @Override
    public int size() {
        return accessOrder.size();
    }
}

