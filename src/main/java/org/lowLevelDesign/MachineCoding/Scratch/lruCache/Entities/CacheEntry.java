package org.lowLevelDesign.MachineCoding.Scratch.lruCache.Entities;


import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CacheEntry {

    private String cachedValue;

    public LocalDateTime expireAt;

    public LocalDateTime lastAccessedAt;

}
