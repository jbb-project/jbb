/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.database.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Sets;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.lib.test.MockSpringSecurityConfig;
import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseConfigException;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.DatabaseSettingsService;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.jbb.system.web.SystemConfigMock;
import org.jbb.system.web.SystemWebConfig;
import org.jbb.system.web.database.form.DatabaseSettingsForm;
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

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, MvcConfig.class, SystemWebConfig.class, PropertiesConfig.class,
        SystemConfigMock.class, MockCommonsConfig.class, MockSpringSecurityConfig.class})
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
        CommonDatabaseSettings commonDatabaseSettings = Mockito.mock(CommonDatabaseSettings.class);
        given(databaseSettings.getCommonSettings()).willReturn(commonDatabaseSettings);
        H2ManagedServerSettings h2ManagedServerSettings = Mockito
            .mock(H2ManagedServerSettings.class);
        given(databaseSettings.getH2ManagedServerSettings()).willReturn(h2ManagedServerSettings);
        given(h2ManagedServerSettings.getDatabaseFileName()).willReturn("jbb.db");
        given(databaseSettingsServiceMock.getDatabaseSettings()).willReturn(databaseSettings);
        given(databaseSettings.getCurrentDatabaseProvider())
            .willReturn(DatabaseProvider.H2_MANAGED_SERVER);

        // when
        ResultActions result = mockMvc.perform(get("/acp/system/database"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/system/database"));

        DatabaseSettingsForm databaseSettingsForm = (DatabaseSettingsForm) result.andReturn()
                .getModelAndView().getModel().get("databaseSettingsForm");

        assertThat(databaseSettingsForm.getH2ManagedServerDatabaseFileName()).isEqualTo("jbb.db");
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
        given(pathMock.toString()).willReturn("h2ManagedServerDatabaseFileName");
        given(violationMock.getPropertyPath()).willReturn(pathMock);
        given(violationMock.getMessage()).willReturn("violation");
        given(exceptionMock.getConstraintViolations()).willReturn(Sets.newHashSet(violationMock));

        willThrow(exceptionMock)
                .given(databaseSettingsServiceMock)
                .setDatabaseSettings(any(DatabaseSettings.class));
        // when
        ResultActions result = mockMvc.perform(post("/acp/system/database")
            .param("currentDatabaseProviderName", "H2_IN_MEMORY")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/system/database"))
                .andExpect(model().attribute("databaseSettingsFormSaved", false));
    }

    @Test
    public void shouldSetFlag_whenPOST_ok() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/system/database")
            .param("currentDatabaseProviderName", "H2_IN_MEMORY")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/system/database"))
                .andExpect(flash().attribute("databaseSettingsFormSaved", true));

        verify(databaseSettingsServiceMock, times(1)).setDatabaseSettings(any(DatabaseSettings.class));
    }
}