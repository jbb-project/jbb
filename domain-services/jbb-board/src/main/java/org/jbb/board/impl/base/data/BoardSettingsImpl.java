/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.base.data;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.impl.base.data.validation.ValidDateFormat;
import org.jbb.board.impl.base.data.validation.ValidDurationFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardSettingsImpl implements BoardSettings {
    @NotBlank
    @Length(min = 1, max = 60)
    private String boardName;

    @ValidDateFormat
    @NotBlank
    private String dateFormat;

    @ValidDurationFormat
    @NotBlank
    private String durationFormat;

    public BoardSettingsImpl(BoardSettings boardSettings) {
        this.boardName = boardSettings.getBoardName();
        this.dateFormat = boardSettings.getDateFormat();
        this.durationFormat = boardSettings.getDurationFormat();
    }

}
