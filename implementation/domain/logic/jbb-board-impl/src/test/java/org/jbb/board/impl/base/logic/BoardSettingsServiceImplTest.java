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

import org.apache.commons.lang3.StringUtils;
import org.jbb.board.impl.base.properties.BoardProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BoardSettingsServiceImplTest {
    @Mock
    private BoardProperties propertiesMock;

    @InjectMocks
    private BoardSettingsServiceImpl boardNameService;

    @Test
    public void shouldUseBoardNameFromProperties() throws Exception {
        // when
        boardNameService.getBoardName();

        // then
        verify(propertiesMock, times(1)).boardName();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenNullBoardNamePassed() throws Exception {
        // when
        boardNameService.setBoardName(null);

        // then
        // should throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenEmptyBoardNamePassed() throws Exception {
        // when
        boardNameService.setBoardName(StringUtils.EMPTY);

        // then
        // should throw IllegalArgumentException
    }

    @Test
    public void shouldSetBoardNameKeyProperty() throws Exception {
        // given
        String newBoardName = "Board 2000";

        // when
        boardNameService.setBoardName(newBoardName);

        // then
        verify(propertiesMock, times(1)).setProperty(eq(BoardProperties.BOARD_NAME_KEY), eq(newBoardName));
    }
}