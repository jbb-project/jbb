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

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.stereotype.Component;

@Component
public class SpringCacheManagerFactory {
    private final ProxyJCacheManager proxyJCacheManager;
    private final CacheProperties cacheProperties;

    @Autowired
    public SpringCacheManagerFactory(ProxyJCacheManager proxyJCacheManager, CacheProperties cacheProperties) {
        this.proxyJCacheManager = proxyJCacheManager;
        this.cacheProperties = cacheProperties;
    }

    public CacheManager build() {
        if (cacheProperties.applicationCacheEnabled()) {
            return buildJCacheManager();
        } else {
            return new NoOpCacheManager();
        }
    }

    private CacheManager buildJCacheManager() {
        CaffeineCachingProvider caffeineCachingProvider = new CaffeineCachingProvider();
        javax.cache.CacheManager caffeineCacheManager = caffeineCachingProvider.getCacheManager();
        proxyJCacheManager.setCacheManagerBeingProxied(caffeineCacheManager);
        return new JCacheCacheManager(proxyJCacheManager);
    }
}
