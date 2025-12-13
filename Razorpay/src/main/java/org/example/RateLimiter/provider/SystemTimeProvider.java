package org.example.RateLimiter.provider;

import org.example.RateLimiter.interfaces.TimeProvider;

/**
 * Default time provider backed by {@link System#currentTimeMillis()}.
 */
public class SystemTimeProvider implements TimeProvider {
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}




