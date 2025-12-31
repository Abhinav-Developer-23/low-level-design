package org.example.RateLimiter.factory;

import java.util.Objects;
import org.example.RateLimiter.config.RateLimiterConfig;
import org.example.RateLimiter.enums.RateLimiterType;
import org.example.RateLimiter.interfaces.RateLimiter;
import org.example.RateLimiter.interfaces.TimeProvider;
import org.example.RateLimiter.provider.SystemTimeProvider;
import org.example.RateLimiter.strategy.FixedWindowRateLimiter;
import org.example.RateLimiter.strategy.TokenBucketRateLimiter;

/**
 * Factory encapsulating creation of different rate limiter strategies.
 */
public class RateLimiterFactory {

    private final TimeProvider timeProvider;

    public RateLimiterFactory() {
        this(new SystemTimeProvider());
    }

    public RateLimiterFactory(TimeProvider timeProvider) {
        this.timeProvider = Objects.requireNonNull(timeProvider, "timeProvider");
    }

    public RateLimiter create(RateLimiterType type, RateLimiterConfig config) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(config, "config");

        return switch (type) {
            case TOKEN_BUCKET -> new TokenBucketRateLimiter(config, timeProvider);
            case FIXED_WINDOW -> new FixedWindowRateLimiter(config, timeProvider);
        };
    }
}







