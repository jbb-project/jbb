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

import org.jbb.security.api.lockout.LockoutSettingsService;
import org.jbb.security.api.lockout.MemberLockoutException;
import org.jbb.security.api.lockout.MemberLockoutSettings;
import org.jbb.security.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class LockoutSettingsServiceIT extends BaseIT {

    @Autowired
    private MemberLockProperties memberLockProperties;

    @Autowired
    private LockoutSettingsService lockoutSettingsService;

    @Test
    public void setNewValuesOfProperties_NoExceptionShouldBeThrow() {

        //given
        MemberLockoutSettings settings = MemberLockoutSettings
            .builder()
            .lockoutDurationMinutes(100L)
            .failedSignInAttemptsExpirationMinutes(100L)
            .failedAttemptsThreshold(100)
            .lockingEnabled(true)
            .build();

        //when
        lockoutSettingsService.setLockoutSettings(settings);

        //then
        assertThat(memberLockProperties.failedAttemptsExpirationMinutes()).isEqualTo(100L);
        assertThat(memberLockProperties.lockoutEnabled()).isEqualTo(true);
        assertThat(memberLockProperties.lockoutDurationMinutes()).isEqualTo(100L);
        assertThat(memberLockProperties.failedAttemptsThreshold()).isEqualTo(100);
    }

    @Test(expected = MemberLockoutException.class)
    public void shouldThrow_whenValidationFailed() {
        // given
        MemberLockoutSettings settings = MemberLockoutSettings.builder()
                .lockoutDurationMinutes(-100L)
                .failedSignInAttemptsExpirationMinutes(100L)
                .failedAttemptsThreshold(100)
                .lockingEnabled(true)
                .build();

        // when
        lockoutSettingsService.setLockoutSettings(settings);

        // then
        // throw MemberLockoutException
    }
}