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
import com.google.common.collect.Sets;

import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.lib.test.SpringSecurityConfigMocks;
import org.jbb.system.api.exception.LoggingConfigurationException;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.jbb.system.api.service.LoggingSettingsService;
import org.jbb.system.web.SystemConfigMock;
import org.jbb.system.web.SystemWebConfig;
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

import java.util.Optional;

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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreConfig.class, MvcConfig.class, SystemWebConfig.class, PropertiesConfig.class,
        SystemConfigMock.class, CoreConfigMocks.class, SpringSecurityConfigMocks.class})
public class AcpAppenderControllerIT {
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
    public void shouldPutConsoleAppenderForm_andNewLoggerStateFlagTrue_whenGET_andActionNewconsole() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/logging/append")
                .param("act", "newconsole"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/appender-console"))
                .andExpect(model().attribute("newAppenderState", true))
                .andExpect(model().attributeExists("appenderForm"));
    }

    @Test
    public void shouldPutFileAppenderForm_andNewLoggerStateFlagTrue_whenGET_andActionNewfile() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/logging/append")
                .param("act", "newfile"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/appender-file"))
                .andExpect(model().attribute("newAppenderState", true))
                .andExpect(model().attributeExists("appenderForm"));
    }

    @Test
    public void shouldPutConsoleAppenderForm_andNewLoggerStateFlagFalse_whenGET_andActionEdit() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        given(loggingSettingsServiceMock.getAppender(any())).willReturn(Optional.of(correctConsoleAppender()));

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/logging/append")
                .param("act", "edit")
                .param("id", "consoleAppender")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/appender-console"))
                .andExpect(model().attribute("newAppenderState", false))
                .andExpect(model().attributeExists("appenderForm"));
    }

    @Test
    public void shouldPutFileAppenderForm_andNewLoggerStateFlagFalse_whenGET_andActionEdit() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        given(loggingSettingsServiceMock.getAppender(any())).willReturn(Optional.of(correctFileAppender()));

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/logging/append")
                .param("act", "edit")
                .param("id", "fileAppender")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/appender-file"))
                .andExpect(model().attribute("newAppenderState", false))
                .andExpect(model().attributeExists("appenderForm"));
    }

    @Test
    public void shouldInvokeDeleteMethodInService_whenGET_andActionDel() throws Exception {
        // given
        prepareLoggingConfigurationMocks();
        given(loggingSettingsServiceMock.getAppender(any())).willReturn(Optional.of(correctFileAppender()));

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/logging/append")
                .param("act", "del")
                .param("id", "consoleAppender")
        );

        // then
        result.andExpect(status().is3xxRedirection());
        verify(loggingSettingsServiceMock, times(1)).deleteAppender(any(LogAppender.class));
    }

    @Test
    public void shouldSetFlag_whenPOST_andServiceHandledConsoleAppenderCreation() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging/append/console")
                .param("name", "consoleAppender")
                .param("target", "System.out")
                .param("filter", "None")
                .param("addingMode", "true")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/logging/append"));

        verify(loggingSettingsServiceMock, times(1)).addAppender(any(LogAppender.class));
    }

    @Test
    public void shouldNotSetFlag_whenPOST_andServiceHandledConsoleLoggerEdition() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging/append/console")
                .param("name", "consoleAppender")
                .param("target", "System.out")
                .param("filter", "None")
                .param("addingMode", "false")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/logging/append"));

        verify(loggingSettingsServiceMock, times(1)).updateAppender(any(LogAppender.class));
    }

    @Test
    public void shouldReturnTheSameView_whenPOST_andSomeExceptionHappenedInServiceDuringConsoleAppenderEdition() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        LoggingConfigurationException ex = new LoggingConfigurationException(Sets.newHashSet());

        Mockito.doThrow(ex).when(loggingSettingsServiceMock).updateAppender(any(LogAppender.class));

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging/append/console")
                .param("name", "consoleAppender")
                .param("target", "System.out")
                .param("filter", "None")
                .param("addingMode", "false")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/appender-console"));
    }

    @Test
    public void shouldSetFlag_whenPOST_andServiceHandledFileAppenderCreation() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging/append/file")
                .param("name", "fileAppender")
                .param("currentLogFileName", "jbb.log")
                .param("filter", "None")
                .param("maxFileSize", "100 MB")
                .param("addingMode", "true")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/logging/append"));

        verify(loggingSettingsServiceMock, times(1)).addAppender(any(LogAppender.class));
    }

    @Test
    public void shouldNotSetFlag_whenPOST_andServiceHandledFileLoggerEdition() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging/append/file")
                .param("name", "fileAppender")
                .param("currentLogFileName", "jbb.log")
                .param("filter", "None")
                .param("maxFileSize", "100 MB")
                .param("addingMode", "false")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/logging/append"));

        verify(loggingSettingsServiceMock, times(1)).updateAppender(any(LogAppender.class));
    }

    @Test
    public void shouldReturnTheSameView_whenPOST_andSomeExceptionHappenedInServiceDuringFileAppenderEdition() throws Exception {
        // given
        prepareLoggingConfigurationMocks();

        LoggingConfigurationException ex = new LoggingConfigurationException(Sets.newHashSet());

        Mockito.doThrow(ex).when(loggingSettingsServiceMock).updateAppender(any(LogAppender.class));

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/logging/append/file")
                .param("name", "fileAppender")
                .param("currentLogFileName", "jbb.log")
                .param("filter", "None")
                .param("maxFileSize", "100 MB")
                .param("addingMode", "false")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/appender-file"));
    }


    private void prepareLoggingConfigurationMocks() {
        LoggingConfiguration loggingConfigurationMock = mock(LoggingConfiguration.class);
        given(loggingSettingsServiceMock.getLoggingConfiguration()).willReturn(loggingConfigurationMock);
        given(loggingConfigurationMock.getConsoleAppenders()).willReturn(Lists.newArrayList(correctConsoleAppender()));
        given(loggingConfigurationMock.getFileAppenders()).willReturn(Lists.newArrayList(correctFileAppender()));
        given(loggingConfigurationMock.getLoggers()).willReturn(Lists.newArrayList(correctAppLogger()));
    }

}