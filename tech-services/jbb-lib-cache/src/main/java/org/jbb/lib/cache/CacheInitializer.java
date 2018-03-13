/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache;

import com.hazelcast.config.Config;

import org.jbb.lib.cache.hazelcast.HazelcastConfigFilesManager;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Set;

import javax.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CacheInitializer {
    public static final String ASTERISK_CACHE_NAME = "*";

    private final HazelcastConfigFilesManager hazelcastConfigFilesManager;
    private final CacheManager cacheManager;

    @PostConstruct
    public void initCaches() {
        Config serverConfig = hazelcastConfigFilesManager.getHazelcastServerConfig();
        Set<String> cacheNames = serverConfig.getCacheConfigs().keySet();
        cacheNames.remove(ASTERISK_CACHE_NAME);
        cacheNames.forEach(cacheManager::getCache);
    }
}
