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

        if (isLockServiceIsAvailable()) {
            Optional<InvalidSignInAttemptEntity> entity = invalidSignInAttemptRepository.findByUsername(username);
            if (entity.isPresent()) {
                log.debug("User {} contains entity in InvalidSignInRepository: {}", username, entity);
                if (entity.isPresent() && !checkIfUserWrongSignInAttemptTimeExpired(entity)) {
                    entity = updateEntity(entity);
                    invalidSignInAttemptRepository.save(entity.get());

                    if (isSystemShouldLockUserAccount(username)) {
                        log.debug("System will lock user {} account", username.getValue());
                        invalidSignInAttemptRepository.delete(entity.get());
                        saveEntityInUserLockRepository(username);
                    }
                } else {
                    log.debug("Invalid Sign In Attempts Measurement time for user {} is expired. User is deleting from InvalidSignInRepository", username);
                    invalidSignInAttemptRepository.delete(entity.get());
                }
            } else {
                saveEntityInInvalidSignInRepository(username);
            }
        }
    }

    private Optional<InvalidSignInAttemptEntity> updateEntity(Optional<InvalidSignInAttemptEntity> entity) {
        entity.map(wholeEntity -> {
            wholeEntity.setInvalidSignInAttempt(wholeEntity.getInvalidSignInAttempt() + 1);
            wholeEntity.setLastInvalidAttemptDateTime(LocalDateTime.now());
            return wholeEntity;
        });

        return entity;
    }

    private boolean checkIfUserWrongSignInAttemptTimeExpired(Optional<InvalidSignInAttemptEntity> entity) {
        return LocalDateTime.now().isAfter(entity.get().getInvalidAttemptsCounterExpire()) ||
                LocalDateTime.now().isEqual(entity.get().getInvalidAttemptsCounterExpire());
    }

    @Override
    public void releaseLockIfPresentAndQualified(Username username) {

        boolean lockShouldBeReleased = userLockRepository.findByUsername(username)
                .map(userLockEntity ->
                        ChronoUnit.MINUTES.between(userLockEntity.getLocalDateTimeWhenLockWasRaised(), userLockEntity.getLocalDateTimeWhenLockShouldBeReleased()))
                .filter(result -> result >= properties.userSignInLockTimePeriod())
                .isPresent();

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

        InvalidSignInAttemptEntity signInAttemptEntity = InvalidSignInAttemptEntity.builder()
                .firstInvalidAttemptDateTime(LocalDateTime.now())
                .invalidAttemptsCounterExpire(LocalDateTime.now().plusMinutes(properties.userSignInLockMeasurementTimePeriod()))
                .invalidSignInAttempt(1)
                .lastInvalidAttemptDateTime(LocalDateTime.now())
                .username(username)
                .build();

        invalidSignInAttemptRepository.save(signInAttemptEntity);
    }

    private void saveEntityInUserLockRepository(Username username) {
        UserLockEntity userLockEntity = UserLockEntity.builder()
                .localDateTimeWhenLockWasRaised(LocalDateTime.now())
                .localDateTimeWhenLockShouldBeReleased(getAccountLockReleasedTime())
                .username(username)
                .build();

        userLockRepository.save(userLockEntity);
    }

    private boolean isSystemShouldLockUserAccount(Username username) {
        return !isUserHasAccountLock(username)
                && isUserExceedInvalidSignInAttempt(username);
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

}
