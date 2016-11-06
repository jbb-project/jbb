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
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PasswordLengthValidator {
    public void validate(PasswordRequirements newRequirements) {
        Optional<Integer> minimumLength = newRequirements.minimumLength();
        Optional<Integer> maximumLength = newRequirements.maximumLength();

        // assert min > 0
        if (minimumLength.isPresent() && minimumLength.get() <= 0) {
            throw new IllegalArgumentException(String.format("Minimum length must be positive! Given: %s", minimumLength.get()));
        }

        // assert max > 0
        if (maximumLength.isPresent() && maximumLength.get() <= 0) {
            throw new IllegalArgumentException(String.format("Maximum length must be positive! Given: %s", maximumLength.get()));
        }

        // assert min <= max
        if (minimumLength.isPresent() && maximumLength.isPresent()
                && minimumLength.get() > maximumLength.get()) {
            throw new IllegalArgumentException(String.format("Minimum length of password is greater than max length (%s > %s)",
                    minimumLength.get(), maximumLength.get()));
        }
    }
}
