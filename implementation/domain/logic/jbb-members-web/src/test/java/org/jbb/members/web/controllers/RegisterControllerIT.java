/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.controllers;

import com.google.common.collect.Sets;

import org.jbb.lib.mvc.MvcConfig;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.model.RegistrationRequest;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.web.MembersWebConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, MembersWebConfig.class,
        MembersConfigMock.class, CoreConfigMock.class})
public class RegisterControllerIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private RegistrationService registrationServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldUseRegisterView_whenRegisterUrlInvoked() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/register"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    public void shouldRedirectToSuccessPage_whenPostCorrectDataInRegisterForm() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/register")
                .param("login", "john")
                .param("displayedName", "John")
                .param("email", "john@john.pl"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register/success"));
    }

    @Test
    public void shouldSetMemberLoginInFlashAttributes_whenPostCorrectDataInRegisterForm() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/register")
                .param("login", "john")
                .param("displayedName", "John")
                .param("email", "john@john.pl"));

        // then
        result.andExpect(flash().attribute("newMemberLogin", "john"));
    }

    @Test
    public void shouldStayAtPage_whenPostIncorrectDataInRegisterForm() throws Exception {
        // given
        Path pathMock = Mockito.mock(Path.class);
        when(pathMock.toString()).thenReturn("login.value");

        ConstraintViolation cfMock = Mockito.mock(ConstraintViolation.class);
        when(cfMock.getPropertyPath()).thenReturn(pathMock);
        when(cfMock.getMessage()).thenReturn("message");

        RegistrationException exMock = Mockito.mock(RegistrationException.class);
        when(exMock.getConstraintViolations()).thenReturn(Sets.newHashSet(cfMock));

        doThrow(exMock).when(registrationServiceMock).register(any(RegistrationRequest.class));

        // when
        ResultActions result = mockMvc.perform(post("/register"));

        // then
        result.andExpect(model().attribute("registrationCompleted", false));
    }
}