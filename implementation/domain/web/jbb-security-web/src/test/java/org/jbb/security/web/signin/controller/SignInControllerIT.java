/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin.controller;

import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.test.MockCoreConfig;
import org.jbb.security.event.SignInFailedEvent;
import org.jbb.security.event.SignInSuccessEvent;
import org.jbb.security.web.SecurityConfigMock;
import org.jbb.security.web.SecurityWebConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreConfig.class, MvcConfig.class, SecurityWebConfig.class,
        MockCoreConfig.class, SecurityConfigMock.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class SignInControllerIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JbbEventBus jbbEventBus;

    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService userDetailsServiceMock;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(springSecurityFilterChain).build();
    }

    @Test
    public void shouldUseSigninView_whenSigninUrlInvoked() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/signin"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("signin"));
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
    public void shouldSignIn_whenCorrectCredencials_andSignInSuccessEventSent() throws Exception {
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
    public void shouldNotSignIn_whenIncorrectCredencials_andSignInFailedEventSent() throws Exception {
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
}