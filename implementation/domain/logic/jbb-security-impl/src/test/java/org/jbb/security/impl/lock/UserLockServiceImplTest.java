/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock;


import org.jbb.lib.core.vo.Username;
import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.InvalidSignInAttemptEntity;
import org.jbb.security.impl.lock.model.UserLockEntity;
import org.jbb.security.impl.lock.properties.UserLockProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserLockServiceImplTest {

    @Mock
    private InvalidSignInAttemptRepository invalidSignInAttemptRepository;

    @Mock
    private UserLockRepository userLockRepository;

    @Mock
    private UserLockProperties userLockProperties;

    @InjectMocks
    private UserLockServiceImpl userLockService;


    @Test
    public void whenUserHasMoreInvalidSignInAttemptsInTimeOfPeriodThenPropertiesAllowToThenUserAccountShouldBeLocked() {

        //given
        InvalidSignInAttemptEntity lockedUser = createInvalidSignInAttemptEntity("lockedUser", 4);

        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(3);
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInLockTimePeriod()).thenReturn(Long.valueOf(10));
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(Long.valueOf(5));
        when(userLockRepository.findByUsername(lockedUser.getUsername())).thenReturn(Optional.empty());
        when(invalidSignInAttemptRepository.findByUsername(lockedUser.getUsername())).thenReturn(Optional.of(lockedUser));

        //when
        userLockService.lockUserIfQualify(lockedUser.getUsername());

        //then
        verify(userLockRepository, times(1)).save(any(UserLockEntity.class));

    }

    @Test
    public void whenUserHasLessInvalidSignInAttemptsInTimeOfPeriodThenPropertiesAllowToThenUserAccountShouldNotBeLocked() {

        //given
        InvalidSignInAttemptEntity lockedUser = createInvalidSignInAttemptEntity("lockedUser", 2);

        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(3);
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInLockTimePeriod()).thenReturn(Long.valueOf(10));
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(Long.valueOf(5));
        when(userLockRepository.findByUsername(lockedUser.getUsername())).thenReturn(Optional.empty());
        when(invalidSignInAttemptRepository.findByUsername(lockedUser.getUsername())).thenReturn(Optional.of(lockedUser));

        //when
        userLockService.lockUserIfQualify(lockedUser.getUsername());

        //then
        verify(userLockRepository, times(0)).save(any(UserLockEntity.class));
    }

    @Test
    public void whenUserEqualInvalidSignInAttemptsInTimeOfPeriodThenPropertiesAllowToThenUserAccountShouldBeLocked() {

        //given
        InvalidSignInAttemptEntity lockedUser = createInvalidSignInAttemptEntity("lockedUser", 3);

        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(3);
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInLockTimePeriod()).thenReturn(Long.valueOf(10));
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(Long.valueOf(5));
        when(userLockRepository.findByUsername(lockedUser.getUsername())).thenReturn(Optional.empty());
        when(invalidSignInAttemptRepository.findByUsername(lockedUser.getUsername())).thenReturn(Optional.of(lockedUser));

        //when
        userLockService.lockUserIfQualify(lockedUser.getUsername());

        //then
        verify(userLockRepository, times(1)).save(any(UserLockEntity.class));
    }

    @Test
    public void whenUserHasLockedAccountThenServiceShouldReturnTrue() {

        //given
        UserLockEntity lockedUser = createUserLockEntity("lockedUser");
        when(userLockRepository.findByUsername(lockedUser.getUsername())).thenReturn(Optional.of(lockedUser));

        //when
        boolean lockedUserHasAccountLock = userLockService.isUserHasAccountLock(lockedUser.getUsername());

        //then
        assertTrue(lockedUserHasAccountLock);
    }

    @Test
    public void whenUserHasNotLockedAccountThenServiceShouldReturnFalse() {

        //given
        UserLockEntity nonLockedUser = createUserLockEntity("nonLockedUser");
        when(userLockRepository.findByUsername(nonLockedUser.getUsername())).thenReturn(Optional.empty());

        //when
        boolean nonLockedUserHasAccountLock = userLockService.isUserHasAccountLock(nonLockedUser.getUsername());

        //then
        assertFalse(nonLockedUserHasAccountLock);

    }

    @Test
    public void whenUserIsNotQualifyToLockAccountThenHisAccountShouldNotBeLocked() {

        //given
        UserLockEntity lockedUser = createUserLockEntity("lockedUser");

        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(3);
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInLockTimePeriod()).thenReturn(Long.valueOf(11));
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(Long.valueOf(5));
        when(userLockRepository.findByUsername(lockedUser.getUsername())).thenReturn(Optional.of(lockedUser));

        //when
        userLockService.releaseLockFromSpecifyUser(lockedUser.getUsername());

        //then
        verify(userLockRepository, times(0)).delete(any(UserLockEntity.class));
    }

    @Test
    public void whenUserIsQualifyToReleaseLockThenLockShouldBeReleased() {

        //given
        UserLockEntity lockedUser = createUserLockEntity("lockedUser");

        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(3);
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInLockTimePeriod()).thenReturn(Long.valueOf(10));
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(Long.valueOf(5));
        when(userLockRepository.findByUsername(lockedUser.getUsername())).thenReturn(Optional.of(lockedUser));

        //when
        userLockService.releaseLockFromSpecifyUser(lockedUser.getUsername());

        //then
        verify(userLockRepository, times(1)).delete(any(UserLockEntity.class));

    }

    private UserLockEntity createUserLockEntity(String username) {
        return UserLockEntity.builder()
                .username(Username.builder().value(username).build())
                .localDateTimeWhenLockWasRaised(LocalDateTime.of(2016, 10, 19, 20, 00))
                .localDateTimeWhenLockShouldBeReleased(LocalDateTime.of(2016, 10, 19, 20, 10))
                .build();
    }

    private InvalidSignInAttemptEntity createInvalidSignInAttemptEntity(String username, int invalidAttempt) {
        return InvalidSignInAttemptEntity.builder()
                .firstInvalidAttemptDateTime(LocalDateTime.of(2016, 10, 19, 20, 00))
                .lastInvalidAttemptDateTime(LocalDateTime.of(2016, 10, 19, 20, 05))
                .invalidSignInAttempt(invalidAttempt)
                .username(Username.builder().value(username).build())
                .build();
    }

}
