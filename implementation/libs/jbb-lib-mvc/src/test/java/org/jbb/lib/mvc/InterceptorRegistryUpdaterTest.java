/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InterceptorRegistryUpdaterTest {
    @Mock
    private Reflections reflectionsMock;

    @Mock
    private ApplicationContext appContextMock;

    private InterceptorRegistryUpdater interceptorRegistryUpdater;

    @Test
    public void shouldAddAllFoundInterceptorsOnClasspath() throws Exception {
        // given
        FirstInterceptor firstInterceptor = mock(FirstInterceptor.class);
        SecondInterceptor secondInterceptor = mock(SecondInterceptor.class);
        given(reflectionsMock.getSubTypesOf(eq(HandlerInterceptorAdapter.class)))
                .willAnswer(invocationOnMock -> Sets.newHashSet(FirstInterceptor.class, SecondInterceptor.class));

        interceptorRegistryUpdater = new InterceptorRegistryUpdater(reflectionsMock);
        interceptorRegistryUpdater.setApplicationContext(appContextMock);

        given(appContextMock.getBean(eq(FirstInterceptor.class))).willReturn(firstInterceptor);
        given(appContextMock.getBean(eq(SecondInterceptor.class))).willReturn(secondInterceptor);

        InterceptorRegistry interceptorRegistryMock = mock(InterceptorRegistry.class);

        // when
        interceptorRegistryUpdater.fill(interceptorRegistryMock);

        // then
        verify(interceptorRegistryMock, times(1)).addInterceptor(eq(firstInterceptor));
        verify(interceptorRegistryMock, times(1)).addInterceptor(eq(secondInterceptor));
    }

    private class FirstInterceptor extends HandlerInterceptorAdapter {

    }

    private class SecondInterceptor extends HandlerInterceptorAdapter {

    }

}