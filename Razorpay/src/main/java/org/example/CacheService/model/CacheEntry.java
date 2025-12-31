package org.example.CacheService.model;

/**
 * Represents a single cache entry with metadata
 * Encapsulates value, access patterns, and expiration information
 * Generic type K for key, V for value
 */
public class CacheEntry<K, V> {
    private final K key;
    private V value;
    private long lastAccessTime;
    private long creationTime;
    private int accessCount;
    private long expirationTime;  // -1 means no expiration
    
    public CacheEntry(K key, V value) {
        this(key, value, -1);
    }
    
    public CacheEntry(K key, V value, long ttlMillis) {
        this.key = key;
        this.value = value;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessTime = this.creationTime;
        this.accessCount = 0;
        this.expirationTime = ttlMillis > 0 ? this.creationTime + ttlMillis : -1;
    }
    
    /**
     * Checks if the entry has expired based on TTL
     */
    public boolean isExpired() {
        if (expirationTime == -1) {
            return false;
        }
        return System.currentTimeMillis() > expirationTime;
    }
    
    /**
     * Updates access metadata when entry is accessed
     */
    public void recordAccess() {
        this.lastAccessTime = System.currentTimeMillis();
        this.accessCount++;
    }
    
    /**
     * Updates the value and resets creation time
     */
    public void updateValue(V newValue) {
        this.value = newValue;
        this.creationTime = System.currentTimeMillis();
        this.lastAccessTime = this.creationTime;
    }
    
    // Getters
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
    
    public long getLastAccessTime() {
        return lastAccessTime;
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    public int getAccessCount() {
        return accessCount;
    }
    
    public long getExpirationTime() {
        return expirationTime;
    }
    
    @Override
    public String toString() {
        return "CacheEntry{" +
                "key=" + key +
                ", value=" + value +
                ", accessCount=" + accessCount +
                ", lastAccessTime=" + lastAccessTime +
                '}';
    }
}




