package com.ratelimiter.algorithm;

import com.ratelimiter.RateLimiter;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SlidingWindowLogRateLimiter implements RateLimiter {

    int requestLimit;
    int timeWindowInSec;
    Queue<Long> requestTimeStamps;

    public SlidingWindowLogRateLimiter(int requestLimit, int timeWindowInSec) {
        this.requestLimit = requestLimit;
        this.timeWindowInSec = timeWindowInSec;
        this.requestTimeStamps = new ConcurrentLinkedQueue<>(   );
    }

    @Override
    public boolean acquire() {
        long currentTime = System.currentTimeMillis();
        removeOutdatedTimestamps(currentTime);
        if (requestTimeStamps.size() < requestLimit) {
            requestTimeStamps.offer(currentTime);
            return true;
        }
        return false;
    }

    private void removeOutdatedTimestamps(long curTime) {
        while (!requestTimeStamps.isEmpty() && (curTime - requestTimeStamps.peek())/1000 > timeWindowInSec) {
            requestTimeStamps.poll();
        }
    }
}
