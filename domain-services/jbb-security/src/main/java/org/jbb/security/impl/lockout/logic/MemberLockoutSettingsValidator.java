/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.logic;

import org.jbb.security.api.lockout.MemberLockoutException;
import org.jbb.security.api.lockout.MemberLockoutSettings;
import org.jbb.security.impl.lockout.data.MemberLockoutSettingsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Component
public class MemberLockoutSettingsValidator {
    private final Validator validator;

    @Autowired
    public MemberLockoutSettingsValidator(Validator validator) {
        this.validator = validator;
    }

    public void validate(MemberLockoutSettings settings) {

        MemberLockoutSettingsImpl settingsModel = MemberLockoutSettingsImpl.builder()
                .lockingEnabled(settings.isLockingEnabled())
                .lockoutDuration(settings.getLockoutDurationMinutes())
                .failedAttemptsThreshold(settings.getFailedAttemptsThreshold())
                .failedAttemptsExpiration(settings.getFailedSignInAttemptsExpirationMinutes())
                .build();

        Set<ConstraintViolation<MemberLockoutSettingsImpl>> validationResult = validator.validate(settingsModel);

        if (!validationResult.isEmpty()) {
            throw new MemberLockoutException(validationResult);
        }
    }

}
