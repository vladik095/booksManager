package com.vladislav.spring.jpa.postgresql.cache;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BookCache {
    private static final int MAX_CACHE_SIZE = 2;
    private final Map<String, List<Long>> bookCache = new ConcurrentHashMap<>();
    private final Map<String, Long> accessOrder = new HashMap<>();

    public synchronized void addToCache(String keyword, List<Long> bookIds) {
        if (bookCache.size() >= MAX_CACHE_SIZE) {
            String oldestKeyword = accessOrder.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            bookCache.remove(oldestKeyword);
            accessOrder.remove(oldestKeyword);
        }
        bookCache.put(keyword, bookIds);
        accessOrder.put(keyword, System.currentTimeMillis());
    }

    public List<Long> getBooksFromCache(String keyword) {
        return bookCache.getOrDefault(keyword, List.of());
    }

    public void clearCache() {
        bookCache.clear();
        accessOrder.clear();
    }

    public void printCacheContents() {
        System.out.println("Cache contents:");
        for (Map.Entry<String, List<Long>> entry : bookCache.entrySet()) {
            String keyword = entry.getKey();
            List<Long> bookIds = entry.getValue();

            System.out.println("Keyword: " + keyword + ", Book IDs: " + bookIds);
        }
    }
}
