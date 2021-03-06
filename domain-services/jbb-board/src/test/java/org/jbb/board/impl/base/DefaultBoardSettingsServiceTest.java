/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.base;

import com.google.common.collect.Sets;

import org.jbb.board.api.base.BoardException;
import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.event.BoardSettingsChangedEvent;
import org.jbb.lib.eventbus.JbbEventBus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBoardSettingsServiceTest {
    @Mock
    private BoardProperties propertiesMock;

    @Mock
    private Validator validatorMock;

    @Mock
    private JbbEventBus eventBusMock;


    @InjectMocks
    private DefaultBoardSettingsService boardNameService;

    @Test
    public void shouldUseBoardNameFromProperties() throws Exception {
        // given

        // when
        boardNameService.getBoardSettings();

        // then
        verify(propertiesMock, times(1)).boardName();
    }

    @Test
    public void shouldSetBoardNameKeyProperty() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        String newBoardName = "Board 2000";

        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName(newBoardName);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        verify(propertiesMock, times(1)).setProperty(eq(BoardProperties.BOARD_NAME_KEY), eq(newBoardName));
    }

    @Test
    public void shouldSentBoardSettingsChangedEvent_whenSetNewSettings() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        String newBoardName = "Board 2000";

        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName(newBoardName);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        verify(eventBusMock).post(isA(BoardSettingsChangedEvent.class));
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