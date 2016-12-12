/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin.logic;

import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.event.SignInSuccessEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
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

    @InjectMocks
    private RedirectAuthSuccessHandler redirectAuthSuccessHandler;

    @Test
    public void shouldEmitSignInSuccessEvent_whenUserSignin() throws Exception {
        // given
        given(requestMock.getSession()).willReturn(mock(HttpSession.class));
        Authentication authenticationMock = mock(Authentication.class);
        SecurityContentUser userMock = mock(SecurityContentUser.class);
        given(authenticationMock.getPrincipal()).willReturn(userMock);

        // when
        redirectAuthSuccessHandler.onAuthenticationSuccess(requestMock, responseMock, authenticationMock);

        // then
        verify(eventBusMock, times(1)).post(any(SignInSuccessEvent.class));
    }
}