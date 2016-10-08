/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.stacktrace.logic;

import org.jbb.frontend.web.base.logic.ReplacingViewInterceptor;
import org.jbb.system.api.service.StackTraceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRequestExceptionHandlerTest {
    @Mock
    private StackTraceService stackTraceServiceMock;

    @Mock
    private ReplacingViewInterceptor replacingViewInterceptorMock;

    @InjectMocks
    private DefaultRequestExceptionHandler defaultRequestExceptionHandler;

    @Test
    public void shouldReturn404View_whenNoHandlerFoundException() throws Exception {
        // when
        ModelAndView modelAndView = defaultRequestExceptionHandler.notFoundExceptionHandler();

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("notFoundException");
    }

    @Test
    public void shouldReturnErrorView_whenException() throws Exception {
        // given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        Exception e = new Exception();

        given(stackTraceServiceMock.getStackTraceAsString(any(Exception.class)))
                .willReturn(Optional.empty());

        // when
        ModelAndView modelAndView = defaultRequestExceptionHandler.defaultErrorHandler(requestMock, responseMock, e);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("defaultException");
    }

    @Test
    public void shouldUseReplaceViewInterceptor_whenException() throws Exception {
        // given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        Exception e = new Exception();

        given(stackTraceServiceMock.getStackTraceAsString(any(Exception.class)))
                .willReturn(Optional.empty());

        // when
        defaultRequestExceptionHandler.defaultErrorHandler(requestMock, responseMock, e);

        // then
        verify(replacingViewInterceptorMock, times(1)).postHandle(eq(requestMock), eq(responseMock), eq(defaultRequestExceptionHandler), any());
    }
}