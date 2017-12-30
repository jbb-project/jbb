/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache;

import org.springframework.cache.jcache.JCacheCache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.lang.Nullable;

import javax.cache.Cache;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SafeJCacheCache extends JCacheCache {

    public SafeJCacheCache(Cache<Object, Object> jcache) {
        super(jcache);
    }

    @Override
    protected Object lookup(Object key) {
        try {
            return super.lookup(key);
        } catch (Exception e) {
            log.warn("Error lookup in cache '{}' for key '{}'", getName(), key, e);
            return null;
        }
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        try {
            super.put(key, value);
        } catch (Exception e) {
            log.warn("Error put in cache '{}' for key '{}' and value '{}'", getName(), key, value, e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, @Nullable Object value) {
        try {
            return super.putIfAbsent(key, value);
        } catch (Exception e) {
            log.warn("Error putIfAbsent in cache '{}' for key '{}' and value '{}'", getName(), key, value, e);
            return new SimpleValueWrapper(null);
        }
    }

    @Override
    public void evict(Object key) {
        try {
            super.evict(key);
        } catch (Exception e) {
            log.warn("Error evict in cache '{}' for key '{}'", getName(), key, e);
        }
    }

    @Override
    public void clear() {
        try {
            super.clear();
        } catch (Exception e) {
            log.warn("Error clear cache '{}' for key '{}'", getName(), e);
        }
    }
}
