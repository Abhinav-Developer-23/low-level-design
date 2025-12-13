package org.example.CacheService.enums;

/**
 * Enum representing different cache eviction policies
 * Defines the strategy for removing entries when cache is full
 */
public enum EvictionPolicy {
    LRU("Least Recently Used"),
    LFU("Least Frequently Used"),
    FIFO("First In First Out"),
    LIFO("Last In First Out");
    
    private final String description;
    
    EvictionPolicy(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}


