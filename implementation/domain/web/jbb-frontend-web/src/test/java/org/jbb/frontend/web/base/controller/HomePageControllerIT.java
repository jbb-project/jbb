/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.controller;

import org.jbb.frontend.web.FrontendConfigMock;
import org.jbb.frontend.web.FrontendWebConfig;
import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.test.MockCoreConfig;
import org.jbb.lib.test.MockSpringSecurityConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreConfig.class, MvcConfig.class, FrontendWebConfig.class,
        FrontendConfigMock.class, MockCoreConfig.class, MockSpringSecurityConfig.class})
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
public class HomePageControllerIT {
    @Autowired
    WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(SecurityMockMvcConfigurers.springSecurity()).build();
    }

    @Test
    public void shouldUseHomeView_whenMainUrlInvoked() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("defaultLayout"))
                .andExpect(model().attribute("contentViewName", "home"));
    }

    @Test
    public void shouldUseFaqView_whenFaqUrlInvoked() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/faq"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("defaultLayout"))
                .andExpect(model().attribute("contentViewName", "faq"));

    }
}