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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.cache.CacheProperties;
import org.jbb.lib.cache.JbbCacheManager;
import org.jbb.system.impl.database.logic.DatabaseSettingsManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("cachePropertiesPropertyListener")
@RequiredArgsConstructor
public class CachePropertiesPropertyListener implements PropertyChangeListener, ApplicationContextAware {
    private final JbbCacheManager jbbCacheManager;
    private final DatabaseSettingsManager databaseSettingsManager;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void addListenerToCacheProperties() {
        applicationContext.getBean(CacheProperties.class).addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        jbbCacheManager.refresh();
        databaseSettingsManager.triggerRefresh();
    }

}
