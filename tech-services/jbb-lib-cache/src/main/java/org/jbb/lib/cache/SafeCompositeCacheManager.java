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

import org.springframework.cache.Cache;
import org.springframework.cache.jcache.JCacheCache;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.NoOpCache;
import org.springframework.lang.Nullable;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SafeCompositeCacheManager extends CompositeCacheManager {

    @Override
    @Nullable
    public Cache getCache(String name) {
        Cache cache;
        try {
            cache = super.getCache(name);
            if (cache instanceof JCacheCache) {
                return new SafeJCacheCache(((JCacheCache) cache).getNativeCache());
            }
        } catch (Exception e) {
            log.warn("Error getting cache '{}'. NoOpCache fallback have been used.", name, e);
            cache = new NoOpCache(name);
        }
        return cache;
    }

}
