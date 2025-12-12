package org.example.CacheService.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks cache statistics and metrics
 * Thread-safe implementation using atomic variables
 * Provides insights into cache performance
 */
public class CacheStats {
    private final AtomicLong hits;
    private final AtomicLong misses;
    private final AtomicLong evictions;
    private final AtomicLong puts;
    private final AtomicLong removes;
    private final AtomicLong expirations;
    
    public CacheStats() {
        this.hits = new AtomicLong(0);
        this.misses = new AtomicLong(0);
        this.evictions = new AtomicLong(0);
        this.puts = new AtomicLong(0);
        this.removes = new AtomicLong(0);
        this.expirations = new AtomicLong(0);
    }
    
    public void recordHit() {
        hits.incrementAndGet();
    }
    
    public void recordMiss() {
        misses.incrementAndGet();
    }
    
    public void recordEviction() {
        evictions.incrementAndGet();
    }
    
    public void recordPut() {
        puts.incrementAndGet();
    }
    
    public void recordRemove() {
        removes.incrementAndGet();
    }
    
    public void recordExpiration() {
        expirations.incrementAndGet();
    }
    
    public long getHits() {
        return hits.get();
    }
    
    public long getMisses() {
        return misses.get();
    }
    
    public long getEvictions() {
        return evictions.get();
    }
    
    public long getPuts() {
        return puts.get();
    }
    
    public long getRemoves() {
        return removes.get();
    }
    
    public long getExpirations() {
        return expirations.get();
    }
    
    public long getTotalRequests() {
        return hits.get() + misses.get();
    }
    
    /**
     * Calculates the cache hit ratio
     */
    public double getHitRatio() {
        long total = getTotalRequests();
        return total == 0 ? 0.0 : (double) hits.get() / total;
    }
    
    /**
     * Resets all statistics
     */
    public void reset() {
        hits.set(0);
        misses.set(0);
        evictions.set(0);
        puts.set(0);
        removes.set(0);
        expirations.set(0);
    }
    
    @Override
    public String toString() {
        return String.format(
            "CacheStats{hits=%d, misses=%d, hitRatio=%.2f%%, evictions=%d, puts=%d, removes=%d, expirations=%d}",
            getHits(), getMisses(), getHitRatio() * 100, getEvictions(), getPuts(), getRemoves(), getExpirations()
        );
    }
}

