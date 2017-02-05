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


import org.jbb.security.api.model.UserLockSettings;

import javax.validation.constraints.Min;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class UserLockSettingImpl implements UserLockSettings {

    @Min(1)
    private int maximumNumberOfInvalidSignInAttempts;

    @Min(1)
    private Long invalidAttemptsMeasurementTimePeriod;

    @Min(1)
    private Long accountLockTimePeriod;

    private boolean serviceAvailable;

    @Override
    public int maximumNumberOfInvalidSignInAttempts() {
        return maximumNumberOfInvalidSignInAttempts;
    }

    @Override
    public Long invalidAttemptsMeasurementTimePeriod() {
        return invalidAttemptsMeasurementTimePeriod;
    }

    @Override
    public Long accountLockTimePeriod() {
        return accountLockTimePeriod;
    }

    @Override
    public boolean serviceAvailable() {
        return serviceAvailable;
    }
}
