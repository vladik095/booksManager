package com.vladislav.spring.jpa.postgresql.cache;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BookCache {
    private static final int MAX_CACHE_SIZE = 0;
    private final Map<String, List<Long>> cache = new ConcurrentHashMap<>();
    private final Map<String, Long> accessOrder = new LinkedHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(BookCache.class);

    public synchronized void addToCache(String keyword, List<Long> bookIds) {
        if (cache.size() >= MAX_CACHE_SIZE) {
            String oldestKeyword = accessOrder.entrySet().stream()
                    .min(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(null);
            cache.remove(oldestKeyword);
            accessOrder.remove(oldestKeyword);
        }
        cache.put(keyword, bookIds);
        accessOrder.put(keyword, System.currentTimeMillis());
    }

    public List<Long> getBooksFromCache(String keyword) {
        return cache.getOrDefault(keyword, List.of());
    }

    public void clearCache() {
        cache.clear();
        accessOrder.clear();
    }

    public void printCacheContents() {
        logger.info("Cache contents:");
        for (Map.Entry<String, List<Long>> entry : cache.entrySet()) {
            String keyword = entry.getKey();
            List<Long> bookIds = entry.getValue();

            logger.info("Keyword: {}, Book IDs: {}", keyword, bookIds);
        }
    }
}
