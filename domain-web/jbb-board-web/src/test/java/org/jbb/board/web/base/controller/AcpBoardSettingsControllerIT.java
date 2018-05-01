/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.web.base.controller;

import com.google.common.collect.Sets;

import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.board.web.BaseIT;
import org.jbb.frontend.api.format.FormatSettings;
import org.jbb.frontend.api.format.FormatSettingsService;
import org.jbb.lib.mvc.WildcardReloadableResourceBundleMessageSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Properties;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class AcpBoardSettingsControllerIT extends BaseIT {
    @Autowired
    WebApplicationContext wac;

    @Autowired
    private Validator validatorMock;

    @Autowired
    private BoardSettingsService boardSettingsServiceMock;

    @Autowired
    private FormatSettingsService formatSettingsServiceMock;

    @Autowired
    private WildcardReloadableResourceBundleMessageSource messageSource;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldUseGeneralBoardView_whenAcpBoardSettingsUrlInvoked() throws Exception {
        // given
        given(boardSettingsServiceMock.getBoardSettings()).willReturn(mock(BoardSettings.class));
        given(formatSettingsServiceMock.getFormatSettings()).willReturn(mock(FormatSettings.class));

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
                .param("dateFormat", "dd/MM/yyyy HH:mm:ss")
                .param("durationFormat", "HH:mm:ss")
        );

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/acp/general/board"));

        verify(boardSettingsServiceMock, times(1)).getBoardSettings();
        verify(formatSettingsServiceMock, times(1)).getFormatSettings();
    }

    @Test
    public void shouldStayInFormPage_whenBoardSettingsValidationFailed() throws Exception {
        // given
        Properties prop = new Properties();
        prop.setProperty("BM", "message");
        messageSource.setCommonMessages(prop);

        ConstraintViolation violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        given(path.toString()).willReturn("boardName");
        given(violation.getPropertyPath()).willReturn(path);
        given(validatorMock.validate(any())).willReturn(
                Sets.newHashSet(violation),
                Sets.newHashSet()
        );

        // when
        ResultActions result = mockMvc.perform(post("/acp/general/board")
                .param("boardName", "")
                .param("dateFormat", "dd/MM/yyyy HH:mm:ss")
                .param("durationFormat", "HH:mm:ss")
        );

        // then
        result.andExpect(status().isOk())
                .andExpect(view().name("acp/general/board"));

        verify(boardSettingsServiceMock, times(1)).getBoardSettings();
    }

}