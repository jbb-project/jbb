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

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.jbb.board.api.base.BoardException;
import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.impl.BoardConfig;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BoardConfig.class, DbConfig.class, PropertiesConfig.class, MvcConfig.class, EventBusConfig.class,
        CommonsConfig.class, MockCommonsConfig.class})
@WebAppConfiguration
public class BoardNameServiceIT {

    @Autowired
    private DefaultBoardSettingsService boardNameService;

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenNullBoardNamePassed() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName(null);
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat("HH:mm:ss");

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
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat("HH:mm:ss");

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
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat("HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        assertThat(boardNameService.getBoardSettings().getBoardName()).isEqualTo(newBoardName);
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenNullDateFormatPassed() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("New board name");
        boardSettings.setDateFormat(null);
        boardSettings.setDurationFormat("HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenEmptyDateFormatPassed() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("New board name");
        boardSettings.setDateFormat(StringUtils.EMPTY);
        boardSettings.setDurationFormat("HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenIncorrectDateFormatPassed() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("New board name");
        boardSettings.setDateFormat("dd/MM/yyyy xHdolLH:mm:s");
        boardSettings.setDurationFormat("HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenNullDurationFormatPassed() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("New board name");
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat(null);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenEmptyDurationFormatPassed() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("New board name");
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat(StringUtils.EMPTY);

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenIncorrectDurationFormatPassed() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("New board name");
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat("HH:mcmi@s!m:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test
    public void shouldSetAndGetBoardNameWithMax60CharactersLength() throws Exception {
        // given
        String newBoardName = "123456123456123456123456123456123456123456123456123456123456";

        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName(newBoardName);
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat("HH:mm:ss");

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
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat("HH:mm:ss");

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
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat("HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenWhitespacesPassedAsDateFormat() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("Board name");
        boardSettings.setDateFormat("         ");
        boardSettings.setDurationFormat("HH:mm:ss");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }

    @Test(expected = BoardException.class)
    public void shouldThrowBoardException_whenWhitespacesPassedAsDurationFormat() throws Exception {
        // given
        BoardSettings boardSettings = new BoardSettings();
        boardSettings.setBoardName("Board name");
        boardSettings.setDateFormat("dd/MM/yyyy HH:mm:ss");
        boardSettings.setDurationFormat("         ");

        // when
        boardNameService.setBoardSettings(boardSettings);

        // then
        // throw BoardException
    }
}