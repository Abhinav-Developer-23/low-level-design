package org.example.RateLimiter.strategy;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.example.RateLimiter.Model.Bucket;
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



    public TokenBucketRateLimiter(RateLimiterConfig config, TimeProvider timeProvider) {
        config.validateForTokenBucket();
        this.capacity = config.getBucketCapacity();
        this.refillPerMillis = config.getRefillTokensPerSecond() / 1000.0;
        this.timeProvider = Objects.requireNonNull(timeProvider, "timeProvider");
    }

    @Override
    public boolean allowRequest(String clientId) {
        long now = timeProvider.currentTimeMillis();


        Bucket bucket = buckets.computeIfAbsent(clientId, k -> new Bucket(capacity, now));


        synchronized (bucket) {
            refill(bucket, now);
            if (bucket.getTokens() >= 1.0) {
                bucket.setTokens(bucket.getTokens()-1.0);
                return true;
            }
            return false;
        }
    }

    private void refill(Bucket bucket, long now) {
        long elapsed = now - bucket.getLastRefillTime();
        if (elapsed <= 0) {
            return;
        }
        double tokensToAdd = elapsed * refillPerMillis;
        bucket.setTokens( Math.min(capacity, bucket.getTokens() + tokensToAdd));
        bucket.setLastRefillTime(now);
    }
}




