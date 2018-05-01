/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password;

import org.apache.commons.lang3.Validate;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordRequirements;
import org.springframework.stereotype.Component;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PasswordRequirementsPolicy {

    private final PasswordProperties passwordProperties;
    private final Validator validator;

    public PasswordRequirements currentRequirements() {
        return PasswordRequirements.builder()
                .minimumLength(passwordProperties.passwordMinimumLength())
                .maximumLength(passwordProperties.passwordMaximumLength())
                .build();
    }

    public void update(PasswordRequirements newRequirements) {
        Set<ConstraintViolation<PasswordRequirements>> validationResult = validator
                .validate(newRequirements);
        if (!validationResult.isEmpty()) {
            throw new PasswordException(validationResult);
        }

        int minimumLength = newRequirements.getMinimumLength();
        int maximumLength = newRequirements.getMaximumLength();

        passwordProperties
                .setProperty(PasswordProperties.PSWD_MIN_LENGTH_KEY, Integer.toString(minimumLength));
        passwordProperties
                .setProperty(PasswordProperties.PSWD_MAX_LENGTH_KEY, Integer.toString(maximumLength));
    }

    public boolean assertMeetCriteria(String password) {
        Validate.notNull(password);

        if (password.isEmpty()) {
            return false;
        }

        PasswordRequirements requirements = currentRequirements();
        boolean minimumLengthCriteria = password.length() >= requirements.getMinimumLength();
        boolean maximumLengthCriteria = password.length() <= requirements.getMaximumLength();

        return minimumLengthCriteria && maximumLengthCriteria;
    }
}
