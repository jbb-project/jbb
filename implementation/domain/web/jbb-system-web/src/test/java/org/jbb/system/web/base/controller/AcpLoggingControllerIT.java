/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.base.controller;

import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.lib.test.SpringSecurityConfigMocks;
import org.jbb.system.api.data.StackTraceVisibilityLevel;
import org.jbb.system.api.service.StackTraceService;
import org.jbb.system.web.SystemWebConfig;
import org.jbb.system.web.base.SystemConfigMock;
import org.jbb.system.web.base.form.LoggingSettingsForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, SystemWebConfig.class, PropertiesConfig.class,
        SystemConfigMock.class, CoreConfigMocks.class, SpringSecurityConfigMocks.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class AcpLoggingControllerIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private StackTraceService stackTraceServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(stackTraceServiceMock);
    }

    @Test
    public void shouldPutCurrentStackTraceVisibilityLevelToModel_whenGET() throws Exception {
        // given
        given(stackTraceServiceMock.getCurrentStackTraceVisibilityLevel()).willReturn(StackTraceVisibilityLevel.USERS);

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/logging"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/logging"));

        LoggingSettingsForm loggingSettingsForm = (LoggingSettingsForm) result.andReturn()
                .getModelAndView().getModel().get("loggingSettingsForm");

        assertThat(loggingSettingsForm.getStackTraceVisibilityLevel()).isEqualTo("Users");
    }

    @Test
    public void shouldSetNewStackTraceVisibilityLevelToModel_whenPUT() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging")
                .param("stackTraceVisibilityLevel", "Administrators"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/logging"))
                .andExpect(model().attribute("loggingSettingsFormSaved", true));

        verify(stackTraceServiceMock, times(1)).setStackTraceVisibilityLevel(eq(StackTraceVisibilityLevel.ADMINISTRATORS));
    }
}