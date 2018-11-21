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

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.MemberLock;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.web.BaseIT;
import org.jbb.security.web.MemberImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AcpMemberLocksControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private MemberLockoutService memberLockoutServiceMock;

    @Autowired
    private MemberService memberServiceMock;

    @Autowired
    private UserDetailsService userDetailsServiceMock;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();

        Mockito.reset(memberLockoutServiceMock);
        Mockito.reset(memberServiceMock);

        UserDetails userDetails = mock(UserDetails.class);
        Collection<? extends GrantedAuthority> administrator = Lists.newArrayList(new SimpleGrantedAuthority("ROLE_ADMINISTRATOR"));

        Mockito.doReturn(administrator).when(userDetails).getAuthorities();
        Mockito.doReturn(userDetails).when(userDetailsServiceMock).loadUserByUsername(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldUseMemberLocksView_whenGET() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/acp/members/locks"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/members/locks"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldSetFlag_whenPOST_ok() throws Exception {
        // given
        given(memberLockoutServiceMock.getLocksWithCriteria(any())).willReturn(examplePageOfLocks());
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/locks")
                .with(csrf())
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/members/locks"))
                .andExpect(flash().attributeExists("resultPage"));

        verify(memberLockoutServiceMock, times(1)).getLocksWithCriteria(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldSetFlag_whenPOST_withExistingDisplayedName() throws Exception {
        // given
        given(memberLockoutServiceMock.getLocksWithCriteria(any())).willReturn(examplePageOfLocks());
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        given(memberServiceMock.getMemberWithDisplayedName(eq(DisplayedName.of("john")))).willReturn(Optional.of(exampleMember()));

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/locks")
                .param("displayedName", "john")
                .with(csrf())
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/members/locks"))
                .andExpect(flash().attributeExists("resultPage"));

        verify(memberLockoutServiceMock, times(1)).getLocksWithCriteria(any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldNotSetFlag_whenPOST_withNotExistingDisplayedName() throws Exception {
        // given
        given(memberLockoutServiceMock.getLocksWithCriteria(any())).willReturn(examplePageOfLocks());
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        given(memberServiceMock.getMemberWithDisplayedName(eq(DisplayedName.of("john")))).willReturn(Optional.empty());

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/locks")
                .param("displayedName", "john")
                .with(csrf())
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/members/locks"))
                .andExpect(model().hasErrors());

        verifyNoMoreInteractions(memberLockoutServiceMock);
    }

    private Page<MemberLock> examplePageOfLocks() {
        return new PageImpl<>(Lists.newArrayList(MemberLock.builder()
                .memberId(123L)
                .active(true)
                .createDateTime(LocalDateTime.now())
                .expirationDateTime(LocalDateTime.now().plusHours(1L))
                .build()), PageRequest.of(0, 1), 1);
    }

    private Member exampleMember() {
        return MemberImpl.builder()
                .id(123L)
                .username(Username.of("john"))
                .displayedName(DisplayedName.of("John"))
                .email(Email.of("john@john.com"))
                .build();
    }

}