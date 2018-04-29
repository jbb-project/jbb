/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.install.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jbb.lib.commons.preinstall.JbbNoInstalledException;
import org.jbb.lib.mvc.PathResolver;
import org.jbb.system.api.install.InstallationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PreInstallationApiInterceptorTest {

    @Mock
    private InstallationService installationServiceMock;

    @Mock
    private PathResolver pathResolverMock;

    @InjectMocks
    private PreInstallationApiInterceptor preInstallationApiInterceptor;


    @Test(expected = JbbNoInstalledException.class)
    public void shouldThrowJbbNoInstalledExceptionWhenJbbNotInstalled_andItIsRequestToApi()
        throws Exception {
        // given
        given(installationServiceMock.isInstalled()).willReturn(false);
        given(pathResolverMock.isRequestToApi()).willReturn(true);

        // when
        preInstallationApiInterceptor.preHandle(mock(HttpServletRequest.class), mock(
            HttpServletResponse.class), null);

        // then
        // throw JbbNoInstalledException
    }

    @Test
    public void shouldReturnTrueWhenJbbNotInstalled_andItIsNotRequestToApi() throws Exception {
        // given
        given(installationServiceMock.isInstalled()).willReturn(false);
        given(pathResolverMock.isRequestToApi()).willReturn(false);

        // when
        boolean result = preInstallationApiInterceptor
            .preHandle(mock(HttpServletRequest.class), mock(
                HttpServletResponse.class), null);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnTrueWhenJbbInstalled_andItIsRequestToApi() throws Exception {
        // given
        given(installationServiceMock.isInstalled()).willReturn(true);
        given(pathResolverMock.isRequestToApi()).willReturn(true);

        // when
        boolean result = preInstallationApiInterceptor
            .preHandle(mock(HttpServletRequest.class), mock(
                HttpServletResponse.class), null);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldReturnTrueWhenJbbInstalled_andItIsNotRequestToApi() throws Exception {
        // given
        given(installationServiceMock.isInstalled()).willReturn(true);
        given(pathResolverMock.isRequestToApi()).willReturn(false);

        // when
        boolean result = preInstallationApiInterceptor
            .preHandle(mock(HttpServletRequest.class), mock(
                HttpServletResponse.class), null);

        // then
        assertThat(result).isTrue();
    }
}