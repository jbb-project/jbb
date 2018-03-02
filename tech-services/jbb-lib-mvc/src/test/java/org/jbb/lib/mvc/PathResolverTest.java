/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc;

import org.jbb.lib.commons.web.HttpServletRequestHolder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.util.UrlPathHelper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PathResolverTest {
    @Mock
    private HttpServletRequestHolder httpServletRequestHolderMock;

    @Mock
    private UrlPathHelper urlPathHelperMock;

    @InjectMocks
    private PathResolver pathResolver;

    @Test
    public void shouldDetectApiPath_whenRequestToUrlWithApi() {
        // given
        given(urlPathHelperMock.getPathWithinApplication(any())).willReturn("/api");

        // when
        boolean result = pathResolver.isRequestToApi();

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldDetectApiPath_whenRequestToUrlWithApiWithSlash() {
        // given
        given(urlPathHelperMock.getPathWithinApplication(any())).willReturn("/api/");

        // when
        boolean result = pathResolver.isRequestToApi();

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldDetectApiPath_whenRequestToUrlWithApiAndSuffix() {
        // given
        given(urlPathHelperMock.getPathWithinApplication(any())).willReturn("/api/v1/members");

        // when
        boolean result = pathResolver.isRequestToApi();

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldDetectNotApiPath_whenRequestToUrlWithNotStartedWithApi() {
        // given
        given(urlPathHelperMock.getPathWithinApplication(any())).willReturn("/acp/api/v1/members");

        // when
        boolean result = pathResolver.isRequestToApi();

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldDetectNotApiPath_whenRequestToUrlStartsWithApis() {
        // given
        given(urlPathHelperMock.getPathWithinApplication(any())).willReturn("/apis");

        // when
        boolean result = pathResolver.isRequestToApi();

        // then
        assertThat(result).isFalse();
    }
}