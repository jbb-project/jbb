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

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.signin.SignInSettings;
import org.jbb.security.api.signin.SignInSettingsService;
import org.jbb.security.event.SignInFailedEvent;
import org.jbb.security.event.SignInSuccessEvent;
import org.jbb.security.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import javax.servlet.Filter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class SignInControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JbbEventBus jbbEventBus;

    @Autowired
    private UserDetailsService userDetailsServiceMock;

    @Autowired
    private SignInSettingsService signInSettingsServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(springSecurityFilterChain).build();
    }

    @Test
    public void shouldUseSignInView_whenSignInUrlInvoked() throws Exception {
        // given
        given(signInSettingsServiceMock.getSignInSettings()).willReturn(validSignInSettings());

        // when
        ResultActions result = mockMvc.perform(get("/signin"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("signin"));
    }

    @Test
    public void shouldPutRememberMeTokenValidityDaysToModel_whenRememberMeIsEnabled() throws Exception {
        // given
        SignInSettings signInSettings = validSignInSettings();
        signInSettings.setRememberMeTokenValidityDays(10L);

        given(signInSettingsServiceMock.getSignInSettings()).willReturn(signInSettings);

        // when
        ResultActions result = mockMvc.perform(get("/signin"));

        // then
        result.andExpect(status().isOk())
                .andExpect(model().attribute("rememberMeTokenValidityDays", 10L))
                .andExpect(model().attribute("rememberMeEnabled", true));
    }

    @Test
    public void shouldPutRememberMeTokenValidityDaysToModel_whenRememberMeIsDisabled() throws Exception {
        // given
        SignInSettings signInSettings = validSignInSettings();
        signInSettings.setRememberMeTokenValidityDays(0L);

        given(signInSettingsServiceMock.getSignInSettings()).willReturn(signInSettings);

        // when
        ResultActions result = mockMvc.perform(get("/signin"));

        // then
        result.andExpect(status().isOk())
                .andExpect(model().attribute("rememberMeTokenValidityDays", 0L))
                .andExpect(model().attribute("rememberMeEnabled", false));
    }

    @Test
    public void shouldRedirectToHomePage_whenUserIsAuthenticated() throws Exception {
        // given
        SecurityContentUser securityContentUser = getSecurityContentUser("any", "any");
        given(userDetailsServiceMock.loadUserByUsername(any())).willReturn(securityContentUser);

        // when
        ResultActions result = mockMvc.perform(get("/signin").with(user(securityContentUser)));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void shouldSignIn_whenCorrectCredentials_andSignInSuccessEventSent() throws Exception {
        // given
        String username = "john";
        String pass = "pass1";

        given(userDetailsServiceMock.loadUserByUsername(eq(username))).willReturn(getSecurityContentUser(username, pass));

        // when
        mockMvc.perform(formLogin("/signin/auth")
                .user("username", username)
                .password("pswd", pass))
                // then
                .andExpect(authenticated().withUsername(username));

        // then
        verify(jbbEventBus, times(1)).post(isA(SignInSuccessEvent.class));
    }

    @Test
    public void shouldNotSignIn_whenIncorrectCredentials_andSignInFailedEventSent() throws Exception {
        // given
        String username = "john";
        String pass = "pass1";

        given(userDetailsServiceMock.loadUserByUsername(eq(username))).willReturn(getSecurityContentUser(username, pass));

        // when
        mockMvc.perform(formLogin("/signin/auth")
                .user("username", username)
                .password("pswd", "WRONGPASSWORD"))
                // then
                .andExpect(unauthenticated());

        // then
        verify(jbbEventBus, times(1)).post(isA(SignInFailedEvent.class));
    }

    private SecurityContentUser getSecurityContentUser(String username, String password) {
        return new SecurityContentUser(new User(
                username,
                passwordEncoder.encode(password),
                Collections.emptyList()),
                username,
                Long.valueOf(username.hashCode())
        );
    }

    private SignInSettings validSignInSettings() {
        return SignInSettings.builder()
                .basicAuthEnabled(true)
                .rememberMeTokenValidityDays(14L)
                .build();
    }
}