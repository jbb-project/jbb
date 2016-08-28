/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.controllers;

import org.jbb.frontend.api.services.BoardNameService;
import org.jbb.frontend.web.FrontendWebConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.test.CoreConfigMocks;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, EventBusConfig.class, FrontendWebConfig.class,
        MvcConfigMock.class, CoreConfigMocks.class})
public class HomePageControllerIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private BoardNameService boardNameServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
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

    @Test
    public void shouldUseNewTitle_whenSettingRequestPerformed() throws Exception {
        // given
        when(boardNameServiceMock.getBoardName()).thenReturn("Board title");

        // when
        ResultActions result = mockMvc.perform(get("/set").param("newBoardName", "New Board title"));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(boardNameServiceMock).setBoardName("New Board title");
    }

    @Test
    public void shouldUseTitleFromPropertyFile_whenGetIndexInvoked() throws Exception {
        // given
        when(boardNameServiceMock.getBoardName()).thenReturn("Board title");

        // when
        ResultActions result = mockMvc.perform(get("/"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("defaultLayout"))
                .andExpect(request().attribute("boardName", "Board title"));

    }

    @Test
    public void shouldUseNewTitle_whenGetIndexInvokedAgain_andPropertyHadBeenChanged() throws Exception {
        // given
        when(boardNameServiceMock.getBoardName()).thenReturn("Board title", "New Board title");

        // when
        mockMvc.perform(get("/"));
        ResultActions result = mockMvc.perform(get("/"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("defaultLayout"))
                .andExpect(request().attribute("boardName", "New Board title"));
    }
}