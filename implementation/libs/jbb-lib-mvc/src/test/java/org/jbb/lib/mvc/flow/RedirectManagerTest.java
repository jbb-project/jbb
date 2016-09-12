/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.flow;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

public class RedirectManagerTest {
    private RedirectManager redirectManager;

    @Before
    public void setUp() throws Exception {
        redirectManager = new RedirectManager();
    }

    @Test
    public void shouldRedirectToPreviousPage_whenRefererPassed() throws Exception {
        // given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        BDDMockito.given(requestMock.getHeader(eq("referer"))).willReturn("http://localhost:8181/hehe");

        // when
        String springMvcViewName = redirectManager.goToPreviousPage(requestMock);

        // then
        assertThat(springMvcViewName).isEqualTo("redirect:http://localhost:8181/hehe");
    }

    @Test
    public void shouldRedirectToHomePage_whenRefererNull_andUserNotSetRedicationPage() throws Exception {
        // given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        BDDMockito.given(requestMock.getHeader(eq("referer"))).willReturn(null);

        // when
        String springMvcViewName = redirectManager.goToPreviousPage(requestMock);

        // then
        assertThat(springMvcViewName).isEqualTo("redirect:/");
    }

    @Test
    public void shouldRedirectToHomePage_whenRefererNotPassed_andUserNotSetRedicationPage() throws Exception {
        // given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        BDDMockito.given(requestMock.getHeader(eq("referer"))).willReturn(StringUtils.EMPTY);

        // when
        String springMvcViewName = redirectManager.goToPreviousPage(requestMock);

        // then
        assertThat(springMvcViewName).isEqualTo("redirect:/");
    }

    @Test
    public void shouldRedirectToCustomPage_whenRefererNotPassed_andUserSetRedicationPage() throws Exception {
        // given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        BDDMockito.given(requestMock.getHeader(eq("referer"))).willReturn(StringUtils.EMPTY);

        // when
        String springMvcViewName = redirectManager.goToPreviousPageOr(requestMock, "/foo");

        // then
        assertThat(springMvcViewName).isEqualTo("redirect:/foo");
    }
}