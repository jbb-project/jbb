/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic;

import org.apache.commons.lang.Validate;
import org.jbb.frontend.api.service.BoardNameService;
import org.jbb.frontend.impl.properties.FrontendProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardNameServiceImpl implements BoardNameService {
    private final FrontendProperties properties;

    @Autowired
    public BoardNameServiceImpl(FrontendProperties properties) {
        this.properties = properties;
    }

    @Override
    public String getBoardName() {
        return properties.boardName();
    }

    @Override
    public void setBoardName(String newBoardName) {
        Validate.notEmpty(newBoardName, "Board name cannot be empty or null");
        properties.setProperty(FrontendProperties.BOARD_NAME_KEY, newBoardName);
    }
}
