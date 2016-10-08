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
import com.google.common.collect.TreeMultimap;

import org.jbb.frontend.api.service.AcpService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AcpReplacingViewStrategyTest {
    @Mock
    private ModelAndView modelAndViewMock;

    @Spy
    private Map<String, Object> modelSpy = Maps.newHashMap();

    @Mock
    private AcpService acpServiceMock;

    @InjectMocks
    private AcpReplacingViewStrategy acpReplacingViewStrategy;

    @Before
    public void setUp() throws Exception {
        given(modelAndViewMock.getModel()).willReturn(modelSpy);
        given(acpServiceMock.selectAllSubcategoriesAndElements(any())).willReturn(mock(TreeMultimap.class));
    }

    @Test
    public void shouldHandle_whenViewNameStartsWithAcp() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("acp/members/create");

        // when
        boolean canHandle = acpReplacingViewStrategy.canHandle(modelAndViewMock);

        // then
        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldNotHandle_whenViewNameNotStartWithAcp() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("ucp/overview/statistics");

        // when
        boolean canHandle = acpReplacingViewStrategy.canHandle(modelAndViewMock);

        // then
        assertThat(canHandle).isFalse();
    }

    @Test
    public void shouldSetAcpLayoutAsContentViewName() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("acp/general/logging");

        // when
        acpReplacingViewStrategy.performHandle(modelAndViewMock);

        // then
        verify(modelSpy, times(1)).put(eq("contentViewName"), eq("acpLayout"));
    }

    @Test
    public void shouldSetDefaultLayoutAsViewName() throws Exception {
        // given
        given(modelAndViewMock.getViewName()).willReturn("acp/system/database");

        // when
        acpReplacingViewStrategy.performHandle(modelAndViewMock);

        // then
        verify(modelAndViewMock, times(1)).setViewName("defaultLayout");
    }

}