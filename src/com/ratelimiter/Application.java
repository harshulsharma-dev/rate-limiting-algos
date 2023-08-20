package com.ratelimiter;

public class Application {

    private final RateLimiter rateLimiter;

    public Application(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    public void invoke() {
        if (rateLimiter.acquire()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

            System.out.println(Thread.currentThread().getName() + " -> Request is Successful!");
        } else {
            System.out.println(Thread.currentThread().getName() + " -> Request Throttled!");
        }
    }
}
