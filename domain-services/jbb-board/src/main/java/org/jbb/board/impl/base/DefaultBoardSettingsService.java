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

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.board.api.base.BoardException;
import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.board.event.BoardSettingsChangedEvent;
import org.jbb.lib.eventbus.JbbEventBus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultBoardSettingsService implements BoardSettingsService {
    private final BoardProperties properties;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Override
    public BoardSettings getBoardSettings() {
        return BoardSettings.builder()
            .boardName(getBoardName())
            .build();
    }

    @Override
    public void setBoardSettings(BoardSettings boardSettings) {
        Validate.notNull(boardSettings);

        Set<ConstraintViolation<BoardSettings>> validationResult = validator
            .validate(boardSettings);

        if (validationResult.isEmpty()) {
            setBoardName(boardSettings.getBoardName());
            eventBus.post(new BoardSettingsChangedEvent());
        } else {
            throw new BoardException(validationResult);
        }
    }

    private String getBoardName() {
        return properties.boardName();
    }

    private void setBoardName(String newBoardName) {
        Validate.notEmpty(newBoardName, "Board name cannot be empty or null");
        properties.setProperty(BoardProperties.BOARD_NAME_KEY, newBoardName);
    }

}
