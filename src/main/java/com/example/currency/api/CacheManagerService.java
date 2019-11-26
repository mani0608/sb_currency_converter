package com.example.currency.api;

public interface CacheManagerService {
    public Object getCacheForKey(String cacheName, String key);
    public boolean evictCacheByKey(String cacheName, String key);
    public boolean addToCache(String cacheName, String key, Object value);
    public boolean hasKey(String cacheName, String key);
}
