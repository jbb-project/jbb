/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache;

import lombok.RequiredArgsConstructor;
import org.jbb.lib.cache.CacheProperties;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.impl.cache.provider.CacheProvidersService;
import org.jbb.system.impl.cache.provider.HazelcastClientProviderManager;
import org.jbb.system.impl.cache.provider.HazelcastServerProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CacheSettingsFactory {

    private final CacheProperties cacheProperties;
    private final CacheProvidersService cacheProvidersService;
    private final HazelcastServerProviderManager hazelcastServerProviderManager;
    private final HazelcastClientProviderManager hazelcastClientProviderManager;


    public CacheSettings currentCacheSettings() {
        return CacheSettings.builder()
            .applicationCacheEnabled(cacheProperties.applicationCacheEnabled())
            .secondLevelCacheEnabled(cacheProperties.secondLevelCacheEnabled())
            .queryCacheEnabled(cacheProperties.queryCacheEnabled())
            .hazelcastServerSettings(hazelcastServerProviderManager.getCurrentProviderSettings())
            .hazelcastClientSettings(hazelcastClientProviderManager.getCurrentProviderSettings())
            .currentCacheProvider(cacheProvidersService.getCurrentCacheProvider())
            .build();
    }
}
