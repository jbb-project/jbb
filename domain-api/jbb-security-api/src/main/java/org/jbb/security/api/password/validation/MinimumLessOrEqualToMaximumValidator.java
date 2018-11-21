/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.password.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.jbb.security.api.password.PasswordPolicy;

public class MinimumLessOrEqualToMaximumValidator
    implements ConstraintValidator<MinimumLessOrEqualToMaximum, PasswordPolicy> {

    @Override
    public void initialize(MinimumLessOrEqualToMaximum constraintAnnotation) {
        // not needed...
    }

    @Override
    public boolean isValid(PasswordPolicy passwordPolicy,
                           ConstraintValidatorContext context) {
        if (passwordPolicy.getMinimumLength() == null
            || passwordPolicy.getMaximumLength() == null) {
            return true;
        }

        boolean result = passwordPolicy.getMinimumLength() <= passwordPolicy.getMaximumLength();

        if (!result) {
            context.disableDefaultConstraintViolation();
            String messageTemplate = context.getDefaultConstraintMessageTemplate();
            context.buildConstraintViolationWithTemplate(messageTemplate)
                    .addPropertyNode("minimumLength").addConstraintViolation();
            context.buildConstraintViolationWithTemplate(messageTemplate)
                    .addPropertyNode("maximumLength").addConstraintViolation();
        }
        return result;
    }
}
