/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.interceptors;

import org.jbb.lib.core.JbbMetaData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JbbVersionInterceptorTest {
    private static final HttpServletResponse ANY_HTTP_RESPONSE = null;
    private static final Object ANY_HANDLER = null;

    @Mock
    private JbbMetaData jbbMetaDataMock;

    @InjectMocks
    private JbbVersionInterceptor jbbVersionInterceptor;

    @Test
    public void shouldSetVersionFromComponentToRequestAttribute() throws Exception {
        // given
        String version = "0.4.0";
        given(jbbMetaDataMock.jbbVersion()).willReturn(version);

        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);

        // when
        jbbVersionInterceptor.preHandle(requestMock, ANY_HTTP_RESPONSE, ANY_HANDLER);

        // then
        verify(requestMock, times(1)).setAttribute(eq("jbbVersion"), eq(version));
    }

    @Test
    public void shouldProcessingMoveOn() throws Exception {
        // given
        given(jbbMetaDataMock.jbbVersion()).willReturn("0.8.1");
        HttpServletRequest requestMock = Mockito.mock(HttpServletRequest.class);

        // when
        boolean process = jbbVersionInterceptor.preHandle(requestMock, ANY_HTTP_RESPONSE, ANY_HANDLER);

        // then
        assertThat(process).isTrue();
    }


}