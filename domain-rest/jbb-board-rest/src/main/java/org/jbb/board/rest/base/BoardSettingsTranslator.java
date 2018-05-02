/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.base;

import org.jbb.board.api.base.BoardSettings;
import org.springframework.stereotype.Component;

@Component
public class BoardSettingsTranslator {

    public BoardSettings toModel(BoardSettingsDto dto) {
        return BoardSettings.builder()
                .boardName(dto.getBoardName())
                .build();
    }

    public BoardSettingsDto toDto(BoardSettings boardSettings) {
        return BoardSettingsDto.builder()
                .boardName(boardSettings.getBoardName())
                .build();
    }
}
