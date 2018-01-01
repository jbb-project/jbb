/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InterceptorRegistryUpdaterTest {
    @Mock
    private ApplicationContext appContextMock;

    @InjectMocks
    private InterceptorRegistryUpdater interceptorRegistryUpdater;

    @Test
    public void shouldAddAllFoundInterceptorsOnClasspath() {
        // given
        FirstInterceptor firstInterceptor = mock(FirstInterceptor.class);
        SecondInterceptor secondInterceptor = mock(SecondInterceptor.class);
        given(appContextMock.getBeanNamesForType(eq(HandlerInterceptorAdapter.class)))
                .willAnswer(invocationOnMock -> new String[]{"firstInterceptor", "secondInterceptor"});

        given(appContextMock.getBean(eq("firstInterceptor"))).willReturn(firstInterceptor);
        given(appContextMock.getBean(eq("secondInterceptor"))).willReturn(secondInterceptor);

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