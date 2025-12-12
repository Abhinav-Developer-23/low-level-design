package org.example.RateLimiter.config;

import java.time.Duration;
import java.util.Objects;

/**
 * Immutable configuration shared by rate limiter strategies.
 */
public final class RateLimiterConfig {

    private final int bucketCapacity;
    private final double refillTokensPerSecond;
    private final int windowRequestLimit;
    private final Duration windowSize;

    private RateLimiterConfig(Builder builder) {
        this.bucketCapacity = builder.bucketCapacity;
        this.refillTokensPerSecond = builder.refillTokensPerSecond;
        this.windowRequestLimit = builder.windowRequestLimit;
        this.windowSize = builder.windowSize;
    }

    public int getBucketCapacity() {
        return bucketCapacity;
    }

    public double getRefillTokensPerSecond() {
        return refillTokensPerSecond;
    }

    public int getWindowRequestLimit() {
        return windowRequestLimit;
    }

    public Duration getWindowSize() {
        return windowSize;
    }

    /**
     * Validation dedicated to token bucket strategy.
     */
    public void validateForTokenBucket() {
        if (bucketCapacity <= 0) {
            throw new IllegalArgumentException("Bucket capacity must be positive");
        }
        if (refillTokensPerSecond <= 0) {
            throw new IllegalArgumentException("Refill rate must be positive");
        }
    }

    /**
     * Validation dedicated to fixed window strategy.
     */
    public void validateForFixedWindow() {
        if (windowRequestLimit <= 0) {
            throw new IllegalArgumentException("Window request limit must be positive");
        }
        if (windowSize == null || windowSize.isZero() || windowSize.isNegative()) {
            throw new IllegalArgumentException("Window size must be positive");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link RateLimiterConfig}.
     */
    public static final class Builder {
        private int bucketCapacity;
        private double refillTokensPerSecond;
        private int windowRequestLimit;
        private Duration windowSize;

        private Builder() {
        }

        public Builder bucketCapacity(int bucketCapacity) {
            this.bucketCapacity = bucketCapacity;
            return this;
        }

        public Builder refillTokensPerSecond(double refillTokensPerSecond) {
            this.refillTokensPerSecond = refillTokensPerSecond;
            return this;
        }

        public Builder windowRequestLimit(int windowRequestLimit) {
            this.windowRequestLimit = windowRequestLimit;
            return this;
        }

        public Builder windowSize(Duration windowSize) {
            this.windowSize = Objects.requireNonNull(windowSize, "windowSize");
            return this;
        }

        public RateLimiterConfig build() {
            return new RateLimiterConfig(this);
        }
    }
}



