package org.example.CacheService.policies;

import org.example.CacheService.interfaces.IEvictionPolicy;
import org.example.CacheService.model.CacheEntry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Least Frequently Used (LFU) eviction policy implementation
 * Evicts the least frequently accessed entry when cache is full
 * 
 * Data Structure: HashMap for frequency count + TreeMap for frequency-based ordering
 * Time Complexity: O(log n) for eviction, O(log n) for access
 * Space Complexity: O(n) where n is number of entries
 * 
 * Pros:
 * - Considers access frequency - keeps hot data
 * - Good for workloads with clear access patterns
 * - Protects frequently accessed items
 * 
 * Cons:
 * - More complex implementation
 * - Early frequent access can cause stale entries to persist
 * - Slightly higher overhead than LRU
 */
public class LFUEvictionPolicy<K, V> implements IEvictionPolicy<K, V> {
    private final Map<K, Integer> frequencyMap;
    private final TreeMap<Integer, LinkedHashSet<K>> frequencyBuckets;
    private int minFrequency;
    
    public LFUEvictionPolicy() {
        this.frequencyMap = new ConcurrentHashMap<>();
        this.frequencyBuckets = new TreeMap<>();
        this.minFrequency = 0;
    }
    
    @Override
    public void recordAccess(K key) {
        if (!frequencyMap.containsKey(key)) {
            return;
        }
        
        int currentFreq = frequencyMap.get(key);
        int newFreq = currentFreq + 1;
        
        // Remove from current frequency bucket
        frequencyBuckets.get(currentFreq).remove(key);
        if (frequencyBuckets.get(currentFreq).isEmpty()) {
            frequencyBuckets.remove(currentFreq);
            if (minFrequency == currentFreq) {
                minFrequency = newFreq;
            }
        }
        
        // Add to new frequency bucket
        frequencyMap.put(key, newFreq);
        frequencyBuckets.computeIfAbsent(newFreq, k -> new LinkedHashSet<>()).add(key);
    }
    
    @Override
    public void recordPut(K key, CacheEntry<K, V> entry) {
        frequencyMap.put(key, 1);
        frequencyBuckets.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFrequency = 1;
    }
    
    @Override
    public void recordRemoval(K key) {
        if (!frequencyMap.containsKey(key)) {
            return;
        }
        
        int freq = frequencyMap.remove(key);
        frequencyBuckets.get(freq).remove(key);
        if (frequencyBuckets.get(freq).isEmpty()) {
            frequencyBuckets.remove(freq);
        }
    }
    
    @Override
    public K evict() {
        if (frequencyMap.isEmpty()) {
            return null;
        }
        
        // Get the first key from minimum frequency bucket
        LinkedHashSet<K> minFreqKeys = frequencyBuckets.get(minFrequency);
        if (minFreqKeys == null || minFreqKeys.isEmpty()) {
            // Fallback: get first available key
            minFrequency = frequencyBuckets.firstKey();
            minFreqKeys = frequencyBuckets.get(minFrequency);
        }
        
        K keyToEvict = minFreqKeys.iterator().next();
        recordRemoval(keyToEvict);
        return keyToEvict;
    }
    
    @Override
    public void clear() {
        frequencyMap.clear();
        frequencyBuckets.clear();
        minFrequency = 0;
    }
    
    @Override
    public int size() {
        return frequencyMap.size();
    }
}




