/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout;


import com.google.common.collect.Lists;

import org.jbb.lib.core.time.JBBTime;
import org.jbb.security.api.model.MemberLockoutSettings;
import org.jbb.security.impl.lockout.dao.FailedSignInAttemptRepository;
import org.jbb.security.impl.lockout.dao.MemberLockRepository;
import org.jbb.security.impl.lockout.logic.MemberLockoutServiceImpl;
import org.jbb.security.impl.lockout.model.FailedSignInAttemptEntity;
import org.jbb.security.impl.lockout.model.MemberLockEntity;
import org.jbb.security.impl.lockout.properties.MemberLockProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MemberLockoutServiceImplTest {

    @Mock
    private FailedSignInAttemptRepository failedSignInAttemptRepository;

    @Mock
    private MemberLockRepository memberLockRepository;

    @Mock
    private MemberLockProperties memberLockProperties;

    @InjectMocks
    private MemberLockoutServiceImpl userLockService;


    @Test
    public void getUserLockServiceSettings() {

        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(true);
        when(memberLockProperties.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockProperties.lockoutDurationMinutes()).thenReturn(2L);
        when(memberLockProperties.failedAttemptsExpirationMinutes()).thenReturn(2L);

        //when
        MemberLockoutSettings userLockServiceSettings = userLockService.getLockoutSettings();

        //then
        assertThat(userLockServiceSettings.getLockoutDurationMinutes()).isEqualTo(2L);
        assertThat(userLockServiceSettings.getFailedSignInAttemptsExpirationMinutes()).isEqualTo(2L);
        assertThat(userLockServiceSettings.isEnabled()).isEqualTo(true);
        assertThat(userLockServiceSettings.getFailedAttemptsThreshold()).isEqualTo(2);
    }

    @Test
    public void whenReleaseLockRequestOnDemandThenRemoveUserLockFromDB() {

        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(true);
        when(memberLockRepository.findByMemberId(1L)).thenReturn(getUserLockEntity(JBBTime.now()));
        //when

        userLockService.releaseUserLock(1L);
        //then

        verify(memberLockRepository, times(1)).delete(any(MemberLockEntity.class));
        verify(memberLockRepository, times(1)).flush();
    }

    @Test
    public void whenServiceIsOfflineAndUserExceedInvalidSignInAttemptsThenUserIsNotLocked() {

        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(false);
        when(memberLockProperties.failedAttemptsThreshold()).thenReturn(2);

        //when
        userLockService.lockMemberIfQualify(1L);

        //then
        verify(memberLockRepository, never()).save(any(MemberLockEntity.class));
        verify(failedSignInAttemptRepository, never()).save(any(FailedSignInAttemptEntity.class));

    }

    @Test
    public void whenUserHasAttemptsAndUserHasFirstInvalidAttempts_AttemptsShouldBeSaveInDB() {

        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(true);
        when(memberLockProperties.failedAttemptsThreshold()).thenReturn(5);
        when(memberLockProperties.failedAttemptsExpirationMinutes()).thenReturn(10L);
        when(failedSignInAttemptRepository.findAllForMember(1L)).thenReturn(getEmptyInvalidSignInList());
        when(memberLockRepository.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepository, Mockito.times(3)).saveAndFlush(any(FailedSignInAttemptEntity.class));

    }

    @Test
    public void whenUserHasInvalidAttemptsBefore_NextAttemptsShouldBeSaveInDB() {
        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(true);
        when(memberLockProperties.failedAttemptsThreshold()).thenReturn(5);
        when(memberLockProperties.failedAttemptsExpirationMinutes()).thenReturn(10L);
        when(failedSignInAttemptRepository.findAllForMemberOrderByDateAsc(1L)).thenReturn(getInvalidsAttemptsForUser(1));
        when(memberLockRepository.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepository, Mockito.times(2)).saveAndFlush(any(FailedSignInAttemptEntity.class));
    }


    @Test
    public void whenUserExceedInvalidAttemptsAndHeDoesNotHaveInvalidAttemptsBefore_AccountShouldBeLocked() {
        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(true);
        when(memberLockProperties.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockProperties.failedAttemptsExpirationMinutes()).thenReturn(1L);
        when(memberLockProperties.lockoutDurationMinutes()).thenReturn(10L);
        when(failedSignInAttemptRepository.findAllForMemberOrderByDateAsc(1L)).thenReturn(getEmptyInvalidSignInList());
        when(failedSignInAttemptRepository.findAllForMember(1L)).thenReturn(getInvalidsAttemptsForUser(3));
        when(memberLockRepository.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);


        //then
        verify(failedSignInAttemptRepository, Mockito.times(3)).saveAndFlush(any(FailedSignInAttemptEntity.class));
        verify(memberLockRepository, Mockito.times(3)).saveAndFlush(any(MemberLockEntity.class));
    }

    @Test
    public void whenUserExceedInvalidAttemptsAndHeHasInvalidAttemptsBefore_AccountShouldBeLocked() {

        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(true);
        when(memberLockProperties.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockProperties.failedAttemptsExpirationMinutes()).thenReturn(1L);
        when(memberLockProperties.lockoutDurationMinutes()).thenReturn(10L);
        when(failedSignInAttemptRepository.findAllForMemberOrderByDateAsc(1L)).thenReturn(getInvalidsAttemptsForUser(2));
        when(failedSignInAttemptRepository.findAllForMember(1L)).thenReturn(getInvalidsAttemptsForUser(3));
        when(memberLockRepository.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepository, Mockito.times(1)).saveAndFlush(any(FailedSignInAttemptEntity.class));
        verify(memberLockRepository, Mockito.times(1)).saveAndFlush(any(MemberLockEntity.class));
    }


    @Test
    public void whenUserDoesNotHaveTooOldAttemptEntries_EntriesShouldNotBeDeleted() {

        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(true);
        when(memberLockProperties.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockProperties.failedAttemptsExpirationMinutes()).thenReturn(5L);
        when(failedSignInAttemptRepository.findAllForMemberOrderByDateAsc(1L)).thenReturn(getInvalidsAttemptsForUser(2));
        when(memberLockRepository.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepository, Mockito.times(0)).delete(any(ArrayList.class));
    }

    @Test
    public void whenUserHasAllTooOldAttemptEntries_EntriesShouldBeDeleted() {

        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(true);
        when(memberLockProperties.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockProperties.failedAttemptsExpirationMinutes()).thenReturn(5L);
        when(failedSignInAttemptRepository.findAllForMemberOrderByDateAsc(1L)).thenReturn(generateAllTooOldInvalidAttemptsEntries(3));
        when(failedSignInAttemptRepository.findAllForMember(1L)).thenReturn(generateAllTooOldInvalidAttemptsEntries(3));
        when(memberLockRepository.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepository, Mockito.atLeast(3)).delete(any(ArrayList.class));
    }

    @Test
    public void whenUserHasTooOldAndNotTooOldAttemptEntries_OnlyTooOldEntriesShouldBeDeleted() {

        //given
        when(memberLockProperties.lockoutEnabled()).thenReturn(true);
        when(memberLockProperties.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockProperties.failedAttemptsExpirationMinutes()).thenReturn(5L);
        when(failedSignInAttemptRepository.findAllForMemberOrderByDateAsc(1L)).thenReturn(generateMixedInvalidSignInAttempts(2, 2));
        when(memberLockRepository.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);
        userLockService.lockMemberIfQualify(1L);

        verify(failedSignInAttemptRepository, Mockito.atLeast(2)).delete(any(ArrayList.class));

    }


    @Test
    public void ifUserAccountBlockadeIsNotExpire_BlockadeShouldNotBeRemovedAndServiceShouldReturnTrue() {

        //given
        when(memberLockProperties.failedAttemptsExpirationMinutes()).thenReturn(5L);
        when(memberLockRepository.findByMemberId(1L)).thenReturn(getUserLockEntity(LocalDateTime.now().plusMinutes(5)));

        //when
        boolean userHasAccountLock = userLockService.isMemberHasLock(1L);

        //then
        assertTrue(userHasAccountLock);
        verify(memberLockRepository, Mockito.times(0)).delete(any(MemberLockEntity.class));
    }

    @Test
    public void ifUserAccountBlockadeIsExpire_BlockadeShouldBeRemovedAndServiceShouldReturnFalse() {

        //given
        when(memberLockRepository.findByMemberId(1L)).thenReturn(getUserLockEntity(JBBTime.now().minusMinutes(30)));

        //when
        boolean userHasAccountLock = userLockService.isMemberHasLock(1L);

        //then
        assertFalse(userHasAccountLock);
        verify(memberLockRepository, Mockito.times(1)).delete(any(MemberLockEntity.class));
    }

    @Test
    public void whenUserHasLock_NewInvalidsAttemptAreNotSaveToDB() {

        //given
        when(memberLockRepository.findByMemberId(1L)).thenReturn(getUserLockEntity(JBBTime.now()));

        //when
        userLockService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepository, Mockito.times(0)).saveAndFlush(any(FailedSignInAttemptEntity.class));
    }

    @Test
    public void whenUserHasLock_LockExpireTimeShouldBeReturn() {

        //given
        when(memberLockRepository.findByMemberId(1L)).thenReturn(getUserLockEntity(JBBTime.now()));

        //when
        Optional<LocalDateTime> userLockExpireTime = userLockService.getUserLockExpireTime(1L);

        //then
        assertTrue(userLockExpireTime.isPresent());
    }

    @Test
    public void whenUserHasNotLock_EmptyOptionalShouldBeReturn() {

        //given
        when(memberLockRepository.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        Optional<LocalDateTime> userLockExpireTime = userLockService.getUserLockExpireTime(1L);

        //then
        assertFalse(userLockExpireTime.isPresent());

    }

    private List<FailedSignInAttemptEntity> generateMixedInvalidSignInAttempts(int numberOfTooOld, int numberOfCorrect) {
        List<FailedSignInAttemptEntity> invalidAttempts = new ArrayList<>();

        for (int i = 0; i < numberOfTooOld; i++) {
            FailedSignInAttemptEntity entity = FailedSignInAttemptEntity.builder()
                    .memberId(1L)
                    .attemptDateTime(JBBTime.now().minusMinutes(10 + i))
                    .build();

            invalidAttempts.add(entity);
        }
        for (int i = 0; i < numberOfCorrect; i++) {
            FailedSignInAttemptEntity entity = FailedSignInAttemptEntity.builder()
                    .memberId(1L)
                    .attemptDateTime(JBBTime.now().plusMinutes(i))
                    .build();

            invalidAttempts.add(entity);
        }
        return invalidAttempts;
    }

    private List<FailedSignInAttemptEntity> generateAllTooOldInvalidAttemptsEntries(int number) {
        List<FailedSignInAttemptEntity> invalidAttempts = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            FailedSignInAttemptEntity entity = FailedSignInAttemptEntity.builder()
                    .memberId(1L)
                    .attemptDateTime(JBBTime.now().minusMinutes(10 + i))
                    .build();

            invalidAttempts.add(entity);
        }
        return invalidAttempts;
    }

    public List<FailedSignInAttemptEntity> getEmptyInvalidSignInList() {
        return Lists.newArrayList();
    }

    public List<FailedSignInAttemptEntity> getInvalidsAttemptsForUser(int number) {
        List<FailedSignInAttemptEntity> invalidAttempts = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            FailedSignInAttemptEntity entity = FailedSignInAttemptEntity.builder()
                    .memberId(1L)
                    .attemptDateTime(JBBTime.now().plusMinutes(i))
                    .build();

            invalidAttempts.add(entity);
        }
        return invalidAttempts;
    }

    public Optional<MemberLockEntity> getUserLockEntity(LocalDateTime localDateTime) {
        return Optional.of(MemberLockEntity.builder()
                .memberId(1L)
                .expirationDate(localDateTime)
                .build()
        );
    }
}
