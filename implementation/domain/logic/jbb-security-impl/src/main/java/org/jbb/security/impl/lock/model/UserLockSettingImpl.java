/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock.model;


import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.jbb.security.api.model.UserLockSettings;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class UserLockSettingImpl implements UserLockSettings {

    @NotEmpty
    @NotBlank
    private String maximumNumberOfInvalidSignInAttempts;

    @NotBlank
    @NotEmpty
    private String invalidAttemptsMeasurementTimePeriod;

    @NotBlank
    @NotEmpty
    private String accountLockTimePeriod;

    @NotBlank
    @NotEmpty
    private String serviceAvailable;

    @Override
    public String maximumNumberOfInvalidSignInAttempts() {
        return maximumNumberOfInvalidSignInAttempts;
    }

    @Override
    public String invalidAttemptsMeasurementTimePeriod() {
        return invalidAttemptsMeasurementTimePeriod;
    }

    @Override
    public String accountLockTimePeriod() {
        return accountLockTimePeriod;
    }

    @Override
    public String serviceAvailable() {
        return serviceAvailable;
    }
}
