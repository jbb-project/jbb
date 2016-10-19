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
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.UserLockEntity;
import org.jbb.security.impl.lock.properties.UserLockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class UserLockServiceImpl implements UserLockService {

    @Autowired
    private UserLockProperties properties;

    @Autowired
    private UserLockRepository userLockRepository;

    @Autowired
    private InvalidSignInAttemptRepository invalidSignInAttemptRepository;


    @Override
    public void lockUserIfQualify(Username username) {

        if (isSystemShouldLockUserAccount(username)) {
            invalidSignInAttemptRepository.removeAllEntiriesWhereUsernameIsEqual(username);
            saveUserEntity(username);
        }
    }

    @Override
    public void releaseLockFromSpecifyUser(Username username) {
        boolean shouldBeReleased = userLockRepository.findByUsername(username)
                .map(userLockEntity ->
                        ChronoUnit.MINUTES.between(userLockEntity.getLocalDateTimeWhenLockWasRaised(), userLockEntity.getLocalDateTimeWhenLockShouldBeReleased()))
                .filter(result -> result >= properties.userSignInLockTimePeriod())
                .isPresent();

        if (shouldBeReleased)
            userLockRepository.delete(userLockRepository.findByUsername(username).get());
    }

    private boolean isSystemShouldLockUserAccount(Username username) {
        return isLockServiceIsAvailable()
                && !isUserHasAccountLock(username)
                && isUserExceedInvalidSignInAttempt(username)
                && isUserExceedInvalidSingInAttemptsInPeriodOfTime(username);
    }

    @Override
    public boolean isUserHasAccountLock(Username username) {
        return userLockRepository.findByUsername(username).isPresent();
    }

    private void saveUserEntity(Username username) {
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
