/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.base.logic;

import org.apache.commons.lang3.StringUtils;
import org.jbb.board.api.exception.BoardException;
import org.jbb.board.impl.base.BoardConfig;
import org.jbb.board.impl.base.data.BoardSettingsImpl;
import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BoardConfig.class, PropertiesConfig.class, MvcConfig.class,
        CoreConfig.class, CoreConfigMocks.class})
@WebAppConfiguration
public class BoardNameServiceIT {

    @Autowired
    private BoardSettingsServiceImpl boardNameService;

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenNullBoardNamePassed() throws Exception {
        // given
        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName(null);
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenEmptyBoardNamePassed() throws Exception {
        // given
        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName(StringUtils.EMPTY);
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test
    public void shouldSetAndGetBoardName() throws Exception {
        // given
        String newBoardName = "New board name";

        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName(newBoardName);
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        assertThat(boardNameService.getBoardSettings().getBoardName()).isEqualTo(newBoardName);
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenNullDateFormatPassed() throws Exception {
        // given
        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName("New board name");
        boardSettings.setDateFormat(null);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenEmptyDateFormatPassed() throws Exception {
        // given
        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName("New board name");
        boardSettings.setDateFormat(StringUtils.EMPTY);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenIncorrectDateFormatPassed() throws Exception {
        // given
        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName("New board name");
        boardSettings.setDateFormat("dd/MM/yyyy xHdolLH:mm:s");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test
    public void shouldSetAndGetBoardNameWithMax60CharactersLength() throws Exception {
        // given
        String newBoardName = "123456123456123456123456123456123456123456123456123456123456";

        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName(newBoardName);
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        assertThat(boardNameService.getBoardSettings().getBoardName()).isEqualTo(newBoardName);
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenBoardNameHasMoreThan60Characters() throws Exception {
        // given
        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName("abcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij1");
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenWhitespacesPassedAsBoardName() throws Exception {
        // given
        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName("               ");
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenWhitespacesPassedAsDateFormat() throws Exception {
        // given
        BoardSettingsImpl boardSettings = new BoardSettingsImpl();
        boardSettings.setBoardName("Board name");
        boardSettings.setDateFormat("         ");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }
}