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

import com.google.common.collect.Maps;

import org.jbb.frontend.web.FrontendConfigMock;
import org.jbb.frontend.web.FrontendWebConfig;
import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.test.MockCoreConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreConfig.class, MvcConfig.class, FrontendWebConfig.class,
        FrontendConfigMock.class, MockCoreConfig.class})
public class ReplacingViewInterceptorIT {
    private static final HttpServletRequest ANY_HTTP_REQUEST = null;
    private static final HttpServletResponse ANY_HTTP_RESPONSE = null;
    private static final Object ANY_HANDLER = null;

    @Autowired
    private ModelAndView modelAndViewMock;

    @Autowired
    private ReplacingViewInterceptor interceptor;

    @Before
    public void setUp() throws Exception {
        when(modelAndViewMock.getModel()).thenReturn(Maps.newHashMap());
    }

    @Test
    public void shouldPutViewNameToModel_whenNoRedirect() throws Exception {
        // given
        when(modelAndViewMock.getViewName()).thenReturn("register");

        // when
        interceptor.postHandle(ANY_HTTP_REQUEST, ANY_HTTP_RESPONSE, ANY_HANDLER, modelAndViewMock);

        // then
        assertThat(modelAndViewMock.getModel()).containsEntry("contentViewName", "register");
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldSetDefaultLayoutAsView_whenNoRedirect() throws Exception {
        // given
        when(modelAndViewMock.getViewName()).thenReturn("register");

        // when
        interceptor.postHandle(ANY_HTTP_REQUEST, ANY_HTTP_RESPONSE, ANY_HANDLER, modelAndViewMock);

        // then
        verify(modelAndViewMock).setViewName(eq("defaultLayout"));
    }

    @Test
    public void shouldNotPutViewNameToModel_whenRedirect() throws Exception {
        // given
        when(modelAndViewMock.getViewName()).thenReturn("redirect:register");

        // when
        interceptor.postHandle(ANY_HTTP_REQUEST, ANY_HTTP_RESPONSE, ANY_HANDLER, modelAndViewMock);

        // then
        assertThat(modelAndViewMock.getModel()).doesNotContainKey("contentViewName");
    }

    @Test
    public void shouldNotChangeViewName_whenRedirect() throws Exception {
        // given
        when(modelAndViewMock.getViewName()).thenReturn("redirect:register");

        // when
        interceptor.postHandle(ANY_HTTP_REQUEST, ANY_HTTP_RESPONSE, ANY_HANDLER, modelAndViewMock);

        // then
        verify(modelAndViewMock, times(0)).setViewName(anyString());
    }
}