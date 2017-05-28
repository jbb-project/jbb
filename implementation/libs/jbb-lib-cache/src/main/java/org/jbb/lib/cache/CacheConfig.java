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

import org.jbb.lib.properties.ModulePropertiesFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
@EnableCaching
@ComponentScan("org.jbb.lib.cache")
public class CacheConfig {

    @Bean
    public CacheProperties cacheProperties(ModulePropertiesFactory propertiesFactory) {
        return propertiesFactory.create(CacheProperties.class);
    }

    @Bean
    @Primary
    public CacheManager cacheManager(SpringCacheManagerFactory springCacheManagerFactory) {
        ProxySpringCacheManager proxySpringCacheManager = new ProxySpringCacheManager();
        proxySpringCacheManager.setCacheManagerBeingProxied(springCacheManagerFactory.build());
        return proxySpringCacheManager;
    }


}
