package com.vladislav.spring.jpa.postgresql.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RequestCounterService {

    private AtomicLong totalRequestCount = new AtomicLong(0);

    public void incrementCounter() {
        totalRequestCount.incrementAndGet();
    }

    public long getTotalRequestCount() {
        return totalRequestCount.get();
    }
}
