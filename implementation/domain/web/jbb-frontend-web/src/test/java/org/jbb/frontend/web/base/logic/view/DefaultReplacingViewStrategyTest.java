/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultReplacingViewStrategyTest {
    @Mock
    private ModelAndView modelAndViewMock;

    @Spy
    private Map<String, Object> modelSpy = Maps.newHashMap();

    private DefaultReplacingViewStrategy defaultReplacingViewStrategy;

    @Before
    public void setUp() throws Exception {
        defaultReplacingViewStrategy = new DefaultReplacingViewStrategy();
        given(modelAndViewMock.getModel()).willReturn(modelSpy);
    }

    @Test
    public void shouldAlwaysHandle() throws Exception {
        // when
        boolean canHandle = defaultReplacingViewStrategy.canHandle(modelAndViewMock);

        // then
        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldSetDefaultViewName_whenHandle() throws Exception {
        // when
        defaultReplacingViewStrategy.performHandle(modelAndViewMock);

        // then
        verify(modelAndViewMock, times(1)).setViewName(eq("defaultLayout"));
    }

    @Test
    public void shouldPutOriginalViewNameToModel() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("foo");

        // when
        defaultReplacingViewStrategy.performHandle(modelAndViewMock);

        // then
        verify(modelSpy, times(1)).put(eq("contentViewName"), eq("foo"));
    }
}