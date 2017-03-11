/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.data;


import org.jbb.security.api.model.MemberLockoutSettings;

import javax.validation.constraints.Min;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class MemberLockoutSettingsImpl implements MemberLockoutSettings {

    @Min(1)
    private int failedAttemptsThreshold;

    @Min(1)
    private Long failedAttemptsExpiration;

    @Min(1)
    private Long lockoutDuration;

    private boolean enabled;

    public int getFailedAttemptsThreshold() {
        return failedAttemptsThreshold;
    }

    @Override
    public long getFailedSignInAttemptsExpirationMinutes() {
        return failedAttemptsExpiration;
    }

    @Override
    public long getLockoutDurationMinutes() {
        return lockoutDuration;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
