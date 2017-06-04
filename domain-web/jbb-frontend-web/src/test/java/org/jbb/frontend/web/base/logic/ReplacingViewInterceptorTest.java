/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic;

import com.google.common.collect.Lists;

import org.jbb.frontend.web.base.logic.view.ReplacingViewStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ReplacingViewInterceptorTest {
    private static final HttpServletRequest ANY_HTTP_REQUEST = null;
    private static final HttpServletResponse ANY_HTTP_RESPONSE = null;
    private static final Object ANY_HANDLER = null;
    private static final ModelAndView ANY_MODEL_AND_VIEW = null;

    @Mock
    private ReplacingViewStrategy firstReplacingViewStrategyMock;

    @Mock
    private ReplacingViewStrategy secondReplacingViewStrategyMock;

    private ReplacingViewInterceptor replacingViewInterceptor;

    @Before
    public void setUp() throws Exception {
        replacingViewInterceptor = new ReplacingViewInterceptor(
                Lists.newArrayList(firstReplacingViewStrategyMock, secondReplacingViewStrategyMock));

    }

    @Test
    public void shouldNotInvokeSecondStrategy_whenFirstCanHandle() throws Exception {
        // given
        given(firstReplacingViewStrategyMock.handle(nullable(ModelAndView.class))).willReturn(true);

        // when
        replacingViewInterceptor.postHandle(ANY_HTTP_REQUEST, ANY_HTTP_RESPONSE, ANY_HANDLER, ANY_MODEL_AND_VIEW);

        // then
        verifyZeroInteractions(secondReplacingViewStrategyMock);
    }

    @Test
    public void shouldInvokeSecondStrategy_whenFirstCanNotHandle() throws Exception {
        // given

        // when
        replacingViewInterceptor.postHandle(ANY_HTTP_REQUEST, ANY_HTTP_RESPONSE, ANY_HANDLER, ANY_MODEL_AND_VIEW);

        // then
        verify(secondReplacingViewStrategyMock, times(1)).handle(nullable(ModelAndView.class));
    }
}