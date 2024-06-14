package org.lowLevelDesign.MachineCoding.Scratch.lruCache.Service;



import org.lowLevelDesign.MachineCoding.Scratch.lruCache.Entities.CacheEntry;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;

public class CacheOperationsService {

    private final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    private final ExpiredCacheEvictorService expiredCacheEvictorService = new ExpiredCacheEvictorService();


    public CacheOperationsService() {
        startExpiredCacheEvictor();
    }

    Integer maxCacheSize = 10;

    public void addKeyValue(String key, String value, Long expireAt) {

        if (cache.size() == maxCacheSize) {
            removeLastAccessedAt();
        }

        CacheEntry cacheEntry = new CacheEntry();
        cacheEntry.setCachedValue(value);
        cacheEntry.setLastAccessedAt(LocalDateTime.now());
        cacheEntry.setExpireAt(LocalDateTime.now().plusSeconds(expireAt));
        cache.put(key, cacheEntry);

        System.out.println("Data inside cache is " + cache);

    }

    public void removeKeyValue(String key) {

        if (Objects.isNull(cache.get(key))) {
            System.out.println("Error while deleting No key found in cache with key :: " + key);
            return;
        }
        cache.remove(key);
        System.out.println("Data inside cache is " + cache);
    }

    public String getKeyValue(String key) {
        if (Objects.isNull(cache.get(key))) {
            System.out.println("Error while getting No key found in cache with key :: " + key);
            return "Not found";
        }
        CacheEntry cacheEntry = cache.get(key);
        cacheEntry.setLastAccessedAt(LocalDateTime.now());
        String value = cacheEntry.getCachedValue();
        System.out.println("value returned is " + value);
        System.out.println("Data inside cache is " + cache);
        return cacheEntry.getCachedValue();
    }

    private void removeLastAccessedAt() {

        LocalDateTime leastTime = LocalDateTime.MAX;
        String keyOfLeastTime = "";

        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            CacheEntry data = entry.getValue();
            if (data.getLastAccessedAt().isBefore(leastTime)) {
                leastTime = data.getLastAccessedAt();
                keyOfLeastTime = entry.getKey();
            }
        }
        cache.remove(keyOfLeastTime);
    }

    private void startExpiredCacheEvictor() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable evictTask = () -> expiredCacheEvictorService.evictExpiredCache(cache);

        scheduler.scheduleAtFixedRate(evictTask, 0, 5, TimeUnit.SECONDS);
    }

}
