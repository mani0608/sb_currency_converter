package com.example.currency.config;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheLogger implements CacheEventListener<Object, Object> {

    private static Logger logger = LoggerFactory.getLogger(CacheLogger.class);

    @Override
    public void onEvent(CacheEvent<?, ?> event) {
        logger.info("Cache Log: {\nkey: " + event.getKey() + ",\ntype: " + event.getType()
                + ",\noldValue: " + event.getOldValue() + ",\nnewValue: " + event.getNewValue());
    }
}
