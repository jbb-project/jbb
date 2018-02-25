/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.logging.model;

import org.jbb.system.api.logging.LoggingSettingsService;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppLoggerNameUniqueValidator implements ConstraintValidator<AppLoggerNameUnique, String> {

    private final LoggingSettingsService loggingSettingsService;

    @Override
    public void initialize(AppLoggerNameUnique constraintAnnotation) {
        // not needed...
    }

    @Override
    public boolean isValid(String loggerName, ConstraintValidatorContext context) {
        return !loggingSettingsService.getLogger(loggerName).isPresent();
    }

}
