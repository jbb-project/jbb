/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.registration.logic;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Set;

import javax.validation.ConstraintViolation;

@Component
public class RegistrationSettingsErrorsBindingMapper {
    public void map(Set<ConstraintViolation<?>> constraintViolations, BindingResult bindingResult) {
        for (ConstraintViolation violation : constraintViolations) {
            String propertyPath = violation.getPropertyPath().toString();
            switch (propertyPath) {
                case "minimumLength":
                    bindingResult.rejectValue("minPassLength", "x", violation.getMessage());
                    break;
                case "maximumLength":
                    bindingResult.rejectValue("maxPassLength", "x", violation.getMessage());
                    break;
                default:
                    bindingResult.rejectValue(propertyPath, "x", violation.getMessage());
            }
        }
    }
}
