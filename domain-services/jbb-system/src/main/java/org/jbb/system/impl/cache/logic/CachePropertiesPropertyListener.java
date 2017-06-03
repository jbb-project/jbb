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

import org.jbb.lib.cache.CacheProperties;
import org.jbb.lib.cache.JbbCacheManager;
import org.jbb.system.impl.database.logic.ConnectionToDatabaseEventSender;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.annotation.PostConstruct;

@Component
public class CachePropertiesPropertyListener implements PropertyChangeListener, ApplicationContextAware {
    private final JbbCacheManager jbbCacheManager;
    private final ConnectionToDatabaseEventSender connectionToDatabaseEventSender;

    private ApplicationContext applicationContext;

    @Autowired
    public CachePropertiesPropertyListener(JbbCacheManager jbbCacheManager,
                                           ConnectionToDatabaseEventSender connectionToDatabaseEventSender) {
        this.jbbCacheManager = jbbCacheManager;
        this.connectionToDatabaseEventSender = connectionToDatabaseEventSender;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void addListenerToCacheProperties() {
        applicationContext.getBean(CacheProperties.class).addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        jbbCacheManager.refresh();
        connectionToDatabaseEventSender.emitEvent();
    }

}
