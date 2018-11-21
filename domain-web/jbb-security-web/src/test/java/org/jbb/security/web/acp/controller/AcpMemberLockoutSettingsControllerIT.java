/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.controller;

import com.google.common.collect.Lists;

import org.assertj.core.util.Sets;
import org.jbb.security.api.lockout.LockoutSettingsService;
import org.jbb.security.api.lockout.MemberLockoutException;
import org.jbb.security.api.lockout.MemberLockoutSettings;
import org.jbb.security.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AcpMemberLockoutSettingsControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private LockoutSettingsService lockoutSettingsServiceMock;

    @Autowired
    private UserDetailsService userDetailsServiceMock;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();

        Mockito.reset(lockoutSettingsServiceMock);

        UserDetails userDetails = mock(UserDetails.class);
        Collection<? extends GrantedAuthority> administrator = Lists.newArrayList(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));

        Mockito.doReturn(administrator).when(userDetails).getAuthorities();
        Mockito.doReturn(userDetails).when(userDetailsServiceMock).loadUserByUsername(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldUseLockoutSettingsView_whenGET() throws Exception {
        // given
        given(lockoutSettingsServiceMock.getLockoutSettings())
            .willReturn(mock(MemberLockoutSettings.class));

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/lockout"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/lockout"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldSetFlag_whenPOST_ok() throws Exception {
        // given
        givenCurrentLockoutSettings();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/lockout")
                .param("lockingEnabled", "true")
                .param("lockoutDurationMinutes", "120")
                .param("failedAttemptsThreshold", "5")
                .param("failedSignInAttemptsExpirationMinutes", "10")
                .with(csrf())
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/lockout"))
                .andExpect(flash().attribute("lockoutSettingsFormSaved", true));

        verify(lockoutSettingsServiceMock, times(1)).setLockoutSettings(any(MemberLockoutSettings.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldNotSetFlag_whenPOST_bindingError() throws Exception {
        // given
        givenCurrentLockoutSettings();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/lockout")
                .param("lockingEnabled", "truexxxxx")
                .param("lockoutDurationMinutes", "120")
                .param("failedAttemptsThreshold", "5")
                .param("failedSignInAttemptsExpirationMinutes", "10")
                .with(csrf())
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/lockout"))
                .andExpect(model().attributeDoesNotExist("lockoutSettingsFormSaved"));

        verifyZeroInteractions(lockoutSettingsServiceMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldNotSetFlag_whenPOST_validationError() throws Exception {
        // given
        givenCurrentLockoutSettings();

        BDDMockito.willThrow(new MemberLockoutException(Sets.newHashSet())).given(lockoutSettingsServiceMock)
                .setLockoutSettings(any());

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/lockout")
                .param("lockingEnabled", "true")
                .param("lockoutDurationMinutes", "120")
                .param("failedAttemptsThreshold", "-5")
                .param("failedSignInAttemptsExpirationMinutes", "10")
                .with(csrf())
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/lockout"))
                .andExpect(model().attributeDoesNotExist("lockoutSettingsFormSaved"));

        verify(lockoutSettingsServiceMock, times(1)).setLockoutSettings(any(MemberLockoutSettings.class));
    }

    private void givenCurrentLockoutSettings() {
        MemberLockoutSettings lockoutSettings = MemberLockoutSettings.builder()
                .lockingEnabled(true)
                .lockoutDurationMinutes(120L)
                .failedAttemptsThreshold(5)
                .failedSignInAttemptsExpirationMinutes(10L)
                .build();

        given(lockoutSettingsServiceMock.getLockoutSettings()).willReturn(lockoutSettings);
    }

}