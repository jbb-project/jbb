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

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Properties;

import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Component
public class ProxyAwareCachingProvider implements CachingProvider, ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    @SuppressFBWarnings(value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD",
            justification = "Using spring bean inside class not managed by spring needs to inject ctx by static field")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private CachingProvider getCachingProvider() {
        ProxyJCacheManager proxyJCacheManager = context.getBean(ProxyJCacheManager.class);
        return proxyJCacheManager.getCacheManagerBeingProxied().getCachingProvider();
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
        return getCachingProvider().getCacheManager(uri, classLoader, properties);
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        return getCachingProvider().getDefaultClassLoader();
    }

    @Override
    public URI getDefaultURI() {
        return getCachingProvider().getDefaultURI();
    }

    @Override
    public Properties getDefaultProperties() {
        return getCachingProvider().getDefaultProperties();
    }

    @Override
    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return getCachingProvider().getCacheManager(uri, classLoader);
    }

    @Override
    public CacheManager getCacheManager() {
        return getCachingProvider().getCacheManager();
    }

    @Override
    public void close() {
        getCachingProvider().close();
    }

    @Override
    public void close(ClassLoader classLoader) {
        getCachingProvider().close(classLoader);
    }

    @Override
    public void close(URI uri, ClassLoader classLoader) {
        getCachingProvider().close(uri, classLoader);
    }

    @Override
    public boolean isSupported(OptionalFeature optionalFeature) {
        return getCachingProvider().isSupported(optionalFeature);
    }

}
