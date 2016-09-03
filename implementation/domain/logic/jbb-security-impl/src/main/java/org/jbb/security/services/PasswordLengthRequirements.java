/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.services;

import org.jbb.security.api.model.PasswordRequirements;
import org.jbb.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PasswordLengthRequirements implements UpdateAwarePasswordRequirements {
    private static final Integer NO_LIMIT = 0;

    private final SecurityProperties properties;

    @Autowired
    public PasswordLengthRequirements(SecurityProperties properties) {
        this.properties = properties;
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
        Optional<Integer> minimumLength = newRequirements.minimumLength();
        Optional<Integer> maximumLength = newRequirements.maximumLength();

        // assert min <= max
        if (minimumLength.isPresent() && maximumLength.isPresent()
                && minimumLength.get() > maximumLength.get()) {
            throw new IllegalArgumentException(String.format("Minimum length of password is greater than max length (%s > %s)",
                    minimumLength.get(), maximumLength.get()));
        }
        // update minimum length of password
        if (minimumLength.isPresent()) {
            properties.setProperty(SecurityProperties.PSWD_MIN_LENGTH_KEY,
                    minimumLength.get().toString());
        } else {
            properties.setProperty(SecurityProperties.PSWD_MIN_LENGTH_KEY, NO_LIMIT.toString());
        }

        // update maximum length of password
        if (maximumLength.isPresent()) {
            properties.setProperty(SecurityProperties.PSWD_MAX_LENGTH_KEY,
                    maximumLength.get().toString());
        } else {
            properties.setProperty(SecurityProperties.PSWD_MAX_LENGTH_KEY, NO_LIMIT.toString());
        }
    }
}
