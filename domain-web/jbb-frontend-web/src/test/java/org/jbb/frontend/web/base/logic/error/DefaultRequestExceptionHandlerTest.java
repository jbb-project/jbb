/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jbb.frontend.web.base.logic.BoardNameInterceptor;
import org.jbb.frontend.web.base.logic.JbbVersionInterceptor;
import org.jbb.frontend.web.base.logic.ReplacingViewInterceptor;
import org.jbb.lib.commons.web.ClientStackTraceProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRequestExceptionHandlerTest {
    @Mock
    HttpServletRequest requestMock;
    @Mock
    HttpServletResponse responseMock;
    @Mock
    private ClientStackTraceProvider clientStackTraceProviderMock;
    @Mock
    private BoardNameInterceptor boardNameInterceptorMock;
    @Mock
    private JbbVersionInterceptor jbbVersionInterceptorMock;
    @Mock
    private ReplacingViewInterceptor replacingViewInterceptorMock;
    @InjectMocks
    private DefaultRequestExceptionHandler defaultRequestExceptionHandler;

    @Test
    public void shouldReturnErrorView_whenException() throws Exception {
        // given
        Exception e = new Exception();

        given(clientStackTraceProviderMock.getClientStackTrace(any(Exception.class)))
                .willReturn(Optional.empty());

        // when
        ModelAndView modelAndView = defaultRequestExceptionHandler.defaultErrorHandler(requestMock, responseMock, e);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("defaultException");
    }

    @Test
    public void shouldUseReplaceViewInterceptor_whenException() throws Exception {
        // given when
        prepareExceptionState();

        // then
        verify(replacingViewInterceptorMock, times(1)).postHandle(eq(requestMock), eq(responseMock), eq(defaultRequestExceptionHandler), any());
    }

    @Test
    public void shouldUseBoardNameInterceptor_whenException() throws Exception {
        // given when
        prepareExceptionState();

        // then
        verify(boardNameInterceptorMock, times(1)).preHandle(eq(requestMock), eq(responseMock), eq(defaultRequestExceptionHandler));
    }

    @Test
    public void shouldUseJbbVersionInterceptor_whenException() throws Exception {
        // given when
        prepareExceptionState();

        // then
        verify(jbbVersionInterceptorMock, times(1)).preHandle(eq(requestMock), eq(responseMock), eq(defaultRequestExceptionHandler));
    }

    private void prepareExceptionState() {
        // given
        given(clientStackTraceProviderMock.getClientStackTrace(any(Exception.class)))
                .willReturn(Optional.empty());

        // when
        defaultRequestExceptionHandler.defaultErrorHandler(requestMock, responseMock, new Exception());
    }
}