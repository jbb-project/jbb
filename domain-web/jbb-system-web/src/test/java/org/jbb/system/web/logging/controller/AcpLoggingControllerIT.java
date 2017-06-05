/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.controller;

import com.google.common.collect.Lists;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.lib.test.MockSpringSecurityConfig;
import org.jbb.system.api.stacktrace.StackTraceVisibilityLevel;
import org.jbb.system.api.logging.model.LoggingConfiguration;
import org.jbb.system.api.logging.LoggingSettingsService;
import org.jbb.system.api.stacktrace.StackTraceService;
import org.jbb.system.web.SystemConfigMock;
import org.jbb.system.web.SystemWebConfig;
import org.jbb.system.web.logging.form.LoggingSettingsForm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.system.web.logging.controller.CommonLoggingConfiguration.correctAppLogger;
import static org.jbb.system.web.logging.controller.CommonLoggingConfiguration.correctConsoleAppender;
import static org.jbb.system.web.logging.controller.CommonLoggingConfiguration.correctFileAppender;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, MvcConfig.class, SystemWebConfig.class, PropertiesConfig.class,
        SystemConfigMock.class, MockCommonsConfig.class, MockSpringSecurityConfig.class})
public class AcpLoggingControllerIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private StackTraceService stackTraceServiceMock;

    @Autowired
    private LoggingSettingsService loggingSettingsServiceMock;

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
        prepareLoggingConfigurationMocks();
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
    public void shouldSetNewStackTraceVisibilityLevelToModel_whenPOST() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging")
                .param("stackTraceVisibilityLevel", "Administrators"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/logging"))
                .andExpect(flash().attribute("loggingSettingsFormSaved", true));

        verify(stackTraceServiceMock, times(1)).setStackTraceVisibilityLevel(eq(StackTraceVisibilityLevel.ADMINISTRATORS));
    }

    private void prepareLoggingConfigurationMocks() {
        LoggingConfiguration loggingConfigurationMock = mock(LoggingConfiguration.class);
        given(loggingSettingsServiceMock.getLoggingConfiguration()).willReturn(loggingConfigurationMock);
        given(loggingConfigurationMock.getConsoleAppenders()).willReturn(Lists.newArrayList(correctConsoleAppender()));
        given(loggingConfigurationMock.getFileAppenders()).willReturn(Lists.newArrayList(correctFileAppender()));
        given(loggingConfigurationMock.getLoggers()).willReturn(Lists.newArrayList(correctAppLogger()));
    }

}