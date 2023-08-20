package com.ratelimiter;

import com.ratelimiter.algorithm.SlidingWindowLogRateLimiter;
import com.ratelimiter.algorithm.TokenBucketRateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {

        // Note: Comment out all the other test scenarios when running one rate limiter

        // Sliding Window Log Rate Limiter test run
        RateLimiter slidingWindowRL = new SlidingWindowLogRateLimiter(10,1);
        Application api1 = new Application(slidingWindowRL);
        try (ExecutorService executorService = Executors.newFixedThreadPool(12)) {
            for (int i=0; i<12; i++) {
                executorService.execute(() -> api1.invoke());
            }
        }

        // Token Bucket Rate Limiter test run
        RateLimiter tokenBucketRL = new TokenBucketRateLimiter(10,2);
        Application api2 = new Application(tokenBucketRL);
        try (ExecutorService executorService = Executors.newFixedThreadPool(50)) {
            for (int i=0; i<50; i++) {
                try {
                    Thread.sleep(80);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
                executorService.execute(() -> api2.invoke());
            }
        }
    }
}