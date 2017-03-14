/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.model.logging;

import org.jbb.system.api.service.LoggingSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class AppLoggerNameUniqueValidator implements ConstraintValidator<AppLoggerNameUnique, String> {

    @Autowired
    private LoggingSettingsService loggingSettingsService;

    @Override
    public void initialize(AppLoggerNameUnique constraintAnnotation) {
        // not needed...
    }

    @Override
    public boolean isValid(String loggerName, ConstraintValidatorContext context) {
        return !loggingSettingsService.getLogger(loggerName).isPresent();
    }

}
