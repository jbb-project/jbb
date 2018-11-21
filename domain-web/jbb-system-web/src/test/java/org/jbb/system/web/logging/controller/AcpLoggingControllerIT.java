/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.controller;

import static org.jbb.system.web.logging.controller.CommonLoggingConfiguration.correctAppLogger;
import static org.jbb.system.web.logging.controller.CommonLoggingConfiguration.correctConsoleAppender;
import static org.jbb.system.web.logging.controller.CommonLoggingConfiguration.correctFileAppender;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Lists;
import org.jbb.system.api.logging.LoggingSettingsService;
import org.jbb.system.api.logging.model.LoggingConfiguration;
import org.jbb.system.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class AcpLoggingControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private LoggingSettingsService loggingSettingsServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void shouldSaveNewLoggingConfiguration_whenPOST() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging")
                .param("stackTraceVisibilityLevel", "Administrators"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/logging"))
                .andExpect(flash().attribute("loggingSettingsFormSaved", true));
    }

    private void prepareLoggingConfigurationMocks() {
        LoggingConfiguration loggingConfigurationMock = mock(LoggingConfiguration.class);
        given(loggingSettingsServiceMock.getLoggingConfiguration()).willReturn(loggingConfigurationMock);
        given(loggingConfigurationMock.getConsoleAppenders()).willReturn(Lists.newArrayList(correctConsoleAppender()));
        given(loggingConfigurationMock.getFileAppenders()).willReturn(Lists.newArrayList(correctFileAppender()));
        given(loggingConfigurationMock.getLoggers()).willReturn(Lists.newArrayList(correctAppLogger()));
    }

}