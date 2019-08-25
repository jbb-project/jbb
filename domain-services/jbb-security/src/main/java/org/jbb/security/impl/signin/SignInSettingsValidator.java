/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.signin;

import org.jbb.security.api.signin.SignInSettings;
import org.jbb.security.api.signin.SignInSettingsException;
import org.springframework.stereotype.Component;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SignInSettingsValidator {
    private final Validator validator;

    public void validate(SignInSettings settings) {
        Set<ConstraintViolation<SignInSettings>> validationResult = validator
                .validate(settings);

        if (!validationResult.isEmpty()) {
            throw new SignInSettingsException(validationResult);
        }
    }

}
