package org.lowLevelDesign.MachineCoding.Final.LruCache.cache.factory;


import org.lowLevelDesign.MachineCoding.Final.LruCache.cache.Cache;
import org.lowLevelDesign.MachineCoding.Final.LruCache.cache.policy.LRUEvictionPolicy;
import org.lowLevelDesign.MachineCoding.Final.LruCache.cache.storage.HashMapStorage;

public class CacheFactory<Key, Value> {
    public Cache<Key, Value> defaultCache(final int capacity) {
        return new Cache<>(new HashMapStorage<Key, Value>(capacity), new LRUEvictionPolicy<Key>());
    }
}
