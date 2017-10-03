/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.api.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.jbb.board.api.base.validation.ValidDateFormat;
import org.jbb.board.api.base.validation.ValidDurationFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardSettings {
    @NotBlank
    @Length(min = 1, max = 60)
    private String boardName;

    @ValidDateFormat
    @NotBlank
    private String dateFormat;

    @ValidDurationFormat
    @NotBlank
    private String durationFormat;

}
