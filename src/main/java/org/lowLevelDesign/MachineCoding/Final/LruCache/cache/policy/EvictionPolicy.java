package org.lowLevelDesign.MachineCoding.Final.LruCache.cache.policy;

public interface EvictionPolicy<Key> {
    void keyAccessed(Key key);
    Key evict();
}
