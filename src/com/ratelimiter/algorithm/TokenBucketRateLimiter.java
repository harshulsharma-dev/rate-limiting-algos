package com.ratelimiter.algorithm;

import com.ratelimiter.RateLimiter;

public class TokenBucketRateLimiter implements RateLimiter {

    private final int capacity; // Maximum number of tokens the bucket can hold
    private final int refillRate; // Tokens to be added per second
    private int tokens; // Current number of tokens in the bucket
    private long lastRefillTime; // Last time the bucket was refilled

    private final Object lock = new Object();

    public TokenBucketRateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTime = System.currentTimeMillis();
    }

    @Override
    public boolean acquire() {
        synchronized (lock) {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }
    }

    private void refill() {
        long now = System.currentTimeMillis();
        long timeElapsed = (now-lastRefillTime)/1000;
        int tokensToAdd = (int) (timeElapsed*refillRate);

        if (tokensToAdd > 0) {
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTime = now;
        }
    }
}
