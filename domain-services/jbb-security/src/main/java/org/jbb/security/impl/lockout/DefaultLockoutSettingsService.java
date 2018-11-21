/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.lockout.LockoutSettingsService;
import org.jbb.security.api.lockout.MemberLockoutSettings;
import org.jbb.security.event.MemberLockoutSettingsChangedEvent;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultLockoutSettingsService implements LockoutSettingsService {

    private final MemberLockProperties properties;
    private final MemberLockoutSettingsValidator settingsValidator;
    private final JbbEventBus eventBus;

    @Override
    public MemberLockoutSettings getLockoutSettings() {
        return MemberLockoutSettings.builder()
            .lockoutDurationMinutes(properties.lockoutDurationMinutes())
            .failedSignInAttemptsExpirationMinutes(properties.failedAttemptsExpirationMinutes())
            .failedAttemptsThreshold(properties.failedAttemptsThreshold())
            .lockingEnabled(properties.lockoutEnabled())
            .build();
    }

    @Override
    public void setLockoutSettings(MemberLockoutSettings settings) {
        Validate.notNull(settings);
        settingsValidator.validate(settings);

        log.debug("New values of lockout settings: " + settings.toString());
        properties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ENABLED,
            Boolean.toString(settings.isLockingEnabled()));
        properties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_DURATION_MINUTES,
            String.valueOf(settings.getLockoutDurationMinutes()));
        properties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ATTEMPTS_EXPIRATION,
            String.valueOf(settings.getFailedSignInAttemptsExpirationMinutes()));
        properties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ATTEMPTS_THRESHOLD,
            String.valueOf(settings.getFailedAttemptsThreshold()));

        eventBus.post(new MemberLockoutSettingsChangedEvent());
    }
}
