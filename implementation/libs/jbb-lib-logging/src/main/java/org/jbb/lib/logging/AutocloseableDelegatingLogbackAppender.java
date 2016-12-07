/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

import java.util.List;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.ext.spring.ApplicationContextHolder;
import ch.qos.logback.ext.spring.EventCacheMode;
import ch.qos.logback.ext.spring.ILoggingEventCache;

class AutocloseableDelegatingLogbackAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

    private final Object lock;

    private String beanName;
    private ILoggingEventCache cache;
    private EventCacheMode cacheMode;
    private volatile Appender<ILoggingEvent> delegate;

    public AutocloseableDelegatingLogbackAppender() {
        cacheMode = EventCacheMode.ON;
        lock = new Object();
    }

    public void setCacheMode(String mode) {
        cacheMode = Enum.valueOf(EventCacheMode.class, mode.toUpperCase());
    }

    @Override
    public void start() {
        if (isStarted()) {
            return;
        }

        if (beanName == null || beanName.trim().isEmpty()) {
            throw new IllegalStateException("A 'beanName' is required for DelegatingLogbackAppender");
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

    @Override
    protected void append(ILoggingEvent event) {
        //Double-check locking here to optimize out the synchronization after the delegate is in place. This also has
        //the benefit of dealing with the race condition where 2 threads are trying to log and one gets the lock with
        //the other waiting and the lead thread sets the delegate, logs all cached events and then returns, allowing
        //the blocked thread to acquire the lock. At that time, the delegate is no longer null and the event is logged
        //directly to it, rather than being cached.
        if (delegate == null) {
            synchronized (lock) {
                //Note the isStarted() check here. If multiple threads are logging at the time the ApplicationContext
                //becomes available, the first thread to acquire the lock _may_ stop this appender if the context does
                //not contain an Appender with the expected name. If that happens, when the lock is released and other
                //threads acquire it, isStarted() will return false and those threads should return without trying to
                //use either the delegate or the cache--both of which will be null.
                if (!isStarted()) {
                    return;
                }
                //If we're still started either no thread has attempted to load the delegate yet, or the delegate has
                //been loaded successfully. If the latter, the delegate will no longer be null
                if (delegate == null) {
                    if (ApplicationContextHolder.hasApplicationContext()) {
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
                    } else {
                        //Otherwise, if the ApplicationContext is not ready yet, cache this event and wait
                        cache.put(event);
                        return;
                    }
                }
            }
        }
    }

    private Appender<ILoggingEvent> getDelegate() {
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();

        try {
            @SuppressWarnings("unchecked")
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

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}