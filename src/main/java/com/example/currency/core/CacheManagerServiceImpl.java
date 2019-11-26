package com.example.currency.core;

import com.example.currency.api.CacheManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheManagerServiceImpl implements CacheManagerService {

    @Autowired
    CacheManager cacheManager;

    @Override
    public Object getCacheForKey(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        Cache.ValueWrapper wrapper = cache.get(key);
        return (wrapper != null) ? wrapper.get() : null;
    }

    @Override
    public boolean evictCacheByKey(String cacheName, String key) {
        Cache cache = cacheManager.getCache(cacheName);
        return cache.evictIfPresent(key);
    }

    @Override
    public boolean addToCache(String cacheName, String key, Object value) {
        Cache cache = cacheManager.getCache(cacheName);
        cache.put(key, value);
        return true;
    }

    @Override
    public boolean hasKey(String cacheName, String key) {
        return (getCacheForKey(cacheName, key) != null);
    }

}
