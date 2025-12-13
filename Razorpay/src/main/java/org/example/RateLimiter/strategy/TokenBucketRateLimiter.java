package org.example.RateLimiter.strategy;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.example.RateLimiter.config.RateLimiterConfig;
import org.example.RateLimiter.interfaces.RateLimiter;
import org.example.RateLimiter.interfaces.TimeProvider;

/**
 * Token Bucket rate limiter implementation.
 */
public class TokenBucketRateLimiter implements RateLimiter {

    private final int capacity;
    private final double refillPerMillis;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final TimeProvider timeProvider;

    private static final class Bucket {
        private double tokens;
        private long lastRefillTime;

        Bucket(double tokens, long lastRefillTime) {
            this.tokens = tokens;
            this.lastRefillTime = lastRefillTime;
        }
    }

    public TokenBucketRateLimiter(RateLimiterConfig config, TimeProvider timeProvider) {
        config.validateForTokenBucket();
        this.capacity = config.getBucketCapacity();
        this.refillPerMillis = config.getRefillTokensPerSecond() / 1000.0;
        this.timeProvider = Objects.requireNonNull(timeProvider, "timeProvider");
    }

    @Override
    public boolean allowRequest(String clientId) {
        long now = timeProvider.currentTimeMillis();
        Bucket bucket = buckets.computeIfAbsent(clientId,
                id -> new Bucket(capacity, now));

        synchronized (bucket) {
            refill(bucket, now);
            if (bucket.tokens >= 1.0) {
                bucket.tokens -= 1.0;
                return true;
            }
            return false;
        }
    }

    private void refill(Bucket bucket, long now) {
        long elapsed = now - bucket.lastRefillTime;
        if (elapsed <= 0) {
            return;
        }
        double tokensToAdd = elapsed * refillPerMillis;
        bucket.tokens = Math.min(capacity, bucket.tokens + tokensToAdd);
        bucket.lastRefillTime = now;
    }
}




