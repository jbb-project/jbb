/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.base.data;

import org.hibernate.validator.constraints.NotEmpty;
import org.jbb.board.api.model.BoardSettings;
import org.jbb.board.impl.base.data.validation.ValidDateFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSettingsImpl implements BoardSettings {
    @NotEmpty
    private String boardName;

    @ValidDateFormat
    private String dateFormat;

    public BoardSettingsImpl() {
    }

    public BoardSettingsImpl(BoardSettings boardSettings) {
        this.boardName = boardSettings.getBoardName();
        this.dateFormat = boardSettings.getDateFormat();
    }
}
