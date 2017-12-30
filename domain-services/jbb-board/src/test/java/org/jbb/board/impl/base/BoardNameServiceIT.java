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

import org.apache.commons.lang3.StringUtils;
import org.jbb.board.api.base.BoardException;
import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardNameServiceIT extends BaseIT {

    @Autowired
    private DefaultBoardSettingsService boardNameService;

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenNullBoardNamePassed() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName(null);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenEmptyBoardNamePassed() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName(StringUtils.EMPTY);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test
    public void shouldSetAndGetBoardName() throws Exception {
        // given
        String newBoardName = "New board name";

        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName(newBoardName);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        assertThat(boardNameService.getBoardSettings().getBoardName()).isEqualTo(newBoardName);
    }

    @Test
    public void shouldSetAndGetBoardNameWithMax60CharactersLength() throws Exception {
        // given
        String newBoardName = "123456123456123456123456123456123456123456123456123456123456";

        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName(newBoardName);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        assertThat(boardNameService.getBoardSettings().getBoardName()).isEqualTo(newBoardName);
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenBoardNameHasMoreThan60Characters() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij1");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenWhitespacesPassedAsBoardName() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("               ");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

}