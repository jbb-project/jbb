/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.controller;

import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.security.web.SecurityWebConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, SecurityWebConfig.class,
        CoreConfigMocks.class, SecurityConfigMock.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class SignInControllerIT {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService userDetailsServiceMock;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
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
    @WithMockUser(username = "any", roles = {})
    public void shouldRedirectToHomePage_whenUserIsAuthenticated() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/signin"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Ignore//TODO
    @Test
    public void shouldSignIn() throws Exception {
        // given
        UserDetails johnDetails = Mockito.mock(UserDetails.class);
        given(johnDetails.getUsername()).willReturn("john");
        given(johnDetails.getPassword()).willReturn("pass1");

        given(userDetailsServiceMock.loadUserByUsername(eq("john"))).willReturn(johnDetails);

        // when
        MvcResult result = mockMvc.perform(post("/signin/auth")
                .requestAttr("login", "john")
                .requestAttr("pswd", "pass2")).andReturn();
        //...

    }
}