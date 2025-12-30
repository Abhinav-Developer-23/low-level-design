package org.example.RateLimiter.strategy;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.example.RateLimiter.config.RateLimiterConfig;
import org.example.RateLimiter.interfaces.RateLimiter;
import org.example.RateLimiter.interfaces.TimeProvider;

/**
 * Fixed window rate limiter implementation.
 */
public class FixedWindowRateLimiter implements RateLimiter {

    private final int maxRequests;
    private final long windowSizeMillis;
    private final Map<String, Window> windows = new ConcurrentHashMap<>();
    private final TimeProvider timeProvider;

    private static final class Window   {
        private long windowStart;
        private int count;

        Window(long windowStart) {
            this.windowStart = windowStart;
            this.count = 0;
        }
    }

    public FixedWindowRateLimiter(RateLimiterConfig config, TimeProvider timeProvider) {
        config.validateForFixedWindow();
        this.maxRequests = config.getWindowRequestLimit();
        this.windowSizeMillis = config.getWindowSize().toMillis();
        this.timeProvider = Objects.requireNonNull(timeProvider, "timeProvider");
    }

    @Override
    public boolean allowRequest(String clientId) {
        long now = timeProvider.currentTimeMillis();
        long currentWindowStart = (now / windowSizeMillis) * windowSizeMillis;

        Window window = windows.computeIfAbsent(clientId, id -> new Window(currentWindowStart));
        synchronized (window) {
            if (window.windowStart != currentWindowStart) {
                window.windowStart = currentWindowStart;
                window.count = 0;
            }

            if (window.count < maxRequests) {
                window.count++;
                return true;
            }
            return false;
        }
    }
}





