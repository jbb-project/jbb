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

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.board.api.base.BoardException;
import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.board.event.BoardSettingsChangedEvent;
import org.jbb.board.impl.base.properties.BoardProperties;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.mvc.formatters.DurationFormatter;
import org.jbb.lib.mvc.formatters.LocalDateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardSettingsServiceImpl implements BoardSettingsService {
    private final BoardProperties properties;
    private final LocalDateTimeFormatter localDateTimeFormatter;
    private final DurationFormatter durationFormatter;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Override
    public BoardSettings getBoardSettings() {
        return BoardSettings.builder()
            .boardName(getBoardName())
            .dateFormat(getDateFormat())
            .durationFormat(getDurationFormat())
            .build();
    }

    @Override
    public void setBoardSettings(BoardSettings boardSettings) {
        Validate.notNull(boardSettings);

        Set<ConstraintViolation<BoardSettings>> validationResult = validator
            .validate(boardSettings);

        if (validationResult.isEmpty()) {
            setBoardName(boardSettings.getBoardName());
            setDateFormat(boardSettings.getDateFormat());
            setDurationFormat(boardSettings.getDurationFormat());
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

    private String getDateFormat() {
        return localDateTimeFormatter.getCurrentPattern();
    }

    private void setDateFormat(String dateFormat) {
        Validate.notEmpty(dateFormat);
        localDateTimeFormatter.setPattern(dateFormat);
    }

    private String getDurationFormat() {
        return durationFormatter.getPattern();
    }

    public void setDurationFormat(String durationFormat) {
        durationFormatter.setPattern(durationFormat);
    }
}
