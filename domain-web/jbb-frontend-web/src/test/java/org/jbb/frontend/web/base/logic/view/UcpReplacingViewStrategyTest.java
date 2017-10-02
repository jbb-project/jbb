/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.view;

import com.google.common.collect.Maps;

import org.jbb.frontend.api.ucp.UcpService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UcpReplacingViewStrategyTest {
    @Mock
    private ModelAndView modelAndViewMock;

    @Spy
    private Map<String, Object> modelSpy = Maps.newHashMap();

    @Mock
    private UcpService ucpServiceMock;

    @InjectMocks
    private UcpReplacingViewStrategy ucpReplacingViewStrategy;

    @Before
    public void setUp() throws Exception {
        given(modelAndViewMock.getModel()).willReturn(modelSpy);
    }

    @Test
    public void shouldHandle_whenViewNameStartsWithUcp() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("ucp/overview/statistics");

        // when
        boolean canHandle = ucpReplacingViewStrategy.canHandle(modelAndViewMock);

        // then
        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldNotHandle_whenViewNameNotStartWithUcp() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("members");

        // when
        boolean canHandle = ucpReplacingViewStrategy.canHandle(modelAndViewMock);

        // then
        assertThat(canHandle).isFalse();
    }

    @Test
    public void shouldSetUcpLayoutAsContentViewName() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("ucp/profile/edit");

        // when
        ucpReplacingViewStrategy.performHandle(modelAndViewMock);

        // then
        verify(modelSpy, times(1)).put(eq("contentViewName"), eq("ucpLayout"));
    }

    @Test
    public void shouldSetDefaultLayoutAsViewName() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("ucp/profile/edit");

        // when
        ucpReplacingViewStrategy.performHandle(modelAndViewMock);

        // then
        verify(modelAndViewMock, times(1)).setViewName("defaultLayout");
    }

}