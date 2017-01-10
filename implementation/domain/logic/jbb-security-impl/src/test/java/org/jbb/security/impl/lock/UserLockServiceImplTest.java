/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock;


import com.google.common.collect.Lists;

import org.jbb.lib.core.time.JBBTime;
import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.InvalidSignInAttemptEntity;
import org.jbb.security.impl.lock.model.UserLockEntity;
import org.jbb.security.impl.lock.properties.UserLockProperties;
import org.jbb.security.impl.lock.service.UserLockServiceImpl;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
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
    public void whenServiceIsOfflineAndUserExceedInvalidSignInAttemptsThenUserIsNotLocked() {

        //given
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(false);
        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(2);

        //when
        userLockService.lockUserIfQualify(1L);

        //then
        verify(userLockRepository, never()).save(any(UserLockEntity.class));
        verify(invalidSignInAttemptRepository, never()).save(any(InvalidSignInAttemptEntity.class));

    }

    @Test
    public void whenUserHasAttemptsAndUserHasFirstInvalidAttempts_AttemptsShouldBeSaveInDB(){

        //given
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(5);
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(10L);
        when(invalidSignInAttemptRepository.findAllWithSpecifyMember(1L)).thenReturn(getEmptyInvalidSignInList());
        when(userLockRepository.findByMemberID(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);

        //then
        verify(invalidSignInAttemptRepository, Mockito.times(3)).saveAndFlush(any(InvalidSignInAttemptEntity.class));

    }

    @Test
    public void whenUserHasInvalidAttemptsBefore_NextAttemptsShouldBeSaveInDB() {
        //given
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(5);
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(10L);
        when(invalidSignInAttemptRepository.findAllInvalidSignInAttemptOrderByDateAsc(1L)).thenReturn(getInvalidsAttemptsForUser(1));
        when(userLockRepository.findByMemberID(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);

        //then
        verify(invalidSignInAttemptRepository, Mockito.times(2)).saveAndFlush(any(InvalidSignInAttemptEntity.class));
    }


    @Test
    public void whenUserExceedInvalidAttemptsAndHeDoesNotHaveInvalidAttemptsBefore_AccountShouldBeLocked() {
        //given
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(2);
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(1L);
        when(userLockProperties.userSignInLockTimePeriod()).thenReturn(10L);
        when(invalidSignInAttemptRepository.findAllInvalidSignInAttemptOrderByDateAsc(1L)).thenReturn(getEmptyInvalidSignInList());
        when(invalidSignInAttemptRepository.findAllWithSpecifyMember(1L)).thenReturn(getInvalidsAttemptsForUser(3));
        when(userLockRepository.findByMemberID(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);


        //then
        verify(invalidSignInAttemptRepository, Mockito.times(3)).saveAndFlush(any(InvalidSignInAttemptEntity.class));
        verify(userLockRepository, Mockito.times(3)).saveAndFlush(any(UserLockEntity.class));
    }

    @Test
    public void whenUserExceedInvalidAttemptsAndHeHasInvalidAttemptsBefore_AccountShouldBeLocked() {

        //given
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(2);
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(1L);
        when(userLockProperties.userSignInLockTimePeriod()).thenReturn(10L);
        when(invalidSignInAttemptRepository.findAllInvalidSignInAttemptOrderByDateAsc(1L)).thenReturn(getInvalidsAttemptsForUser(2));
        when(invalidSignInAttemptRepository.findAllWithSpecifyMember(1L)).thenReturn(getInvalidsAttemptsForUser(3));
        when(userLockRepository.findByMemberID(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockUserIfQualify(1L);

        //then
        verify(invalidSignInAttemptRepository, Mockito.times(1)).saveAndFlush(any(InvalidSignInAttemptEntity.class));
        verify(userLockRepository, Mockito.times(1)).saveAndFlush(any(UserLockEntity.class));
    }


    @Test
    public void whenUserDoesNotHaveTooOldAttemptEntries_EntriesShouldNotBeDeleted(){

        //given
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(2);
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(5L);
        when(invalidSignInAttemptRepository.findAllInvalidSignInAttemptOrderByDateAsc(1L)).thenReturn(getInvalidsAttemptsForUser(2));
        when(userLockRepository.findByMemberID(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);

        //then
        verify(invalidSignInAttemptRepository, Mockito.times(0)).delete(any(ArrayList.class));
    }

    @Test
    public void whenUserHasAllTooOldAttemptEntries_EntriesShouldBeDeleted(){

        //given
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(2);
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(5L);
        when(invalidSignInAttemptRepository.findAllInvalidSignInAttemptOrderByDateAsc(1L)).thenReturn(generateAllTooOldInvalidAttemptsEntries(3));
        when(invalidSignInAttemptRepository.findAllWithSpecifyMember(1L)).thenReturn(generateAllTooOldInvalidAttemptsEntries(3));
        when(userLockRepository.findByMemberID(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);

        //then
        verify(invalidSignInAttemptRepository, Mockito.atLeast(3)).delete(any(ArrayList.class));
    }

    @Test
    public void whenUserHasTooOldAndNotTooOldAttemptEntries_OnlyTooOldEntriesShouldBeDeleted() {

        //given
        when(userLockProperties.userSignInLockServiceEnable()).thenReturn(true);
        when(userLockProperties.userSignInMaximumAttempt()).thenReturn(2);
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(5L);
        when(invalidSignInAttemptRepository.findAllInvalidSignInAttemptOrderByDateAsc(1L)).thenReturn(generateMixedInvalidSignInAttempts(2,2));
        when(userLockRepository.findByMemberID(1L)).thenReturn(Optional.empty());

        //when
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);
        userLockService.lockUserIfQualify(1L);

        verify(invalidSignInAttemptRepository, Mockito.atLeast(2)).delete(any(ArrayList.class));

    }


    @Test
    public void ifUserAccountBlockadeIsNotExpire_BlockadeShouldNotBeRemovedAndServiceShouldReturnTrue(){

        //given
        when(userLockProperties.userSignInLockMeasurementTimePeriod()).thenReturn(5L);
        when(userLockRepository.findByMemberID(1L)).thenReturn(getUserLockEntity(LocalDateTime.now().plusMinutes(5)));

        //when
        boolean userHasAccountLock = userLockService.isUserHasAccountLock(1L);

        //then
        assertTrue(userHasAccountLock);
        verify(userLockRepository,Mockito.times(0)).delete(any(UserLockEntity.class));
    }

    @Test
    public void ifUserAccountBlockadeIsExpire_BlockadeShouldBeRemovedAndServiceShouldReturnFalse(){

        //given
        when(userLockRepository.findByMemberID(1L)).thenReturn(getUserLockEntity(JBBTime.now().minusMinutes(30)));

        //when
        boolean userHasAccountLock = userLockService.isUserHasAccountLock(1L);

        //then
        assertFalse(userHasAccountLock);
        verify(userLockRepository,Mockito.times(1)).delete(any(UserLockEntity.class));
    }

    @Test
    public void whenUserHasLock_NewInvalidsAttemptAreNotSaveToDB() {

        //given
        when(userLockRepository.findByMemberID(1L)).thenReturn(getUserLockEntity(JBBTime.now()));

        //when
        userLockService.lockUserIfQualify(1L);

        //then
        verify(invalidSignInAttemptRepository, Mockito.times(0)).saveAndFlush(any(InvalidSignInAttemptEntity.class));
    }

    private List<InvalidSignInAttemptEntity> generateMixedInvalidSignInAttempts(int numberOfTooOld,int numberOfCorrect) {
        List<InvalidSignInAttemptEntity> invalidAttempts = new ArrayList<>();

        for(int i = 0; i<numberOfTooOld; i++) {
            InvalidSignInAttemptEntity entity = InvalidSignInAttemptEntity.builder()
                    .memberID(1L)
                    .invalidAttemptDateTime(JBBTime.now().minusMinutes(10 + i))
                    .build();

            invalidAttempts.add(entity);
        }
        for(int i = 0; i<numberOfCorrect; i++) {
            InvalidSignInAttemptEntity entity = InvalidSignInAttemptEntity.builder()
                    .memberID(1L)
                    .invalidAttemptDateTime(JBBTime.now().plusMinutes(i))
                    .build();

            invalidAttempts.add(entity);
        }
        return invalidAttempts;
    }

    private List<InvalidSignInAttemptEntity> generateAllTooOldInvalidAttemptsEntries(int number) {
        List<InvalidSignInAttemptEntity> invalidAttempts = new ArrayList<>();

        for(int i = 0; i<number; i++) {
            InvalidSignInAttemptEntity entity = InvalidSignInAttemptEntity.builder()
                    .memberID(1L)
                    .invalidAttemptDateTime(JBBTime.now().minusMinutes(10 + i))
                    .build();

            invalidAttempts.add(entity);
        }
        return invalidAttempts;
    }

    public List<InvalidSignInAttemptEntity> getEmptyInvalidSignInList() {
        return Lists.newArrayList();
    }

    public List<InvalidSignInAttemptEntity> getInvalidsAttemptsForUser(int number) {
        List<InvalidSignInAttemptEntity> invalidAttempts = new ArrayList<>();

        for(int i = 0; i<number; i++) {
            InvalidSignInAttemptEntity entity = InvalidSignInAttemptEntity.builder()
                    .memberID(1L)
                    .invalidAttemptDateTime(JBBTime.now().plusMinutes(i))
                    .build();

            invalidAttempts.add(entity);
        }
        return invalidAttempts;
    }

    public Optional<UserLockEntity> getUserLockEntity(LocalDateTime localDateTime) {
        return Optional.of(UserLockEntity.builder()
            .memberID(1L)
                .accountExpireDate(localDateTime)
                .build()
        );
    }
}
