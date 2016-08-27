/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.entities.validation;

import org.jbb.security.services.PasswordRequirementsPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordRequirementsSatisfiedValidator implements ConstraintValidator<PasswordRequirementsSatisfied, String> {
    @Autowired
    private PasswordRequirementsPolicy requirementsPolicy;

    @Override
    public void initialize(PasswordRequirementsSatisfied annotation) {
        // not needed...
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate("Huhu");
        return requirementsPolicy.assertMeetCriteria(password);
    }
}