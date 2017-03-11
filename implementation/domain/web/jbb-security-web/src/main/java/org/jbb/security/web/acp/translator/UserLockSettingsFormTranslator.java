/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.translator;

import org.jbb.security.api.model.UserLockSettings;
import org.jbb.security.web.acp.form.UserLockSettingsForm;
import org.springframework.stereotype.Component;

@Component
public class UserLockSettingsFormTranslator {
    public UserLockSettings createSettingsModel(UserLockSettingsForm form) {
        return new UserLockSettings() {
            @Override
            public int maximumNumberOfInvalidSignInAttempts() {
                return form.getMaximumNumberOfInvalidSignInAttempts();
            }

            @Override
            public Long invalidAttemptsMeasurementTimePeriod() {
                return form.getInvalidAttemptsMeasurementTimePeriod();
            }

            @Override
            public Long accountLockTimePeriod() {
                return form.getAccountLockTimePeriod();
            }

            @Override
            public boolean serviceAvailable() {
                return form.isLockingEnabled();
            }
        };
    }
}
