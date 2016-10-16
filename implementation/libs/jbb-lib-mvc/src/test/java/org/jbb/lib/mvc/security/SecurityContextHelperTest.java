/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.web.context.HttpRequestResponseHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SecurityContextHelperTest {
    @Mock
    private RefreshableSecurityContextRepository securityContextRepositoryMock;

    @InjectMocks
    private SecurityContextHelper securityContextHelper;

    @Test
    public void shouldInvokeLoadingContext_withHttpRequestResponseHolder() throws Exception {
        // given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        HttpServletResponse responseMock = mock(HttpServletResponse.class);

        // when
        securityContextHelper.refresh(requestMock, responseMock);

        // then
        verify(securityContextRepositoryMock, times(1)).loadContext(argThat(arg -> {
            if (arg instanceof HttpRequestResponseHolder) {
                HttpRequestResponseHolder holder = (HttpRequestResponseHolder) arg;
                assertThat(holder.getRequest()).isEqualTo(requestMock);
                assertThat(holder.getResponse()).isEqualTo(responseMock);
                return true;
            } else {
                return false;
            }
        }));


    }

}