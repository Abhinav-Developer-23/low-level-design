package org.example.RateLimiter.interfaces;

/**
 * Core contract for any rate limiter strategy.
 */
public interface RateLimiter {

    /**
     * Evaluates whether the client can proceed with the request.
     *
     * @param clientId logical identifier for the caller
     * @return true if the request is permitted, false otherwise
     */
    boolean allowRequest(String clientId);
}




