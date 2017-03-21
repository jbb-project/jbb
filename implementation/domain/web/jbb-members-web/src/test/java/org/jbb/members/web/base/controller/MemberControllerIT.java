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


import com.google.common.collect.Lists;

import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.core.vo.Email;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.MemberRegistrationAware;
import org.jbb.members.api.data.RegistrationMetaData;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.web.MembersConfigMock;
import org.jbb.members.web.MembersWebConfig;
import org.jbb.members.web.base.data.MemberBrowserRow;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreConfig.class, MvcConfig.class, MembersWebConfig.class,
        MembersConfigMock.class, CoreConfigMocks.class})
public class MemberControllerIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private MemberService memberServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldUseMemberBrowserView_whenMemberUrlInvoked() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/members"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("member_browser"));
    }

    @Test
    public void shouldMapServiceResultToMemberBrowserRow() throws Exception {
        // given
        LocalDateTime dateTime = LocalDateTime.now();
        RegistrationMetaData regMetaDataMock = Mockito.mock(RegistrationMetaData.class);

        MemberRegistrationAware memberRegAwareMock = Mockito.mock(MemberRegistrationAware.class);
        when(memberRegAwareMock.getEmail()).thenReturn(Email.builder().value("foo@bar.com").build());
        when(memberRegAwareMock.getDisplayedName()).thenReturn(DisplayedName.builder().value("John").build());
        when(regMetaDataMock.getJoinDateTime()).thenReturn(dateTime);
        when(memberRegAwareMock.getRegistrationMetaData()).thenReturn(regMetaDataMock);

        when(memberServiceMock.getAllMembersSortedByRegistrationDate())
                .thenReturn(Lists.newArrayList(memberRegAwareMock));

        // when
        ResultActions result = mockMvc.perform(get("/members"));

        // then
        result.andExpect(model().attribute("memberRows",
                Lists.newArrayList(new MemberBrowserRow(
                        memberRegAwareMock.getEmail(),
                        memberRegAwareMock.getDisplayedName(),
                        regMetaDataMock.getJoinDateTime()
                ))));
    }
}
