/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;

import org.jbb.security.api.password.PasswordRequirements;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.impl.password.data.PasswordProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Component
public class PasswordLengthRequirements implements UpdateAwarePasswordRequirements {
    private final PasswordProperties properties;
    private final Validator validator;

    @Autowired
    public PasswordLengthRequirements(PasswordProperties properties,
                                      Validator validator) {
        this.properties = properties;
        this.validator = validator;
    }

    @Override
    public int minimumLength() {
        return properties.passwordMinimumLength();
    }

    @Override
    public int maximumLength() {
        return properties.passwordMaximumLength();
    }

    @Override
    public void update(PasswordRequirements newRequirements) {
        PasswordRequirements passwordRequirements = new PasswordRequirementsImpl(newRequirements);

        Set<ConstraintViolation<PasswordRequirements>> validationResult = validator.validate(passwordRequirements);
        if (!validationResult.isEmpty()) {
            throw new PasswordException(validationResult);
        }

        int minimumLength = newRequirements.minimumLength();
        int maximumLength = newRequirements.maximumLength();

        properties.setProperty(PasswordProperties.PSWD_MIN_LENGTH_KEY, Integer.toString(minimumLength));
        properties.setProperty(PasswordProperties.PSWD_MAX_LENGTH_KEY, Integer.toString(maximumLength));
    }
}
