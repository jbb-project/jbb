/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.api.format.validation;


import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidDurationFormatValidator implements ConstraintValidator<ValidDurationFormat, String> {

    @Override
    public void initialize(ValidDurationFormat constraintAnnotation) {
        // not needed
    }

    @Override
    public boolean isValid(String pattern, ConstraintValidatorContext context) {
        try {
            Validate.notBlank(pattern);
            DurationFormatUtils.formatDuration(1L, pattern);
            DateTimeFormatter.ofPattern(pattern);
            return true;
        } catch (Exception e) {
            log.trace("Duration format validation error for pattern: '{}'", pattern, e);
            return false;
        }
    }
}
