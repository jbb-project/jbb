/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock.service;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import org.jbb.lib.core.vo.Username;
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.InvalidSignInAttemptEntity;
import org.jbb.security.impl.lock.model.UserLockEntity;
import org.jbb.security.impl.lock.properties.UserLockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserLockServiceImpl implements UserLockService {

    @Autowired
    private UserLockProperties properties;

    @Autowired
    private UserLockRepository userLockRepository;

    @Autowired
    private InvalidSignInAttemptRepository invalidSignInAttemptRepository;


    @Override
    public void lockUserIfQualify(Username username) {
        clearTooOldInvalidAttemptsForAllUsers();

        if (isLockServiceIsAvailable()) {
            if (isSystemShouldLockUserAccount(username)) {
                log.debug("System will lock user {} account", username.getValue());
                invalidSignInAttemptRepository.clearInvalidSigInAttemptForSpecifyUser(username);
                saveEntityInUserLockRepository(username);
            }
            saveEntityInInvalidSignInRepository(username);
        }
    }

    @Override
    public void releaseLockIfPresentAndQualified(Username username) {
        clearTooOldInvalidAttemptsForAllUsers();

        boolean lockShouldBeReleased = userLockRepository.findByUsername(username)
                .map(userLockEntity ->
                        ChronoUnit.MINUTES.between(userLockEntity.getLocalDateTimeWhenLockWasRaised(), userLockEntity.getLocalDateTimeWhenLockShouldBeReleased()))
                .filter(result -> result >= properties.userSignInLockTimePeriod())
                .isPresent();

        invalidSignInAttemptRepository.findByUsername(username)
                .ifPresent(entity -> {
                    log.debug("Invalid attempts for user {} are cleared", username.getValue());
                    invalidSignInAttemptRepository.delete(entity);
                });

        if (lockShouldBeReleased) {
            log.debug("Account lock for user {} is released", username.getValue());
            userLockRepository.delete(userLockRepository.findByUsername(username).get());
        }
    }

    @Override
    public boolean isUserHasAccountLock(Username username) {
        return userLockRepository.findByUsername(username).isPresent();
    }

    @Override
    public int getUserInvalidSignInAttempts(Username username) {
        return invalidSignInAttemptRepository.getNumberOfInvalidSignInAttempts(username);
    }

    private void saveEntityInInvalidSignInRepository(Username username) {
        log.debug("Invalid sign in attempt will be added to user {}", username.getValue());
        Optional<InvalidSignInAttemptEntity> invalidSignInAttemptRepositoryByUsername = invalidSignInAttemptRepository.findByUsername(username);
        InvalidSignInAttemptEntity invalidSignInAttemptEntity = null;

        if (invalidSignInAttemptRepositoryByUsername.isPresent()) {
            log.debug("Founded invalid sign in attempt in repository for user {}." +
                    " System will increment invalid attempt value and update last invalid sign in date time", username.getValue());
            invalidSignInAttemptRepositoryByUsername.get()
                    .setInvalidSignInAttempt(invalidSignInAttemptRepositoryByUsername.get().getInvalidSignInAttempt() + 1);
            invalidSignInAttemptRepositoryByUsername.get()
                    .setLastInvalidAttemptDateTime(LocalDateTime.now());

            invalidSignInAttemptEntity = invalidSignInAttemptRepositoryByUsername.get();
        } else {
            log.debug("First invalid sign in attempt for user ", username.getValue());
            invalidSignInAttemptEntity = InvalidSignInAttemptEntity.builder()
                    .firstInvalidAttemptDateTime(LocalDateTime.now())
                    .lastInvalidAttemptDateTime(LocalDateTime.now())
                    .username(username)
                    .invalidSignInAttempt(1)
                    .build();
        }

        invalidSignInAttemptRepository.save(invalidSignInAttemptEntity);
    }

    private void clearTooOldInvalidAttemptsForAllUsers() {
        ImmutableList<InvalidSignInAttemptEntity> attemptsToRemove = FluentIterable.from(invalidSignInAttemptRepository.findAll())
                .filter(entity -> ChronoUnit.MINUTES.between(entity.getFirstInvalidAttemptDateTime(), LocalDateTime.now()) >= properties.userSignInLockMeasurementTimePeriod())
                .toList();

        invalidSignInAttemptRepository.delete(attemptsToRemove);
    }

    private boolean isSystemShouldLockUserAccount(Username username) {
        return !isUserHasAccountLock(username)
                && isUserExceedInvalidSignInAttempt(username)
                && isUserExceedInvalidSingInAttemptsInPeriodOfTime(username);
    }

    private void saveEntityInUserLockRepository(Username username) {
        UserLockEntity userLockEntity = UserLockEntity.builder()
                .localDateTimeWhenLockWasRaised(LocalDateTime.now())
                .localDateTimeWhenLockShouldBeReleased(getAccountLockReleasedTime())
                .username(username)
                .build();

        userLockRepository.save(userLockEntity);
    }

    private LocalDateTime getAccountLockReleasedTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusMinutes(properties.userSignInLockTimePeriod());
    }

    private boolean isLockServiceIsAvailable() {
        return properties.userSignInLockServiceEnable();
    }

    private boolean isUserExceedInvalidSignInAttempt(Username username) {
        return invalidSignInAttemptRepository.findByUsername(username)
                .map(invalidSignInAttemptEntity -> invalidSignInAttemptEntity.getInvalidSignInAttempt())
                .filter(invalidAttemptValue -> invalidAttemptValue >= properties.userSignInMaximumAttempt())
                .isPresent();
    }

    private boolean isUserExceedInvalidSingInAttemptsInPeriodOfTime(Username username) {
        return invalidSignInAttemptRepository.findByUsername(username)
                .map(invalidSignInAttemptEntity ->
                        ChronoUnit.MINUTES.between(invalidSignInAttemptEntity.getFirstInvalidAttemptDateTime(), invalidSignInAttemptEntity.getLastInvalidAttemptDateTime()))
                .filter(result -> result >= properties.userSignInLockMeasurementTimePeriod())
                .isPresent();
    }
}
