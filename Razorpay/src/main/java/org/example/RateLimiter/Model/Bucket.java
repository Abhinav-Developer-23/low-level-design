package org.example.RateLimiter.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bucket {
    private double tokens;
    private long lastRefillTime;

    public Bucket(double tokens, long lastRefillTime) {
        this.tokens = tokens;
        this.lastRefillTime = lastRefillTime;
    }
}