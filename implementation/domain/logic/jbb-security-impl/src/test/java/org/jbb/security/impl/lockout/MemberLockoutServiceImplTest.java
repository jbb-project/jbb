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
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.model.MemberLock;
import org.jbb.security.api.model.MemberLockoutSettings;
import org.jbb.security.event.MemberLockedEvent;
import org.jbb.security.event.MemberUnlockedEvent;
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
    private FailedSignInAttemptRepository failedSignInAttemptRepositoryMock;

    @Mock
    private MemberLockRepository memberLockRepositoryMock;

    @Mock
    private MemberLockProperties memberLockPropertiesMock;

    @Mock
    private JbbEventBus eventBusMock;

    @InjectMocks
    private MemberLockoutServiceImpl memberLockoutService;


    @Test
    public void getMemberLockServiceSettings() {

        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(1);
        when(memberLockPropertiesMock.lockoutDurationMinutes()).thenReturn(2L);
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(3L);

        //when
        MemberLockoutSettings userLockServiceSettings = memberLockoutService.getLockoutSettings();

        //then
        assertThat(userLockServiceSettings.getLockoutDurationMinutes()).isEqualTo(2L);
        assertThat(userLockServiceSettings.getFailedSignInAttemptsExpirationMinutes()).isEqualTo(3L);
        assertThat(userLockServiceSettings.isLockingEnabled()).isEqualTo(true);
        assertThat(userLockServiceSettings.getFailedAttemptsThreshold()).isEqualTo(1);
    }

    @Test
    public void whenReleaseLock_thenRemoveMemberLockFromDB_andSentEvent() {

        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(getMemberLockEntity(JBBTime.now()));

        //when
        memberLockoutService.releaseMemberLock(1L);

        //then
        verify(memberLockRepositoryMock, times(1)).delete(any(MemberLockEntity.class));
        verify(memberLockRepositoryMock, times(1)).flush();
        verify(eventBusMock, times(1)).post(any(MemberUnlockedEvent.class));
    }

    @Test
    public void whenLockoutIsDisabled_AndMemberExceedFailedSignInAttempts_thenMemberIsNotLocked() {

        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(false);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(2);

        //when
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        verify(memberLockRepositoryMock, never()).save(any(MemberLockEntity.class));
        verify(failedSignInAttemptRepositoryMock, never()).save(any(FailedSignInAttemptEntity.class));

    }

    @Test
    public void whenMemberHasAttemptsAndMemberHasFirstInvalidAttempts_AttemptsShouldBeSaveInDB() {

        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(5);
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(10L);
        when(failedSignInAttemptRepositoryMock.findAllForMember(1L)).thenReturn(getEmptyInvalidSignInList());
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepositoryMock, times(3)).saveAndFlush(any(FailedSignInAttemptEntity.class));

    }

    @Test
    public void whenMemberHasInvalidAttemptsBefore_NextAttemptsShouldBeSaveInDB() {
        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(5);
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(10L);
        when(failedSignInAttemptRepositoryMock.findAllForMemberOrderByDateAsc(1L)).thenReturn(getInvalidsAttemptsForMember(1));
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepositoryMock, times(2)).saveAndFlush(any(FailedSignInAttemptEntity.class));
    }


    @Test
    public void whenMemberExceedInvalidAttemptsAndHeDoesNotHaveInvalidAttemptsBefore_AccountShouldBeLocked() {
        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(1L);
        when(memberLockPropertiesMock.lockoutDurationMinutes()).thenReturn(10L);
        when(failedSignInAttemptRepositoryMock.findAllForMemberOrderByDateAsc(1L)).thenReturn(getEmptyInvalidSignInList());
        when(failedSignInAttemptRepositoryMock.findAllForMember(1L)).thenReturn(getInvalidsAttemptsForMember(3));
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);


        //then
        verify(failedSignInAttemptRepositoryMock, times(3)).saveAndFlush(any(FailedSignInAttemptEntity.class));
        verify(memberLockRepositoryMock, times(3)).saveAndFlush(any(MemberLockEntity.class));
    }

    @Test
    public void whenMemberExceedInvalidAttemptsAndHeHasInvalidAttemptsBefore_AccountShouldBeLocked_andEventSent() {

        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(1L);
        when(memberLockPropertiesMock.lockoutDurationMinutes()).thenReturn(10L);
        when(failedSignInAttemptRepositoryMock.findAllForMemberOrderByDateAsc(1L)).thenReturn(getInvalidsAttemptsForMember(2));
        when(failedSignInAttemptRepositoryMock.findAllForMember(1L)).thenReturn(getInvalidsAttemptsForMember(3));
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepositoryMock, times(1)).saveAndFlush(any(FailedSignInAttemptEntity.class));
        verify(memberLockRepositoryMock, times(1)).saveAndFlush(any(MemberLockEntity.class));
        verify(eventBusMock, times(1)).post(any(MemberLockedEvent.class));
    }


    @Test
    public void whenMemberDoesNotHaveTooOldAttemptEntries_EntriesShouldNotBeDeleted() {

        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(5L);
        when(failedSignInAttemptRepositoryMock.findAllForMemberOrderByDateAsc(1L)).thenReturn(getInvalidsAttemptsForMember(2));
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepositoryMock, times(0)).delete(any(ArrayList.class));
    }

    @Test
    public void whenMemberHasAllTooOldAttemptEntries_EntriesShouldBeDeleted() {

        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(5L);
        when(failedSignInAttemptRepositoryMock.findAllForMemberOrderByDateAsc(1L)).thenReturn(generateAllTooOldInvalidAttemptsEntries(3));
        when(failedSignInAttemptRepositoryMock.findAllForMember(1L)).thenReturn(generateAllTooOldInvalidAttemptsEntries(3));
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepositoryMock, Mockito.atLeast(3)).delete(any(ArrayList.class));
    }

    @Test
    public void whenMemberHasTooOldAndNotTooOldAttemptEntries_OnlyTooOldEntriesShouldBeDeleted() {

        //given
        when(memberLockPropertiesMock.lockoutEnabled()).thenReturn(true);
        when(memberLockPropertiesMock.failedAttemptsThreshold()).thenReturn(2);
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(5L);
        when(failedSignInAttemptRepositoryMock.findAllForMemberOrderByDateAsc(1L)).thenReturn(generateMixedInvalidSignInAttempts(2, 2));
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);
        memberLockoutService.lockMemberIfQualify(1L);

        verify(failedSignInAttemptRepositoryMock, Mockito.atLeast(2)).delete(any(ArrayList.class));
    }


    @Test
    public void ifMemberAccountBlockadeIsNotExpire_BlockadeShouldNotBeRemovedAndServiceShouldReturnTrue() {

        //given
        when(memberLockPropertiesMock.failedAttemptsExpirationMinutes()).thenReturn(5L);
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(getMemberLockEntity(LocalDateTime.now().plusMinutes(5)));

        //when
        boolean userHasAccountLock = memberLockoutService.isMemberHasLock(1L);

        //then
        assertTrue(userHasAccountLock);
        verify(memberLockRepositoryMock, times(0)).delete(any(MemberLockEntity.class));
    }

    @Test
    public void ifMemberAccountBlockadeIsExpire_BlockadeShouldBeRemovedAndServiceShouldReturnFalse() {

        //given
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(getMemberLockEntity(JBBTime.now().minusMinutes(30)));

        //when
        boolean userHasAccountLock = memberLockoutService.isMemberHasLock(1L);

        //then
        assertFalse(userHasAccountLock);
        verify(memberLockRepositoryMock, times(1)).delete(any(MemberLockEntity.class));
    }

    @Test
    public void whenMemberHasLock_NewInvalidsAttemptAreNotSaveToDB() {

        //given
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(getMemberLockEntity(JBBTime.now()));

        //when
        memberLockoutService.lockMemberIfQualify(1L);

        //then
        verify(failedSignInAttemptRepositoryMock, times(0)).saveAndFlush(any(FailedSignInAttemptEntity.class));
    }

    @Test
    public void whenMemberHasLock_LockShouldBeReturn() {

        //given
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(getMemberLockEntity(JBBTime.now()));

        //when
        Optional<MemberLock> memberLock = memberLockoutService.getMemberLock(1L);

        //then
        assertTrue(memberLock.isPresent());
    }

    @Test
    public void whenMemberHasNotLock_EmptyOptionalShouldBeReturn() {

        //given
        when(memberLockRepositoryMock.findByMemberId(1L)).thenReturn(Optional.empty());

        //when
        Optional<MemberLock> memberLock = memberLockoutService.getMemberLock(1L);

        //then
        assertFalse(memberLock.isPresent());

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

    public List<FailedSignInAttemptEntity> getInvalidsAttemptsForMember(int number) {
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

    public Optional<MemberLockEntity> getMemberLockEntity(LocalDateTime localDateTime) {
        return Optional.of(MemberLockEntity.builder()
                .memberId(1L)
                .expirationDate(localDateTime)
                .build()
        );
    }
}
