/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.base.logic;

import org.apache.commons.lang3.StringUtils;
import org.jbb.frontend.impl.FrontendConfig;
import org.jbb.frontend.impl.base.logic.BoardNameServiceImpl;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {FrontendConfig.class, PropertiesConfig.class, CoreConfigMocks.class})
public class BoardNameServiceIT {

    @Autowired
    private BoardNameServiceImpl boardNameService;

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenNullBoardNamePassed() throws Exception {
        // when
        boardNameService.setBoardName(null);

        // then
        // throw IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenEmptyBoardNamePassed() throws Exception {
        // when
        boardNameService.setBoardName(StringUtils.EMPTY);

        // then
        // throw IllegalArgumentException
    }

    @Test
    public void shouldSetAndGetBoardName() throws Exception {
        // given
        String newBoardName = "New board name";

        // when
        boardNameService.setBoardName(newBoardName);

        // then
        assertThat(boardNameService.getBoardName()).isEqualTo(newBoardName);
    }
}