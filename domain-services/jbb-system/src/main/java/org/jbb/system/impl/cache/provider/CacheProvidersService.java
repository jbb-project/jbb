/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache.provider;

import com.google.common.collect.ImmutableMap;

import org.jbb.lib.cache.CacheProperties;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CacheProvidersService {

    private static final Map<String, Class<? extends CacheProviderManager>> PROVIDERS =
            ImmutableMap.<String, Class<? extends CacheProviderManager>>builder()
                    .put(CaffeineProviderManager.PROVIDER_PROPERTY_VALUE, CaffeineProviderManager.class)
                    .put(HazelcastServerProviderManager.PROVIDER_PROPERTY_VALUE,
                            HazelcastServerProviderManager.class)
                    .put(HazelcastClientProviderManager.PROVIDER_PROPERTY_VALUE,
                            HazelcastClientProviderManager.class)
                    .build();

    private final CacheProperties cacheProperties;
    private final ApplicationContext applicationContext;

    public CacheProviderManager getManagerForCurrentProvider() {
        String providerName = cacheProperties.providerName();
        return getManagerForProviderName(providerName);
    }

    private CacheProviderManager getManagerForProviderName(String providerName) {
        Class managerClass = PROVIDERS.get(providerName.trim().toLowerCase());

        if (managerClass != null) {
            return (CacheProviderManager) applicationContext.getBean(managerClass);
        }

        throw new IllegalStateException(
                String.format("No cache provider with name: %s", providerName));

    }

    public CacheProvider getCurrentCacheProvider() {
        return getManagerForCurrentProvider().getProviderName();
    }

    public void setSettingsForAllProviders(CacheSettings newCacheSettings) {
        PROVIDERS.values().forEach(
                providerManagerClass -> applicationContext.getBean(providerManagerClass)
                        .setProviderSettings(newCacheSettings)
        );
    }

    public void setNewProvider(CacheSettings newCacheSettings) {
        CacheProvider cacheProviderName = newCacheSettings.getCurrentCacheProvider();
        String formattedProviderName = cacheProviderName.toString().replaceAll("_", "-").trim()
                .toLowerCase();
        getManagerForProviderName(formattedProviderName).setAsCurrentProvider(newCacheSettings);
        cacheProperties.setProperty(CacheProperties.PROVIDER_NAME, formattedProviderName);
    }
}
