/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache.logic;

import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.cache.CacheProperties;
import org.jbb.system.api.cache.CacheProvider;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.CacheSettingsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheSettingsServiceImpl implements CacheSettingsService {
    private final CacheProperties cacheProperties;
    private final Validator validator;

    @Override
    public CacheSettings getCacheSettings() {
        return CacheSettings.builder()
            .applicationCacheEnabled(cacheProperties.applicationCacheEnabled())
            .secondLevelCacheEnabled(cacheProperties.secondLevelCacheEnabled())
            .queryCacheEnabled(cacheProperties.queryCacheEnabled())
            .currentCacheProvider(CacheProvider.CAFFEINE_EMBEDDED)
            .build();
    }

    @Override
    public void setCacheSettings(CacheSettings newCacheSettings) {
        Validate.notNull(newCacheSettings);

        cacheProperties.setProperty(CacheProperties.APPLICATION_CACHE_ENABLED,
                Boolean.toString(newCacheSettings.isApplicationCacheEnabled()));
        cacheProperties.setProperty(CacheProperties.SECOND_LEVEL_CACHE_ENABLED,
                Boolean.toString(newCacheSettings.isSecondLevelCacheEnabled()));
        cacheProperties.setProperty(CacheProperties.QUERY_CACHE_ENABLED,
                Boolean.toString(newCacheSettings.isQueryCacheEnabled()));
    }
}
