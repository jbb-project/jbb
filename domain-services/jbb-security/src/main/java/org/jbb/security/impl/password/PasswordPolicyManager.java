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

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordPolicy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordPolicyManager {

    private final PasswordProperties passwordProperties;
    private final Validator validator;

    public PasswordPolicy currentPolicy() {
        return PasswordPolicy.builder()
                .minimumLength(passwordProperties.passwordMinimumLength())
                .maximumLength(passwordProperties.passwordMaximumLength())
                .build();
    }

    public void update(PasswordPolicy newPolicy) {
        Set<ConstraintViolation<PasswordPolicy>> validationResult = validator
            .validate(newPolicy);
        if (!validationResult.isEmpty()) {
            throw new PasswordException(validationResult);
        }

        int minimumLength = newPolicy.getMinimumLength();
        int maximumLength = newPolicy.getMaximumLength();

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

        PasswordPolicy policy = currentPolicy();
        boolean minimumLengthCriteria = password.length() >= policy.getMinimumLength();
        boolean maximumLengthCriteria = password.length() <= policy.getMaximumLength();

        return minimumLengthCriteria && maximumLengthCriteria;
    }
}
