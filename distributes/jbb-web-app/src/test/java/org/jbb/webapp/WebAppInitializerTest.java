/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class WebAppInitializerTest {
    @Mock
    private ServletContext servletContextMock;

    private WebAppInitializer webAppInitializer;

    @Before
    public void setUp() throws Exception {
        // given
        webAppInitializer = new WebAppInitializer();

        given(servletContextMock.addServlet(any(String.class), any(Servlet.class)))
                .willReturn(Mockito.mock(ServletRegistration.Dynamic.class));

        given(servletContextMock.addFilter(any(String.class), any(Class.class)))
                .willReturn(Mockito.mock(FilterRegistration.Dynamic.class));
    }

    @Test
    public void shouldAddServletToServletContext() throws Exception {
        // when
        webAppInitializer.onStartup(servletContextMock);

        // then
        verify(servletContextMock, times(1))
                .addServlet(eq(WebAppInitializer.SERVLET_NAME), any(DispatcherServlet.class));
    }

    @Test
    public void shouldAddSpringSecurityFilterToServletContext() throws Exception {
        // when
        webAppInitializer.onStartup(servletContextMock);

        // then
        verify(servletContextMock, times(1))
                .addFilter(eq(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME), eq(DelegatingFilterProxy.class));
    }
}