/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.base.controller;

import com.google.common.collect.Sets;

import org.jbb.board.api.exception.BoardException;
import org.jbb.board.api.model.BoardSettings;
import org.jbb.board.api.service.BoardSettingsService;
import org.jbb.board.web.BoardWebConfig;
import org.jbb.board.web.base.BoardConfigMock;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {MvcConfig.class, BoardWebConfig.class, PropertiesConfig.class,
        BoardConfigMock.class, CoreConfigMocks.class})
public class AcpBoardSettingsControllerIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private BoardSettingsService boardSettingsServiceMock;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldUseGeneralBoardView_whenAcpBoardSettingsUrlInvoked() throws Exception {
        // given
        given(boardSettingsServiceMock.getBoardSettings()).willReturn(mock(BoardSettings.class));

        // when
        ResultActions result = mockMvc.perform(get("/acp/general/board"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/board"));
    }

    @Test
    public void shouldRedirectToPage_andInvokeService_whenPostCorrectDataToForm() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/acp/general/board")
                .param("boardName", "newBoardName")
                .param("dateFormat", "dd/MM/yyyy HH:mm:ss"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/board"));

        verify(boardSettingsServiceMock, times(1)).getBoardSettings();
    }

    @Test
    public void shouldStayInFormPage_whenExceptionFromService() throws Exception {
        // given
        BoardException ex = mock(BoardException.class);
        given(ex.getConstraintViolations()).willReturn(Sets.newHashSet());
        doThrow(ex).when(boardSettingsServiceMock).setBoardSettings(any());

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/board")
                .param("boardName", "")
                .param("dateFormat", "dd/MM/yyyy HH:mm:ss"));

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/board"));

        verify(boardSettingsServiceMock, times(1)).getBoardSettings();
    }

}