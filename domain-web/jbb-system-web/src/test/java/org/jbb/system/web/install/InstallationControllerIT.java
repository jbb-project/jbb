/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.install;

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

import com.google.common.collect.Sets;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import org.jbb.system.api.install.InstallationDataException;
import org.jbb.system.api.install.InstallationService;
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

public class InstallationControllerIT extends BaseIT {

    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private InstallationService installationServiceMock;


    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .apply(SecurityMockMvcConfigurers.springSecurity()).build();
        Mockito.reset(installationServiceMock);
    }

    @Test
    public void shouldPutInstallFormToModel_whenGET() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/install"));

        // then
        result.andExpect(status().isOk())
            .andExpect(model().attributeExists("installForm"))
            .andExpect(view().name("install"));
    }

    @Test
    public void shouldInstall_whenPOST() throws Exception {
        // when
        ResultActions result = getPostRequest();

        // then
        result.andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/install"));

        verify(installationServiceMock, times(1)).install(any());
    }

    private ResultActions getPostRequest() throws Exception {
        return mockMvc.perform(post("/install")
            .param("adminUsername", "administrator")
            .param("adminDisplayedName", "Administrator")
            .param("adminEmail", "admin@admin.com")
            .param("adminPassword", "passwd")
            .param("adminPasswordAgain", "passwd")
            .param("boardName", "jBB Board")
            .param("databaseProviderName", "POSTGRESQL")
            .param("postgresqlForm.hostName", "127.0.0.1")
            .param("postgresqlForm.port", "5432")
            .param("postgresqlForm.databaseName", "jbbdb")
            .param("postgresqlForm.username", "admin")
            .param("postgresqlForm.password", "passwd")
        );
    }

    @Test
    public void shouldNotInstall_whenPOST_andDifferentPasswordsProvided() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/install")
            .param("adminPassword", "passwd")
            .param("adminPasswordAgain", "different")
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(view().name("install"));

        verify(installationServiceMock, times(0)).install(any());
    }

    @Test
    public void shouldNotInstall_whenPOST_andValidationFailed() throws Exception {
        // given
        InstallationDataException exceptionMock = mock(InstallationDataException.class);
        ConstraintViolation violationMock = mock(ConstraintViolation.class);
        Path pathMock = mock(Path.class);
        given(pathMock.toString()).willReturn("postgresqlForm.port");
        given(violationMock.getPropertyPath()).willReturn(pathMock);
        given(violationMock.getMessage()).willReturn("violation");
        given(exceptionMock.getConstraintViolations()).willReturn(Sets.newHashSet(violationMock));

        Mockito.doThrow(exceptionMock).when(installationServiceMock).install(any());

        // when
        ResultActions result = getPostRequest();

        // then
        result.andExpect(status().isOk())
            .andExpect(view().name("install"));

        verify(installationServiceMock, times(1)).install(any());

    }
}