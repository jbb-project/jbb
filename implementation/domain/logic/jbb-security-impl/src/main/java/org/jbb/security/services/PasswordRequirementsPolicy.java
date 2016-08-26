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

import org.jbb.lib.core.vo.Password;
import org.jbb.security.api.model.PasswordRequirements;
import org.jbb.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class PasswordRequirementsPolicy {
    private static final Integer NO_LIMIT = 0;

    private final SecurityProperties properties;
    private final PasswordRequirementsImpl currentRequirements;

    @Autowired
    public PasswordRequirementsPolicy(SecurityProperties properties) {
        this.properties = properties;
        this.currentRequirements = new PasswordRequirementsImpl();
    }

    public PasswordRequirements currentRequirements() {
        return currentRequirements;
    }

    public void update(PasswordRequirements newRequirements) {
        currentRequirements.update(newRequirements);
    }

    public boolean assertMeetCriteria(Password password) {
        String passString = String.valueOf(password.getValue());

        boolean minimumLengthCriteria = true;
        if (currentRequirements.minimumLength().isPresent()) {
            minimumLengthCriteria = passString.length() >= currentRequirements.minimumLength().get();
        }

        boolean maximumLengthCriteria = true;
        if (currentRequirements.maximumLength().isPresent()) {
            maximumLengthCriteria = passString.length() <= currentRequirements.maximumLength().get();
        }

        return minimumLengthCriteria && maximumLengthCriteria;
    }

    private class PasswordRequirementsImpl implements PasswordRequirements {
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

        public void update(PasswordRequirements newRequirements) {
            // update minimum length of password
            if (newRequirements.minimumLength().isPresent()) {
                properties.setProperty(SecurityProperties.PSWD_MIN_LENGTH_KEY,
                        newRequirements.minimumLength().get().toString());
            } else {
                properties.setProperty(SecurityProperties.PSWD_MIN_LENGTH_KEY, NO_LIMIT.toString());
            }

            // update maximum length of password
            if (newRequirements.maximumLength().isPresent()) {
                properties.setProperty(SecurityProperties.PSWD_MAX_LENGTH_KEY,
                        newRequirements.maximumLength().get().toString());
            } else {
                properties.setProperty(SecurityProperties.PSWD_MAX_LENGTH_KEY, NO_LIMIT.toString());
            }
        }
    }
}
