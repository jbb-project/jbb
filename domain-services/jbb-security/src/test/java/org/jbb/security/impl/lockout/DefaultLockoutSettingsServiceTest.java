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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.lockout.MemberLockoutException;
import org.jbb.security.api.lockout.MemberLockoutSettings;
import org.jbb.security.event.MemberLockoutSettingsChangedEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLockoutSettingsServiceTest {

    @Mock
    private MemberLockProperties memberLockPropertiesMock;

    @Mock
    private MemberLockoutSettingsValidator settingsValidatorMock;

    @Mock
    private JbbEventBus eventBusMock;

    @InjectMocks
    private DefaultLockoutSettingsService defaultLockoutSettingsService;

    @Test
    public void getMemberLockServiceSettings() {
        // given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(1);
        when(memberLockPropertiesMock.lockoutDurationMinutes()).thenReturn(2L);
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(3L);

        // when
        MemberLockoutSettings userLockServiceSettings = defaultLockoutSettingsService
            .getLockoutSettings();

        // then
        assertThat(userLockServiceSettings.getLockoutDurationMinutes()).isEqualTo(2L);
        assertThat(userLockServiceSettings.getFailedSignInAttemptsExpirationMinutes())
            .isEqualTo(3L);
        assertThat(userLockServiceSettings.isLockingEnabled()).isEqualTo(true);
        assertThat(userLockServiceSettings.getFailedAttemptsThreshold()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullSettingsPassed() throws Exception {
        // when
        defaultLockoutSettingsService.setLockoutSettings(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = MemberLockoutException.class)
    public void shouldThrowMemberLockoutException_whenInvalidettingsPassed() throws Exception {
        // given
        MemberLockoutSettings invalidLockoutSettings = MemberLockoutSettings.builder()
            .lockingEnabled(true)
            .lockoutDurationMinutes(10L)
            .failedSignInAttemptsExpirationMinutes(-2L)
            .failedAttemptsThreshold(2)
            .build();

        Mockito.doThrow(new MemberLockoutException(Sets.newHashSet()))
            .when(settingsValidatorMock).validate(any());

        // when
        defaultLockoutSettingsService.setLockoutSettings(invalidLockoutSettings);

        // then
        // throw MemberLockoutException
    }

    @Test
    public void shouldSendEvent_whenLockoutSettingsUpdated() throws Exception {
        // given
        MemberLockoutSettings newLockoutSettings = MemberLockoutSettings.builder()
            .lockingEnabled(true)
            .lockoutDurationMinutes(10L)
            .failedSignInAttemptsExpirationMinutes(5L)
            .failedAttemptsThreshold(2)
            .build();

        // when
        defaultLockoutSettingsService.setLockoutSettings(newLockoutSettings);

        // then
        Mockito.verify(eventBusMock).post(any(MemberLockoutSettingsChangedEvent.class));
    }
}