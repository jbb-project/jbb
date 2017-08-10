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

import static org.jbb.lib.cache.JbbCacheManager.CACHE_PROVIDER_AVAILABLE_NAMES;
import static org.jbb.lib.cache.JbbCacheManager.CAFFEINE_PROVIDER_NAME;
import static org.jbb.lib.cache.JbbCacheManager.HAZELCAST_CLIENT_PROVIDER_NAME;
import static org.jbb.lib.cache.JbbCacheManager.HAZELCAST_SERVER_PROVIDER_NAME;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import com.hazelcast.cache.impl.HazelcastServerCachingProvider;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.cache.impl.HazelcastClientCachingProvider;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;
import javax.cache.CacheManager;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.cache.hazelcast.HazelcastConfigFilesManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class JCacheManagerFactory {
    private final CacheProperties cacheProperties;
    private final HazelcastConfigFilesManager hazelcastConfigFilesManager;
    private final ManagedHazelcastInstance managedHazelcastInstance;

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
            HazelcastInstance hazelcastInstance = HazelcastInstanceFactory.getOrCreateHazelcastInstance(config);
            managedHazelcastInstance.setTarget(hazelcastInstance);
            cacheManager = HazelcastServerCachingProvider.createCachingProvider(hazelcastInstance).getCacheManager(); //NOSONAR
        } else if (HAZELCAST_CLIENT_PROVIDER_NAME.equalsIgnoreCase(cacheProviderName)) {
            ClientConfig clientConfig = hazelcastConfigFilesManager.getHazelcastClientConfig();
            clientConfig.setInstanceName("jbb-hz-client");
            HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
            managedHazelcastInstance.setTarget(hazelcastInstance);
            cacheManager = HazelcastClientCachingProvider.createCachingProvider(hazelcastInstance)
                .getCacheManager(); //NOSONAR
        }

        return cacheManager;
    }

    private boolean providerNameIsIncorrect(String cacheProviderName) {
        return !CACHE_PROVIDER_AVAILABLE_NAMES.contains(cacheProviderName.toLowerCase());
    }
}
