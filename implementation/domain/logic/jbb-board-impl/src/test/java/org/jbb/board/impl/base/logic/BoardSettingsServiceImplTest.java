/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.base.logic;

import com.google.common.collect.Sets;

import org.jbb.board.api.exception.BoardException;
import org.jbb.board.api.model.BoardSettings;
import org.jbb.board.impl.base.data.BoardSettingsImpl;
import org.jbb.board.impl.base.properties.BoardProperties;
import org.jbb.lib.mvc.formatters.LocalDateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BoardSettingsServiceImplTest {
    @Mock
    private BoardProperties propertiesMock;

    @Mock
    private LocalDateTimeFormatter localDateTimeFormatterMock;

    @Mock
    private Validator validatorMock;


    @InjectMocks
    private BoardSettingsServiceImpl boardNameService;

    @Test
    public void shouldUseBoardNameFromProperties() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        // when
        boardNameService.getBoardSettings();

        // then
        verify(propertiesMock, times(1)).boardName();
    }

    @Test
    public void shouldUseDateFormatFromFormatter() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        // when
        boardNameService.getBoardSettings();

        // then
        verify(localDateTimeFormatterMock, times(1)).getCurrentPattern();
    }

    @Test
    public void shouldSetBoardNameKeyProperty() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        String newBoardName = "Board 2000";

        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName(newBoardName);
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        verify(propertiesMock, times(1)).setProperty(eq(BoardProperties.BOARD_NAME_KEY), eq(newBoardName));
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenValidationFailed() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        boardNameService.setBoardSettings(mock(BoardSettings.class));

        // then
        // throw BoardException
    }
}