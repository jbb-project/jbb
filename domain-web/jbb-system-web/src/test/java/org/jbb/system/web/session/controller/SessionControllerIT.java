/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.session.controller;

import com.google.common.collect.Lists;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.lib.test.MockSpringSecurityConfig;
import org.jbb.system.api.session.MemberSession;
import org.jbb.system.api.session.SessionService;
import org.jbb.system.web.SystemConfigMock;
import org.jbb.system.web.SystemWebConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, MvcConfig.class, SystemWebConfig.class, PropertiesConfig.class,
        SystemConfigMock.class, MockCommonsConfig.class, MockSpringSecurityConfig.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class SessionControllerIT {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private SessionService sessionService;

    @Before
    public void init() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void whenGetAllSessionMethodIsInvokedThenOkStatusShouldBeReturned() throws Exception {

        //given
        Duration oneHourDuration = Duration.ofHours(1);
        List<MemberSession> memberSessionList = createUserSessionList(2);
        when(sessionService.getMaxInactiveSessionInterval()).thenReturn(oneHourDuration);
        when(sessionService.getAllUserSessions()).thenReturn(memberSessionList);

        //when
        ResultActions resultActions = mockMvc.perform(get("/acp/system/sessions"));

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(view().name("acp/system/sessions"));
        resultActions.andExpect(model().attributeExists("userSessions"));
        resultActions.andExpect(model().attributeExists("sessionSettingsForm"));
    }



    @Test
    public void whenSaveNewValueOfMaxInActiveIntervalTimeAttributeMethodIsInvokedWithCorrectFormatOfInputValueThenNoExceptionShouldBeDisplay() throws Exception {

        //given

        //when
        ResultActions resultActions = mockMvc.perform(post("/acp/system/sessions/properties")
                .param("maxInactiveIntervalTime", "7200"));
        //then

        resultActions.andExpect(status().is3xxRedirection());
    }

    @Test
    public void whenSaveNewValueOfMaxInActiveIntervalTimeAttributeMethodIsInvokedWithWrongInputThenExceptionShouldBeDisplay() throws Exception {

        //given

        //when
        ResultActions resultActions_NotNumber = mockMvc.perform(post("/acp/system/sessions/properties")
                .param("maxInactiveIntervalTime", "7200abc"));

        ResultActions resultActions_Fraction = mockMvc.perform(post("/acp/system/sessions/properties")
                .param("maxInactiveIntervalTime", "7200.012"));

        ResultActions resultActions_Empty = mockMvc.perform(post("/acp/system/sessions/properties")
                .param("maxInactiveIntervalTime", ""));

        ResultActions resultActions_WhiteSpace = mockMvc.perform(post("/acp/system/sessions/properties")
                .param("maxInactiveIntervalTime", " "));

        ResultActions resultActions_NegativeValue = mockMvc.perform(post("/acp/system/sessions/properties")
                .param("maxInactiveIntervalTime", "-2"));

        //then
        resultActions_NotNumber.andExpect(status().is3xxRedirection());
        resultActions_NotNumber.andExpect(model().attributeDoesNotExist("savecorrectly"));

        resultActions_Fraction.andExpect(status().is3xxRedirection());
        resultActions_Fraction.andExpect(model().attributeDoesNotExist("savecorrectly"));

        resultActions_Empty.andExpect(status().is3xxRedirection());
        resultActions_Empty.andExpect(model().attributeDoesNotExist("savecorrectly"));

        resultActions_NegativeValue.andExpect(status().is3xxRedirection());
        resultActions_NegativeValue.andExpect(model().attributeDoesNotExist("savecorrectly"));

        resultActions_WhiteSpace.andExpect(status().is3xxRedirection());
        resultActions_WhiteSpace.andExpect(model().attributeDoesNotExist("savecorrectly"));
    }

    @Test
    public void whenSaveNewValueOfMaxInActiveIntervalTimeAttributeMethodIsInvokedWithFractionAsInputThenExceptionShouldBeDisplay() throws Exception {

        //given

        //when
        ResultActions resultActions = mockMvc.perform(post("/acp/system/sessions/properties")
                .param("maxInactiveIntervalTime", "7200.012"));

        //then
        resultActions.andExpect(status().is3xxRedirection());
        resultActions.andExpect(model().attributeDoesNotExist("savecorrectly"));
    }

    @Test
    public void whenRemoveSessionMethodIsInvokedThenNoExceptionIsThrow() throws Exception {

        //given

        //when
        ResultActions resultActions = mockMvc.perform(post("/acp/system/sessions/remove")
                .param("id", "sessionId3"));

        //then
        resultActions.andExpect(status().is3xxRedirection());
    }


    private List<MemberSession> createUserSessionList(int numberOfSessionToCreate) {
        ArrayList<MemberSession> arrayList = Lists.newArrayList();

        for(int i =0; i<numberOfSessionToCreate; i++){
            final int temp = i;
            arrayList.add(new MemberSession() {
                @Override
                public String getSessionId() {
                    return "sessionId"+temp;
                }

                @Override
                public LocalDateTime getCreationTime() {
                    return LocalDateTime.of(2017,05,16,temp,temp);
                }

                @Override
                public LocalDateTime getLastAccessedTime() {
                    return LocalDateTime.of(2017,05,16,temp,temp);
                }

                @Override
                public Duration getUsedTime() {
                    return Duration.ofHours(temp);
                }

                @Override
                public Duration getInactiveTime() {
                    return Duration.ofHours(temp);
                }

                @Override
                public Duration getTimeToLive() {
                    return Duration.ofHours(temp);
                }

                @Override
                public String getUsername() {
                    return "username"+temp;
                }

                @Override
                public String getDisplayedName() {
                    return "displayedName"+temp;
                }
            });
        }
        return arrayList;
    }


}
