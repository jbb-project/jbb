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

import com.google.common.collect.Sets;

import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.lib.test.SpringSecurityConfigMocks;
import org.jbb.system.api.exception.DatabaseConfigException;
import org.jbb.system.api.model.DatabaseSettings;
import org.jbb.system.api.service.DatabaseSettingsService;
import org.jbb.system.web.SystemConfigMock;
import org.jbb.system.web.SystemWebConfig;
import org.jbb.system.web.base.form.DatabaseSettingsForm;
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

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, SystemWebConfig.class, PropertiesConfig.class,
        SystemConfigMock.class, CoreConfigMocks.class, SpringSecurityConfigMocks.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class AcpDatabaseSettingsControllerIT {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private DatabaseSettingsService databaseSettingsServiceMock;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(databaseSettingsServiceMock);
    }

    @Test
    public void shouldPutCurrentDatabaseSettingsToModel_whenGET() throws Exception {
        // given
        DatabaseSettings databaseSettings = Mockito.mock(DatabaseSettings.class);
        given(databaseSettings.databaseFileName()).willReturn("jbb.db");
        given(databaseSettingsServiceMock.getDatabaseSettings()).willReturn(databaseSettings);

        // when
        ResultActions result = mockMvc.perform(get("/acp/system/database"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/system/database"));

        DatabaseSettingsForm databaseSettingsForm = (DatabaseSettingsForm) result.andReturn()
                .getModelAndView().getModel().get("databaseSettingsForm");

        assertThat(databaseSettingsForm.getDatabaseFileName()).isEqualTo("jbb.db");
    }

    @Test
    public void shouldNotSetFlag_whenPOST_andBindingErrorExist() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/system/database")
                .param("minimumIdleConnections", "blablabla"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/system/database"))
                .andExpect(model().attribute("databaseSettingsFormSaved", false));
    }

    @Test
    public void shouldNotSetFlag_whenPOST_andServiceThrowException() throws Exception {
        // given
        DatabaseConfigException exceptionMock = mock(DatabaseConfigException.class);
        ConstraintViolation violationMock = mock(ConstraintViolation.class);
        Path pathMock = mock(Path.class);
        given(pathMock.toString()).willReturn("databaseFileName");
        given(violationMock.getPropertyPath()).willReturn(pathMock);
        given(violationMock.getMessage()).willReturn("violation");
        given(exceptionMock.getConstraintViolations()).willReturn(Sets.newHashSet(violationMock));

        willThrow(exceptionMock)
                .given(databaseSettingsServiceMock)
                .setDatabaseSettings(any(DatabaseSettings.class));
        // when
        ResultActions result = mockMvc.perform(post("/acp/system/database"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/system/database"))
                .andExpect(model().attribute("databaseSettingsFormSaved", false));
    }

    @Test
    public void shouldSetFlag_whenPOST_ok() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/system/database"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/system/database"))
                .andExpect(flash().attribute("databaseSettingsFormSaved", true));

        verify(databaseSettingsServiceMock, times(1)).setDatabaseSettings(any(DatabaseSettings.class));
    }
}