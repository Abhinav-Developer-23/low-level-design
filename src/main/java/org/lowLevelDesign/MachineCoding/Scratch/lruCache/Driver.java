

package org.lowLevelDesign.MachineCoding.Scratch.lruCache;

import org.lowLevelDesign.MachineCoding.Scratch.lruCache.Service.CacheOperationsService;

public class Driver {

    public static void main(String[] args) {
        CacheOperationsService cacheService = new CacheOperationsService();
        // Adding data to the cache
        cacheService.addKeyValue("key1", "value1", 100000L);
        cacheService.addKeyValue("key2", "value2", 20L);
        cacheService.addKeyValue("key3", "value3", 20L);

        // Getting data from the cache
        System.out.println("Get key1: " + cacheService.getKeyValue("key1"));

        // Wait for some time to see eviction in action
        try {
            Thread.sleep(15000);  // Sleep for 15 seconds to allow some entries to expire
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Getting data from the cache after some time
        System.out.println("Get key1 after some time: " + cacheService.getKeyValue("key1"));
        System.out.println("Get key2 after some time: " + cacheService.getKeyValue("key2"));

        // Removing data from the cache
        cacheService.removeKeyValue("key1");

        // Trying to get removed data
        System.out.println("Get removed key1: " + cacheService.getKeyValue("key1"));
    }

}

