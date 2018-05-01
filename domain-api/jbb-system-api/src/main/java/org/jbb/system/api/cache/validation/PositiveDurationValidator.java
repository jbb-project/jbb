/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.cache.validation;

import java.time.Duration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PositiveDurationValidator implements ConstraintValidator<PositiveDuration, Duration> {

    @Override
    public void initialize(PositiveDuration constraintAnnotation) {
        // not needed...
    }

    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext context) {
        return !duration.isZero() && !duration.isNegative();
    }

}
