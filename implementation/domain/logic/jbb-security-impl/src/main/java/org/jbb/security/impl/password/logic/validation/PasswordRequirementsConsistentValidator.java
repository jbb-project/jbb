/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic.validation;

import org.jbb.security.impl.password.logic.PasswordRequirementsImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordRequirementsConsistentValidator
        implements ConstraintValidator<PasswordRequirementsConsistent, PasswordRequirementsImpl> {

    @Override
    public void initialize(PasswordRequirementsConsistent constraintAnnotation) {
        // not needed...
    }

    @Override
    public boolean isValid(PasswordRequirementsImpl passwordRequirements, ConstraintValidatorContext context) {
        boolean result = passwordRequirements.minimumLength() < passwordRequirements.maximumLength();

        if (!result) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{org.jbb.security.impl.password.logic.validation.PasswordRequirementsConsistent.message}")
                    .addPropertyNode("minimumLength").addConstraintViolation();
        }
        return result;
    }
}
