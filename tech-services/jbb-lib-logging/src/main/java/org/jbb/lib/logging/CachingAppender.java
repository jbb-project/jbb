/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.ext.spring.ApplicationContextHolder;
import ch.qos.logback.ext.spring.EventCacheMode;
import ch.qos.logback.ext.spring.ILoggingEventCache;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

class CachingAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private final Object lock;

    private String beanName;
    private ILoggingEventCache cache;
    private EventCacheMode cacheMode;
    private volatile Appender<ILoggingEvent> delegate;

    public CachingAppender() {
        lock = new Object();
    }

    public void setCacheMode(EventCacheMode mode) {
        cacheMode = mode;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void start() {
        if (isStarted()) {
            return;
        }

        if (StringUtils.isEmpty(beanName)) {
            throw new IllegalStateException("A 'beanName' is required for CachingAppender");
        }
        cache = cacheMode.createCache();

        super.start();
    }

    @Override
    public void stop() {
        super.stop();

        if (cache != null) {
            cache = null;
        }
        if (delegate != null) {
            delegate.stop();
            delegate = null;
        }
    }

    public ILoggingEventCache getCache() {
        return cache;
    }

    @Override
    protected void append(ILoggingEvent event) {
        if (delegate != null) {
            return;
        }

        synchronized (lock) {
            if (!isStarted() || delegate != null) {
                return;
            }

            if (ApplicationContextHolder.hasApplicationContext()) {
                appendCachedEvents();
            } else {
                putToCache(event);
            }
        }
    }

    private void appendCachedEvents() {
        //First, load the delegate Appender from the ApplicationContext. If it cannot be loaded, this
        //appender will be stopped and null will be returned.
        Appender<ILoggingEvent> appender = getDelegate();
        if (appender == null) {
            return;
        }

        //Once we have the appender, unload the cache to it.
        List<ILoggingEvent> cachedEvents = cache.get();
        for (ILoggingEvent cachedEvent : cachedEvents) {
            appender.doAppend(cachedEvent);
        }

        //If we've found our delegate appender, we no longer need the cache.
        cache = null;
        delegate = appender;
        stop();
    }

    private void putToCache(ILoggingEvent event) {
        //Otherwise, if the ApplicationContext is not ready yet, cache this event and wait
        cache.put(event);
    }

    private Appender<ILoggingEvent> getDelegate() {
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();

        try {
            Appender<ILoggingEvent> appender = context.getBean(beanName, Appender.class);
            appender.setContext(getContext());
            if (!appender.isStarted()) {
                appender.start();
            }
            return appender;
        } catch (NoSuchBeanDefinitionException e) {
            stop();
            addError("The ApplicationContext does not contain an Appender named [" + beanName +
                    "]. This delegating appender will now stop processing events.", e);
        }
        return null;
    }
}