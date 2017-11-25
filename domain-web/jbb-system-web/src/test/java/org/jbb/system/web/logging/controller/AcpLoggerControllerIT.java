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


import static org.jbb.system.web.logging.controller.CommonLoggingConfiguration.correctAppLogger;
import static org.jbb.system.web.logging.controller.CommonLoggingConfiguration.correctConsoleAppender;
import static org.jbb.system.web.logging.controller.CommonLoggingConfiguration.correctFileAppender;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.jbb.system.api.logging.LoggingConfigurationException;
import org.jbb.system.api.logging.LoggingSettingsService;
import org.jbb.system.api.logging.model.AppLogger;
import org.jbb.system.api.logging.model.LoggingConfiguration;
import org.jbb.system.web.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


public class AcpLoggerControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private LoggingSettingsService loggingSettingsServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(loggingSettingsServiceMock);
    }

    @Test
    public void shouldPutLoggerForm_andNewLoggerStateFlagTrue_whenGET_andActionNew() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/logging/logger")
                .param("act", "new"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/logger"))
                .andExpect(model().attribute("newLoggerState", true))
                .andExpect(model().attributeExists("loggerForm"));
    }

    @Test
    public void shouldPutLoggerForm_andNewLoggerStateFlagFalse_whenGET_andActionEdit() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/logging/logger")
                .param("act", "edit")
                .param("id", "org.jbb.testing")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/logger"))
                .andExpect(model().attribute("newLoggerState", false))
                .andExpect(model().attributeExists("loggerForm"));
    }

    @Test
    public void shouldInvokeDeleteMethodInService_whenGET_andActionDel() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/logging/logger")
                .param("act", "del")
                .param("id", "org.jbb.testing")
        );

        // then
        result.andExpect(status().is3xxRedirection());
        verify(loggingSettingsServiceMock, times(1)).deleteLogger(any(AppLogger.class));
    }

    @Test
    public void shouldSetFlag_whenPOST_andServiceHandledLoggerCreation() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging/logger")
                .param("name", "org.jbb")
                .param("level", "info")
                .param("addingMode", "true")
                .param("appenders[default]", "true")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/logging/logger"));

        verify(loggingSettingsServiceMock, times(1)).addLogger(any(AppLogger.class));
    }

    @Test
    public void shouldNotSetFlag_whenPOST_andServiceHandledLoggerEdition() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging/logger")
                .param("name", "org.jbb")
                .param("level", "info")
                .param("addingMode", "false")
                .param("appenders[default]", "true")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/logging/logger"));

        verify(loggingSettingsServiceMock, times(1)).updateLogger(any(AppLogger.class));
    }

    @Test
    public void shouldReturnTheSameView_whenPOST_andSomeExceptionHappenedInService() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        LoggingConfigurationException ex = new LoggingConfigurationException(Sets.newHashSet());

        Mockito.doThrow(ex).when(loggingSettingsServiceMock).updateLogger(any(AppLogger.class));

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging/logger")
                .param("name", "org.jbb")
                .param("level", "info")
                .param("addingMode", "false")
                .param("appenders[default]", "true")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/logger"));
    }

    private void prepareLoggingConfigurationMocks() {
        LoggingConfiguration loggingConfigurationMock = mock(LoggingConfiguration.class);
        given(loggingSettingsServiceMock.getLoggingConfiguration()).willReturn(loggingConfigurationMock);
        given(loggingConfigurationMock.getConsoleAppenders()).willReturn(Lists.newArrayList(correctConsoleAppender()));
        given(loggingConfigurationMock.getFileAppenders()).willReturn(Lists.newArrayList(correctFileAppender()));
        given(loggingConfigurationMock.getLoggers()).willReturn(Lists.newArrayList(correctAppLogger()));
    }
}