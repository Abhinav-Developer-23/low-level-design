package org.lowLevelDesign.MachineCoding.Final.LruCache.cache.storage;


import org.lowLevelDesign.MachineCoding.Final.LruCache.cache.exception.KeyNotFoundException;
import org.lowLevelDesign.MachineCoding.Final.LruCache.cache.exception.StorageFullException;

public interface Storage<Key,Value> {
    void add(Key key, Value value) throws StorageFullException;
    void remove(Key key) ;
    Value get(Key key) throws KeyNotFoundException;
}
