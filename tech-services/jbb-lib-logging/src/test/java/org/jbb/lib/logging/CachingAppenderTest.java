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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.ext.spring.ApplicationContextHolder;
import ch.qos.logback.ext.spring.EventCacheMode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ApplicationContextHolder.class)
public class CachingAppenderTest {

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenStart_andBeanNameNotSet() throws Exception {
        // given
        CachingAppender cachingAppender = new CachingAppender();

        // when
        cachingAppender.start();

        // then
        // throw IllegalStateException
    }

    @Test
    public void shouldPutLoggingEventsToCache_whenApplicationContextNotStarted() throws Exception {
        // given
        PowerMockito.mockStatic(ApplicationContextHolder.class);
        PowerMockito.when(ApplicationContextHolder.hasApplicationContext()).thenReturn(false);

        ILoggingEvent firstEvent = mock(ILoggingEvent.class);
        ILoggingEvent secondEvent = mock(ILoggingEvent.class);

        // when
        CachingAppender cachingAppender = new CachingAppender();
        cachingAppender.setCacheMode(EventCacheMode.ON);
        cachingAppender.setBeanName("aggregateAppenderProxyBean");
        cachingAppender.start();
        cachingAppender.append(firstEvent);
        cachingAppender.append(secondEvent);

        // then
        assertThat(cachingAppender.getCache().get()).contains(firstEvent, secondEvent);
    }

    @Test
    public void shouldNotPutLoggingEventsToCache_whenApplicationContextNotStarted_butCachingIsDisabled() throws Exception {
        // given
        PowerMockito.mockStatic(ApplicationContextHolder.class);
        PowerMockito.when(ApplicationContextHolder.hasApplicationContext()).thenReturn(false);

        ILoggingEvent firstEvent = mock(ILoggingEvent.class);
        ILoggingEvent secondEvent = mock(ILoggingEvent.class);

        // when
        CachingAppender cachingAppender = new CachingAppender();
        cachingAppender.setCacheMode(EventCacheMode.OFF);
        cachingAppender.setBeanName("aggregateAppenderProxyBean");
        cachingAppender.start();
        cachingAppender.append(firstEvent);
        cachingAppender.append(secondEvent);

        // then
        assertThat(cachingAppender.getCache().get()).isEmpty();
    }

    @Test
    public void shouldRemoveCache_whenAppenderIsStopped() throws Exception {
        // given
        PowerMockito.mockStatic(ApplicationContextHolder.class);
        PowerMockito.when(ApplicationContextHolder.hasApplicationContext()).thenReturn(false);

        ILoggingEvent firstEvent = mock(ILoggingEvent.class);
        ILoggingEvent secondEvent = mock(ILoggingEvent.class);

        // when
        CachingAppender cachingAppender = new CachingAppender();
        cachingAppender.setCacheMode(EventCacheMode.OFF);
        cachingAppender.setBeanName("aggregateAppenderProxyBean");
        cachingAppender.start();
        cachingAppender.append(firstEvent);
        cachingAppender.append(secondEvent);
        cachingAppender.stop();

        // then
        assertThat(cachingAppender.getCache()).isNull();
    }

    @Test
    public void startingManyTimes_doesNotBreakAnything() throws Exception {
        // given
        PowerMockito.mockStatic(ApplicationContextHolder.class);
        PowerMockito.when(ApplicationContextHolder.hasApplicationContext()).thenReturn(false);

        // when
        CachingAppender cachingAppender = new CachingAppender();
        cachingAppender.setCacheMode(EventCacheMode.SOFT);
        cachingAppender.setBeanName("aggregateAppenderProxyBean");
        cachingAppender.start();
        cachingAppender.start();
        cachingAppender.start();

        // then
        assertThat(cachingAppender.isStarted()).isTrue();
    }

    @Test
    public void shouldAppendCachedEvents_whenApplicationContextIsStarted() throws Exception {
        // given
        PowerMockito.mockStatic(ApplicationContextHolder.class);
        PowerMockito.when(ApplicationContextHolder.hasApplicationContext()).thenReturn(false);

        ILoggingEvent firstEvent = mock(ILoggingEvent.class);
        ILoggingEvent secondEvent = mock(ILoggingEvent.class);
        ILoggingEvent thirdEvent = mock(ILoggingEvent.class);

        // when
        CachingAppender cachingAppender = new CachingAppender();
        cachingAppender.setCacheMode(EventCacheMode.ON);
        cachingAppender.setBeanName("aggregateAppenderProxyBean");
        cachingAppender.start();
        cachingAppender.append(firstEvent);
        cachingAppender.append(secondEvent);

        PowerMockito.when(ApplicationContextHolder.hasApplicationContext()).thenReturn(true);
        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        PowerMockito.when(ApplicationContextHolder.getApplicationContext()).thenReturn(applicationContextMock);
        Appender appenderMock = mock(Appender.class);
        when(applicationContextMock.getBean(any(), eq(Appender.class))).thenReturn(appenderMock);
        cachingAppender.append(thirdEvent);

        // then
        assertThat(cachingAppender.getCache()).isNull();
        Mockito.verify(appenderMock, times(1)).doAppend(eq(firstEvent));
        Mockito.verify(appenderMock, times(1)).doAppend(eq(secondEvent));
    }
}