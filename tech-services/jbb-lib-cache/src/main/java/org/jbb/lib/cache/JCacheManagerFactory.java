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
import com.hazelcast.cache.impl.HazelcastServerCachingProvider;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.cache.CacheManager;

import static org.jbb.lib.cache.JbbCacheManager.CACHE_PROVIDER_AVAILABLE_NAMES;
import static org.jbb.lib.cache.JbbCacheManager.CAFFEINE_PROVIDER_NAME;
import static org.jbb.lib.cache.JbbCacheManager.HAZELCAST_SERVER_PROVIDER_NAME;

@Component
class JCacheManagerFactory {
    private final CacheProperties cacheProperties;
    private final HazelcastConfigFilesManager hazelcastConfigFilesManager;
    private final ManagedHazelcastInstance managedHazelcastInstance;

    @Autowired
    public JCacheManagerFactory(CacheProperties cacheProperties,
                                HazelcastConfigFilesManager hazelcastConfigFilesManager,
                                ManagedHazelcastInstance managedHazelcastInstance) {
        this.cacheProperties = cacheProperties;
        this.hazelcastConfigFilesManager = hazelcastConfigFilesManager;
        this.managedHazelcastInstance = managedHazelcastInstance;
    }

    public CacheManager build() {
        String cacheProviderName = cacheProperties.providerName();

        if (providerNameIsIncorrect(cacheProviderName)) {
            throw new IllegalArgumentException("Cache provider name is invalid: '" + cacheProviderName +
                    "'. Available names are: " + CACHE_PROVIDER_AVAILABLE_NAMES);
        }

        CacheManager cacheManager = null;
        if (CAFFEINE_PROVIDER_NAME.equalsIgnoreCase(cacheProviderName)) {
            CaffeineCachingProvider caffeineCachingProvider = new CaffeineCachingProvider(); //NOSONAR
            cacheManager = caffeineCachingProvider.getCacheManager();
        } else if (HAZELCAST_SERVER_PROVIDER_NAME.equalsIgnoreCase(cacheProviderName)) {
            Config config = hazelcastConfigFilesManager.getHazelcastServerConfig().setInstanceName("jbb-hz");
            config.setProperty("hazelcast.logging.type", "slf4j");
            HazelcastInstance hazelcastInstance = HazelcastInstanceFactory.getOrCreateHazelcastInstance(config);
            managedHazelcastInstance.setTarget(hazelcastInstance);
            cacheManager = HazelcastServerCachingProvider.createCachingProvider(hazelcastInstance).getCacheManager();
        }

        return cacheManager;
    }

    private boolean providerNameIsIncorrect(String cacheProviderName) {
        return !CACHE_PROVIDER_AVAILABLE_NAMES.contains(cacheProviderName.toLowerCase());
    }
}
