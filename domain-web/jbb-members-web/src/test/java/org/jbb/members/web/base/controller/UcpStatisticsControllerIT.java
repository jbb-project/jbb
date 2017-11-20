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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class UcpStatisticsControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private RegistrationService registrationServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "any", roles = {})
    public void shouldSetOverviewStatisticsViewName_andPutJoinTimeToModel() throws Exception {
        // given
        RegistrationMetaData registrationMetaDataMock = mock(RegistrationMetaData.class);
        given(registrationServiceMock.getRegistrationMetaData(any())).willReturn(registrationMetaDataMock);
        LocalDateTime joinTime = LocalDateTime.of(2016, 9, 28, 23, 58, 11);
        given(registrationMetaDataMock.getJoinDateTime()).willReturn(joinTime);

        // when
        ResultActions result = mockMvc.perform(get("/ucp/overview/statistics"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("ucp/overview/statistics"))
                .andExpect(model().attribute("joinTime", joinTime));
    }
}