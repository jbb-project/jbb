/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin.logic;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.service.MemberLockoutService;
import org.jbb.security.event.SignInSuccessEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RedirectAuthSuccessHandlerTest {
    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private JbbEventBus eventBusMock;

    @Mock
    private MemberLockoutService userService;

    @InjectMocks
    private RedirectAuthSuccessHandler redirectAuthSuccessHandler;

    @Test
    public void shouldEmitSignInSuccessEvent_whenUserSignin() throws Exception {
        // given
        Authentication authenticationMock = mock(Authentication.class);
        SecurityContentUser userMock = mock(SecurityContentUser.class);
        given(authenticationMock.getPrincipal()).willReturn(userMock);

        // when
        redirectAuthSuccessHandler.onAuthenticationSuccess(requestMock, responseMock, authenticationMock);

        // then
        verify(eventBusMock, times(1)).post(any(SignInSuccessEvent.class));
    }
}