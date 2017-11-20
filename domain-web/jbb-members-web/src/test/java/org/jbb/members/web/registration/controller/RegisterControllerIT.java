/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.registration.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Sets;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import org.jbb.members.api.registration.RegistrationException;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.web.BaseIT;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class RegisterControllerIT extends BaseIT {
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
                .param("username", "john")
                .param("displayedName", "John")
                .param("email", "john@john.pl"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register/success"));
    }

    @Ignore//FIXME?
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void shouldSetMemberUsernameInFlashAttributes_whenPostCorrectDataInRegisterForm() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/register")
            .param("username", "john")
            .param("displayedName", "John")
            .param("email", "john@john.pl"));

        // then
        result.andExpect(flash().attribute("newMemberUsername", "john"));
    }

    @Test
    public void shouldStayAtPage_whenPostIncorrectDataInRegisterForm() throws Exception {
        // given
        Path pathMock = Mockito.mock(Path.class);
        when(pathMock.toString()).thenReturn("username.value");

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

    @Test
    public void shouldRedirectFromSuccessPage_whenCallGetDirectly() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/register/success"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }

    @Test
    public void shouldStayAtRegisterView_afterSuccess() throws Exception {
        // when
        ResultActions result = mockMvc.perform(
                get("/register/success").flashAttr("newMemberUsername", "john"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("registrationCompleted", true));
    }
}