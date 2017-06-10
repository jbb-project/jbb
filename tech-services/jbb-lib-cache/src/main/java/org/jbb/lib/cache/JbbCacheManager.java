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

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JbbCacheManager {
    public static final String CAFFEINE_PROVIDER_NAME = "caffeine";
    public static final String HAZELCAST_CLIENT_PROVIDER_NAME = "hazelcast-client";
    public static final String HAZELCAST_SERVER_PROVIDER_NAME = "hazelcast-server";

    public static final List<String> CACHE_PROVIDER_AVAILABLE_NAMES = Lists.newArrayList(
            CAFFEINE_PROVIDER_NAME, HAZELCAST_CLIENT_PROVIDER_NAME, HAZELCAST_SERVER_PROVIDER_NAME);

    private final ProxyJCacheManager proxyJCacheManager;
    private final ProxySpringCacheManager proxySpringCacheManager;
    private final SpringCacheManagerFactory springCacheManagerFactory;

    @Autowired
    public JbbCacheManager(ProxyJCacheManager proxyJCacheManager,
                           ProxySpringCacheManager proxySpringCacheManager,
                           SpringCacheManagerFactory springCacheManagerFactory) {
        this.proxyJCacheManager = proxyJCacheManager;
        this.proxySpringCacheManager = proxySpringCacheManager;
        this.springCacheManagerFactory = springCacheManagerFactory;
    }

    public void refresh() {
        if (!proxyJCacheManager.isClosed()) {
            proxyJCacheManager.close();
        }

        proxySpringCacheManager.setCacheManagerBeingProxied(springCacheManagerFactory.build());
    }
}
