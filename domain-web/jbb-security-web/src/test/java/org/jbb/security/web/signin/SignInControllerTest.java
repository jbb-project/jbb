/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin;

import org.jbb.lib.mvc.flow.RedirectManager;
import org.jbb.security.api.signin.SignInSettings;
import org.jbb.security.api.signin.SignInSettingsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class SignInControllerTest {
    @Mock
    private RedirectManager redirectManagerMock;

    @Mock
    private SignInSettingsService signInSettingsServiceMock;

    @InjectMocks
    private SignInController signInController;

    @Test
    public void shouldGoToPreviousPage_whenAuthenticatedUserOpenedSignInPage() {
        // given
        Model modelMock = mock(Model.class);
        HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        Authentication authenticationMock = mock(Authentication.class); // user authenticated
        given(authenticationMock.isAuthenticated()).willReturn(true);
        HttpSession httpSessionMock = mock(HttpSession.class);

        // when
        signInController.signIn(null, modelMock, httpServletRequestMock, authenticationMock, httpSessionMock);

        // then
        verify(redirectManagerMock, times(1)).goToPreviousPageSafe(eq(httpServletRequestMock));
    }

    @Test
    public void shouldGoToSignInView_whenAnonUserOpenedSignInPage() {
        // given
        Model modelMock = mock(Model.class);
        HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        Authentication authenticationMock = mock(Authentication.class); // user not authenticated
        given(authenticationMock.isAuthenticated()).willReturn(false);
        HttpSession httpSessionMock = mock(HttpSession.class);

        given(signInSettingsServiceMock.getSignInSettings()).willReturn(validSignInSettings());

        // when
        String viewName = signInController.signIn(null, modelMock, httpServletRequestMock, authenticationMock, httpSessionMock);

        // then
        assertThat(viewName).isEqualTo("signin");
    }

    @Test
    public void shouldAddErrorMessageAboutInvalidUsernameOrPassword_whenErrorFlagPassed_andBadCredentialsExceptionHappened() {
        // given
        Model modelMock = mock(Model.class);
        HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        HttpSession httpSessionMock = mock(HttpSession.class);
        given(httpServletRequestMock.getSession()).willReturn(httpSessionMock);
        BadCredentialsException badCredentialsException = new BadCredentialsException("bad credentials");
        given(httpSessionMock.getAttribute(eq("SPRING_SECURITY_LAST_EXCEPTION"))).willReturn(badCredentialsException);

        Authentication authenticationMock = mock(Authentication.class); // user not authenticated
        given(authenticationMock.isAuthenticated()).willReturn(false);

        given(signInSettingsServiceMock.getSignInSettings()).willReturn(validSignInSettings());

        // when
        String viewName = signInController.signIn(false, modelMock, httpServletRequestMock, authenticationMock, httpSessionMock);

        // then
        verify(modelMock, times(1)).addAttribute(eq("loginError"), eq("Invalid username or password"));
    }

    @Test
    public void shouldAddGenericErrorMessage_whenErrorFlagPassed_andDifferentThanBadCredentialsExceptionHappened() {
        // given
        Model modelMock = mock(Model.class);
        HttpServletRequest httpServletRequestMock = mock(HttpServletRequest.class);
        HttpSession httpSessionMock = mock(HttpSession.class);
        given(httpServletRequestMock.getSession()).willReturn(httpSessionMock);
        given(httpSessionMock.getAttribute(eq("SPRING_SECURITY_LAST_EXCEPTION"))).willReturn(mock(IllegalStateException.class));


        Authentication authenticationMock = mock(Authentication.class); // user not authenticated
        given(authenticationMock.isAuthenticated()).willReturn(false);

        given(signInSettingsServiceMock.getSignInSettings()).willReturn(validSignInSettings());

        // when
        String viewName = signInController.signIn(false, modelMock, httpServletRequestMock, authenticationMock, httpSessionMock);

        // then
        verify(modelMock, times(1)).addAttribute(eq("loginError"), eq("Some error occurred. Please contact administrator"));
    }

    private SignInSettings validSignInSettings() {
        return SignInSettings.builder()
                .basicAuthEnabled(true)
                .rememberMeTokenValidityDays(14L)
                .build();
    }

}