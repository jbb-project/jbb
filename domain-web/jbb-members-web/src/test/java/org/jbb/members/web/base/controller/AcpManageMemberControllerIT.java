/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Lists;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.api.base.MemberSearchJoinDateFormatException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class AcpManageMemberControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private MemberService memberServiceMock;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void shouldSetView_whenGET() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/acp/members/manage"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/members/manage"));
    }

    @Test
    public void shouldSetFlag_whenCorrectForm_whenPOST() throws Exception {
        // given
        memberMockPrepare();

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/manage")
            .param("sortByField", "email")
            .param("sortDirection", "ASC"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/members/manage"))
                .andExpect(flash().attribute("memberSearchFormSent", true));
    }

    @Test
    public void shouldNotSetFlag_whenMemberSearchJoinDateFormatException_whenPOST() throws Exception {
        // given
        given(memberServiceMock.getAllMembersWithCriteria(any(MemberSearchCriteria.class)))
            .willThrow(MemberSearchJoinDateFormatException.class);

        // when
        ResultActions result = mockMvc.perform(post("/acp/members/manage")
            .param("sortByField", "email")
            .param("sortDirection", "ASC"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/members/manage"))
                .andExpect(model().attribute("memberSearchFormSent", false));
    }

    private void memberMockPrepare() {
        MemberRegistrationAware memberMock = mock(MemberRegistrationAware.class);
        PageImpl<MemberRegistrationAware> memberPage = new PageImpl<>(
            Lists.newArrayList(memberMock), new PageRequest(0, 1, Direction.ASC, "email"), 1);
        given(memberServiceMock.getAllMembersWithCriteria(any(MemberSearchCriteria.class)))
            .willReturn(memberPage);

        Username username = Username.builder().value("john").build();
        DisplayedName displayedName = DisplayedName.builder().value("John").build();
        Email email = Email.builder().value("john@john.com").build();
        Long memberId = 100L;

        given(memberMock.getUsername()).willReturn(username);
        given(memberMock.getDisplayedName()).willReturn(displayedName);
        given(memberMock.getEmail()).willReturn(email);
        given(memberMock.getId()).willReturn(memberId);

        given(memberMock.getRegistrationMetaData()).willReturn(mock(RegistrationMetaData.class));

    }

}