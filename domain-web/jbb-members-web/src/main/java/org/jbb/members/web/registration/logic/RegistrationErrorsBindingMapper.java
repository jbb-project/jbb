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

import java.text.MessageFormat;
import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jbb.security.api.password.PasswordPolicy;
import org.jbb.security.api.password.PasswordService;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@RequiredArgsConstructor
public class RegistrationErrorsBindingMapper {
    private final PasswordService passwordService;

    private static String unwrap(String s) {
        if (s.isEmpty()) {
            return "password";
        }
        return StringUtils.removeEndIgnoreCase(s, ".value");
    }

    public void map(Set<ConstraintViolation<?>> constraintViolations, BindingResult bindingResult) {
        for (ConstraintViolation violation : constraintViolations) {
            String propertyPath = violation.getPropertyPath().toString();
            if ("visiblePassword".equals(propertyPath)) {
                PasswordPolicy policy = passwordService.currentPolicy();
                bindingResult.rejectValue("password", "x", MessageFormat
                    .format(violation.getMessage(), policy.getMinimumLength(),
                        policy.getMaximumLength()));
            } else {
                bindingResult.rejectValue(unwrap(propertyPath), "x", violation.getMessage());
            }
        }
    }
}
