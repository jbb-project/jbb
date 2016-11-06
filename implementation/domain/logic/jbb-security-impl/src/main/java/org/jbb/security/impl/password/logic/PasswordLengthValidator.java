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

@Component
public class PasswordLengthValidator {
    public void validate(PasswordRequirements newRequirements) {
        Integer minimumLength = newRequirements.minimumLength().orElse(1);
        Integer maximumLength = newRequirements.maximumLength().orElse(Integer.MAX_VALUE);

        // assert min > 0
        if (minimumLength <= 0) {
            throw new IllegalArgumentException(String.format("Minimum length must be positive! Given: %s", minimumLength));
        }

        // assert max > 0
        if (maximumLength <= 0) {
            throw new IllegalArgumentException(String.format("Maximum length must be positive! Given: %s", maximumLength));
        }

        // assert min <= max
        if (minimumLength > maximumLength) {
            throw new IllegalArgumentException(String.format("Minimum length of password is greater than max length (%s > %s)",
                    minimumLength, maximumLength));
        }
    }
}
