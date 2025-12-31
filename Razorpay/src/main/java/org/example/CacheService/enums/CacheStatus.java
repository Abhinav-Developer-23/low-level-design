package org.example.CacheService.enums;

/**
 * Enum representing cache operation status
 * Used for tracking cache hits, misses, and operations
 */
public enum CacheStatus {
    HIT,        // Cache hit - key found in cache
    MISS,       // Cache miss - key not found in cache
    ADDED,      // New entry added to cache
    UPDATED,    // Existing entry updated
    EVICTED,    // Entry evicted from cache
    REMOVED,    // Entry explicitly removed
    EXPIRED     // Entry expired based on TTL
}




