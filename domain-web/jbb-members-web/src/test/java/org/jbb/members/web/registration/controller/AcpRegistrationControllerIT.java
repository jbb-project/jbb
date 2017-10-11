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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.google.common.collect.Sets;
import java.util.Properties;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.mvc.WildcardReloadableResourceBundleMessageSource;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.web.MembersConfigMock;
import org.jbb.members.web.MembersWebConfig;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordRequirements;
import org.jbb.security.api.password.PasswordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, MvcConfig.class, MembersWebConfig.class,
    MembersConfigMock.class, MockCommonsConfig.class})
public class AcpRegistrationControllerIT {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    private RegistrationService registrationServiceMock;

    @Autowired
    private PasswordService passwordServiceMock;

    @Autowired
    private WildcardReloadableResourceBundleMessageSource messageSource;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldPutRegistrationSettingsForm_whenGet() throws Exception {
        // given
        PasswordRequirements passwordRequirements = new PasswordRequirements();
        given(passwordServiceMock.currentRequirements()).willReturn(passwordRequirements);

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/registration"));

        // then
        result.andExpect(status().isOk())
            .andExpect(view().name("acp/general/registration"))
            .andExpect(model().attributeExists("registrationSettingsForm"));
    }

    @Test
    public void shouldNotSetFlag_whenPostAndBindingError() throws Exception {

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/registration")
            .param("emailDuplicationAllowed", "true")
            .param("minPassLength", "6")
            .param("maxPassLength", "16aa")
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(view().name("acp/general/registration"))
            .andExpect(model().attributeDoesNotExist("registrationSettingsFormSaved"));
    }

    @Test
    public void shouldNotSetFlag_whenPostAndServiceError() throws Exception {
        // given
        Properties prop = new Properties();
        prop.setProperty("x", "message");
        messageSource.setCommonMessages(prop);
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path propertyPath = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(propertyPath);
        when(propertyPath.toString()).thenReturn("minimumLength");
        doThrow(new PasswordException(Sets.newHashSet(violation)))
            .when(passwordServiceMock).updateRequirements(any());

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/registration")
            .param("emailDuplicationAllowed", "true")
            .param("minPassLength", "6")
            .param("maxPassLength", "16")
        );

        // then
        result.andExpect(status().isOk())
            .andExpect(view().name("acp/general/registration"))
            .andExpect(model().attributeDoesNotExist("registrationSettingsFormSaved"));
    }


    @Test
    public void shouldSetFlag_whenPostSucceed() throws Exception {

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/registration")
            .param("emailDuplicationAllowed", "true")
            .param("minPassLength", "6")
            .param("maxPassLength", "16")
        );

        // then
        result.andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/acp/general/registration"))
            .andExpect(flash().attribute("registrationSettingsFormSaved", true));
    }


}