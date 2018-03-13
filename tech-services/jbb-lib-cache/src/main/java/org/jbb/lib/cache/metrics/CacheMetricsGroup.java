/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache.metrics;

import com.google.common.collect.Lists;

import org.jbb.lib.metrics.group.MetricsGroup;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.binder.cache.JCacheMetrics;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import lombok.RequiredArgsConstructor;

@Component
@DependsOn("cacheInitializer")
@RequiredArgsConstructor
public class CacheMetricsGroup implements MetricsGroup {

    private final CacheManager cacheManager;

    @Override
    public void registerMetrics(CompositeMeterRegistry meterRegistry) {
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            javax.cache.Cache nativeCache = (javax.cache.Cache) cache.getNativeCache();
            new JCacheMetrics(nativeCache, Lists.newArrayList()).bindTo(meterRegistry);
        });
    }
}
