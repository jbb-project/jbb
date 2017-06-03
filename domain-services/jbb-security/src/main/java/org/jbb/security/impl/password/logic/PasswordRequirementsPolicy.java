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

import org.apache.commons.lang3.Validate;
import org.jbb.security.api.data.PasswordRequirements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PasswordRequirementsPolicy {
    private final UpdateAwarePasswordRequirements currentRequirements;

    @Autowired
    public PasswordRequirementsPolicy(UpdateAwarePasswordRequirements currentRequirements) {
        this.currentRequirements = currentRequirements;
    }

    public PasswordRequirements currentRequirements() {
        return currentRequirements;
    }

    public void update(PasswordRequirements newRequirements) {
        currentRequirements.update(newRequirements);
    }

    public boolean assertMeetCriteria(String password) {
        Validate.notNull(password);

        if (password.isEmpty()) {
            return false;
        }

        boolean minimumLengthCriteria = password.length() >= currentRequirements.minimumLength();
        boolean maximumLengthCriteria = password.length() <= currentRequirements.maximumLength();

        return minimumLengthCriteria && maximumLengthCriteria;
    }
}
