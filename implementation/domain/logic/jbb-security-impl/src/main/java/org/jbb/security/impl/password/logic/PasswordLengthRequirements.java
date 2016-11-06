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

import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.impl.password.data.PasswordProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PasswordLengthRequirements implements UpdateAwarePasswordRequirements {
    private static final Integer MINIMUM = 1;
    private static final Integer MAXIMUM = Integer.MAX_VALUE;

    private final PasswordProperties properties;
    private final PasswordLengthValidator lengthValidator;

    @Autowired
    public PasswordLengthRequirements(PasswordProperties properties,
                                      PasswordLengthValidator lengthValidator) {
        this.properties = properties;
        this.lengthValidator = lengthValidator;
    }

    @Override
    public Optional<Integer> minimumLength() {
        Integer minLength = properties.passwordMinimumLength();
        if (minLength != null && minLength > 0) {
            return Optional.of(minLength);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Integer> maximumLength() {
        Integer maxLength = properties.passwordMaximumLength();
        if (maxLength != null && maxLength > 0) {
            return Optional.of(maxLength);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void update(PasswordRequirements newRequirements) {
        lengthValidator.validate(newRequirements);

        Optional<Integer> minimumLength = newRequirements.minimumLength();
        Optional<Integer> maximumLength = newRequirements.maximumLength();

        // update minimum length of password
        if (minimumLength.isPresent()) {
            properties.setProperty(PasswordProperties.PSWD_MIN_LENGTH_KEY,
                    minimumLength.get().toString());
        } else {
            properties.setProperty(PasswordProperties.PSWD_MIN_LENGTH_KEY, MINIMUM.toString());
        }

        // update maximum length of password
        if (maximumLength.isPresent()) {
            properties.setProperty(PasswordProperties.PSWD_MAX_LENGTH_KEY,
                    maximumLength.get().toString());
        } else {
            properties.setProperty(PasswordProperties.PSWD_MAX_LENGTH_KEY, MAXIMUM.toString());
        }
    }
}
