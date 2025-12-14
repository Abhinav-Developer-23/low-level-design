package org.example.RateLimiter;

import java.time.Duration;
import org.example.RateLimiter.config.RateLimiterConfig;
import org.example.RateLimiter.enums.RateLimiterType;
import org.example.RateLimiter.factory.RateLimiterFactory;
import org.example.RateLimiter.interfaces.RateLimiter;

/**
 * Demo runner showcasing both Token Bucket and Fixed Window rate limiters.
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        RateLimiterFactory factory = new RateLimiterFactory();

        RateLimiter tokenBucketLimiter = factory.create(
                RateLimiterType.TOKEN_BUCKET,
                RateLimiterConfig.builder()
                        .bucketCapacity(5)
                        .refillTokensPerSecond(2.0) // 2 tokens per second
                        .build()
        );

        RateLimiter fixedWindowLimiter = factory.create(
                RateLimiterType.FIXED_WINDOW,
                RateLimiterConfig.builder()
                        .windowRequestLimit(3)
                        .windowSize(Duration.ofSeconds(5))
                        .build()
        );

        String clientId = "client-1";

        System.out.println("=== Token Bucket Demo ===");
        for (int i = 1; i <= 8; i++) {
            boolean allowed = tokenBucketLimiter.allowRequest(clientId);
            System.out.printf("Request %d allowed: %s%n", i, allowed);
            Thread.sleep(100);
        }

        Thread.sleep(1500); // allow some refill
        System.out.printf("Post-refill request allowed: %s%n",
                tokenBucketLimiter.allowRequest(clientId));

        System.out.println("\n=== Fixed Window Demo ===");
        for (int i = 1; i <= 6; i++) {
            boolean allowed = fixedWindowLimiter.allowRequest(clientId);
            System.out.printf("Request %d allowed: %s%n", i, allowed);
            Thread.sleep(1000);
        }
    }
}
