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

import org.apache.commons.lang.Validate;
import org.jbb.board.api.service.BoardNameService;
import org.jbb.board.impl.base.properties.BoardProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardNameServiceImpl implements BoardNameService {
    private final BoardProperties properties;

    @Autowired
    public BoardNameServiceImpl(BoardProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getBoardName() {
        return properties.boardName();
    }

    @Override
    public void setBoardName(String newBoardName) {
        Validate.notEmpty(newBoardName, "Board name cannot be empty or null");
        properties.setProperty(BoardProperties.BOARD_NAME_KEY, newBoardName);
    }
}
