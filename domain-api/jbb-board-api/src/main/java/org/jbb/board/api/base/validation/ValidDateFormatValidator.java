/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.api.base.validation;


import java.time.format.DateTimeFormatter;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;

@Slf4j
public class ValidDateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {

    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
        // not needed
    }

    @Override
    public boolean isValid(String pattern, ConstraintValidatorContext context) {
        try {
            Validate.notEmpty(pattern);
            DateTimeFormatter.ofPattern(pattern);
            return true;
        } catch (Exception e) {
            log.trace("Date format validation error for pattern: '{}'", pattern, e);
            return false;
        }
    }
}
