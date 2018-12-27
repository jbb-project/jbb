/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.rememberme;

import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.event.SignInSuccessEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventAwareTokenBasedRememberMeServicesTest {

    @Mock
    private JbbEventBus eventBusMock;

    @Mock
    private PersistentTokenRepository tokenRepository;

    private EventAwareTokenBasedRememberMeServices eventAwareTokenBasedRememberMeServices;

    @Before
    public void injectMock() {
        eventAwareTokenBasedRememberMeServices = new EventAwareTokenBasedRememberMeServices("key",
                mock(UserDetailsService.class), tokenRepository, eventBusMock);
    }

    @Test
    public void shouldSentSignInSuccessEvent_whenAutologinHappened() {
        // given
        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        given(requestMock.getContextPath()).willReturn("");
        given(requestMock.getSession()).willReturn(mock(HttpSession.class));
        given(tokenRepository.getTokenForSeries(any())).willReturn(new PersistentRememberMeToken("a", "a", "val", new Date()));

        // when
        eventAwareTokenBasedRememberMeServices.processAutoLoginCookie(new String[]{"a", "val"}, requestMock, mock(HttpServletResponse.class));

        // then
        verify(eventBusMock, times(1)).post(any(SignInSuccessEvent.class));
    }
}