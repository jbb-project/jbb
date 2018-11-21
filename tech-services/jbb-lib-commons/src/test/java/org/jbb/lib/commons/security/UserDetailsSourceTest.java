/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsSourceTest {

    @Mock
    private SecurityContext securityContextMock;

    @InjectMocks
    private UserDetailsSource userDetailsSource;

    @Test
    public void shouldReturnNullUserDetails_whenNoAuthentication() {
        // given
        SecurityContextHolder.setContext(securityContextMock);
        given(securityContextMock.getAuthentication()).willReturn(null);

        // when
        SecurityContentUser userDetails = userDetailsSource.getFromApplicationContext();

        // then
        assertThat(userDetails).isNull();
    }

    @Test
    public void shouldReturnNotNullUserDetails_whenAuthenticationPresent() {
        // given
        SecurityContextHolder.setContext(securityContextMock);

        Authentication authMock = mock(Authentication.class);
        given(authMock.getPrincipal()).willReturn(mock(SecurityContentUser.class));

        given(securityContextMock.getAuthentication()).willReturn(authMock);

        // when
        SecurityContentUser userDetails = userDetailsSource.getFromApplicationContext();

        // then
        assertThat(userDetails).isNotNull();
    }
}