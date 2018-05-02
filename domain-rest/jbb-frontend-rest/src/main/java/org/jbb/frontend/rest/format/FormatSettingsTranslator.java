/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.rest.format;

import org.jbb.frontend.api.format.FormatSettings;
import org.springframework.stereotype.Component;

@Component
public class FormatSettingsTranslator {

    public FormatSettings toModel(FormatSettingsDto dto) {
        return FormatSettings.builder()
                .dateFormat(dto.getDateFormat())
                .durationFormat(dto.getDurationFormat())
                .build();
    }

    public FormatSettingsDto toDto(FormatSettings formatSettings) {
        return FormatSettingsDto.builder()
                .dateFormat(formatSettings.getDateFormat())
                .durationFormat(formatSettings.getDurationFormat())
                .build();
    }
}
