/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.interceptors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RequestTimeInterceptorTest {
    private static final HttpServletResponse ANY_RESPONSE = null;
    private static final Object ANY_HANDLER = null;

    @Mock
    private Clock clockMock;

    @InjectMocks
    private RequestTimeInterceptor requestTimeInterceptor;

    @Test
    public void shouldStoreStartTimeToRequestAttribute_whenPreHandle() {
        // given
        given(clockMock.millis()).willReturn(123456L);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        // when
        requestTimeInterceptor.preHandle(request, ANY_RESPONSE, ANY_HANDLER);

        // then
        verify(request, times(1))
                .setAttribute(eq(RequestTimeInterceptor.REQUEST_START_TIME_ATTRIBUTE), eq(123456L));
    }

    @Test
    public void shouldRetriveStartTime_whenAfterCompletion() {
        // given
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        given(request.getAttribute(anyString())).willReturn(0L);

        // when
        requestTimeInterceptor.afterCompletion(request, ANY_RESPONSE, ANY_HANDLER, new Exception());

        // then
        verify(request, times(1)).getAttribute(eq(RequestTimeInterceptor.REQUEST_START_TIME_ATTRIBUTE));
    }
}