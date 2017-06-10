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
import org.springframework.stereotype.Component;

import javax.cache.CacheManager;

@Component
class JCacheManagerFactory {
    private final CacheProperties cacheProperties;

    @Autowired
    public JCacheManagerFactory(CacheProperties cacheProperties) {
        this.cacheProperties = cacheProperties;
    }

    public CacheManager build() {
        String cacheProviderName = cacheProperties.providerName();

        if (providerNameIsIncorrect(cacheProviderName)) {
            throw new IllegalArgumentException("Cache provider name is invalid: '" + cacheProviderName +
                    "'. Available names are: " + JbbCacheManager.CACHE_PROVIDER_AVAILABLE_NAMES);
        }

        CacheManager cacheManager = null;
        if (JbbCacheManager.CAFFEINE_PROVIDER_NAME.equalsIgnoreCase(cacheProviderName)) {
            CaffeineCachingProvider caffeineCachingProvider = new CaffeineCachingProvider(); //NOSONAR
            cacheManager = caffeineCachingProvider.getCacheManager();
        }

        return cacheManager;
    }

    private boolean providerNameIsIncorrect(String cacheProviderName) {
        return !JbbCacheManager.CACHE_PROVIDER_AVAILABLE_NAMES.contains(cacheProviderName.toLowerCase());
    }
}
