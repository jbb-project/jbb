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

import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.service.MemberService;
import org.jbb.security.api.service.MemberLockoutService;
import org.jbb.security.event.SignInFailedEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationServiceException;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SignInUrlAuthFailureHandlerTest {
    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private MemberService memberServiceMock;

    @Mock
    private MemberLockoutService memberLockoutService;

    @Mock
    private JbbEventBus eventBusMock;

    @InjectMocks
    private SignInUrlAuthFailureHandler signInUrlAuthFailureHandler;

    @Test
    public void shouldEmitSignInFailedEvent_whenUserSigninFailure() throws Exception {
        // given
        given(requestMock.getSession()).willReturn(mock(HttpSession.class));
        given(requestMock.getParameter(eq("username"))).willReturn("omc");
        given(memberServiceMock.getMemberWithUsername(any())).willReturn(Optional.of(mock(Member.class)));
        // when
        signInUrlAuthFailureHandler.onAuthenticationFailure(requestMock, responseMock, new AuthenticationServiceException(""));

        // then
        verify(eventBusMock, times(1)).post(any(SignInFailedEvent.class));
    }

}