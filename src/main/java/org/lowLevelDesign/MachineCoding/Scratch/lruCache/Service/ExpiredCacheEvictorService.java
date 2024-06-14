package org.lowLevelDesign.MachineCoding.Scratch.lruCache.Service;



import org.lowLevelDesign.MachineCoding.Scratch.lruCache.Entities.CacheEntry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ExpiredCacheEvictorService {


    public void evictExpiredCache(ConcurrentHashMap<String, CacheEntry> cache) {

        System.out.println("Running eviction cache eviction");
        List<String> keysToRemove = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Map.Entry<String, CacheEntry> entry : cache.entrySet()) {
            CacheEntry data = entry.getValue();
            if (data.getExpireAt().isBefore(now)) {
                keysToRemove.add(entry.getKey());
            }
        }

        System.out.println("Auto Removed cache entries: " + keysToRemove);
        for (String key : keysToRemove) {
            cache.remove(key);
        }
    }


}
