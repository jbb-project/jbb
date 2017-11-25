/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.format;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.frontend.api.format.FormatException;
import org.jbb.frontend.api.format.FormatSettings;
import org.jbb.frontend.api.format.FormatSettingsService;
import org.jbb.frontend.event.FormatSettingsChangedEvent;
import org.jbb.lib.eventbus.JbbEventBus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultFormatSettingsService implements FormatSettingsService {

    private final LocalDateTimeFormatter localDateTimeFormatter;
    private final DurationFormatter durationFormatter;

    private final Validator validator;
    private final JbbEventBus eventBus;

    @Override
    public FormatSettings getFormatSettings() {
        return FormatSettings.builder()
            .dateFormat(localDateTimeFormatter.getCurrentPattern())
            .durationFormat(durationFormatter.getPattern())
            .build();
    }

    @Override
    public void setFormatSettings(FormatSettings formatSettings) {
        Validate.notNull(formatSettings);

        Set<ConstraintViolation<FormatSettings>> validationResult = validator
            .validate(formatSettings);

        if (validationResult.isEmpty()) {
            localDateTimeFormatter.setPattern(formatSettings.getDateFormat());
            durationFormatter.setPattern(formatSettings.getDurationFormat());
            eventBus.post(new FormatSettingsChangedEvent());
        } else {
            throw new FormatException(validationResult);
        }

    }
}
