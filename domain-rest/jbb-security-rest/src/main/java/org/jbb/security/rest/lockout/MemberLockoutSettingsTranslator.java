/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.lockout;

import org.jbb.security.api.lockout.MemberLockoutSettings;
import org.springframework.stereotype.Component;

@Component
public class MemberLockoutSettingsTranslator {

    public MemberLockoutSettingsDto toDto(MemberLockoutSettings lockoutSettings) {
        return MemberLockoutSettingsDto.builder()
                .failedAttemptsThreshold(lockoutSettings.getFailedAttemptsThreshold())
                .failedSignInAttemptsExpirationMinutes(lockoutSettings.getFailedSignInAttemptsExpirationMinutes())
                .lockoutDurationMinutes(lockoutSettings.getLockoutDurationMinutes())
                .lockingEnabled(lockoutSettings.getLockingEnabled())
                .build();
    }

    public MemberLockoutSettings toModel(MemberLockoutSettingsDto dto) {
        return MemberLockoutSettings.builder()
                .failedAttemptsThreshold(dto.getFailedAttemptsThreshold())
                .failedSignInAttemptsExpirationMinutes(dto.getFailedSignInAttemptsExpirationMinutes())
                .lockoutDurationMinutes(dto.getLockoutDurationMinutes())
                .lockingEnabled(dto.getLockingEnabled())
                .build();
    }
}
