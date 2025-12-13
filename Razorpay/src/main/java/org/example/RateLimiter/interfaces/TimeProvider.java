package org.example.RateLimiter.interfaces;

/**
 * Abstraction over time to ease testing and decouple from system clock.
 */
public interface TimeProvider {
    long currentTimeMillis();
}




