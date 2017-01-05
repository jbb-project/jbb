/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock.service;

import org.apache.commons.lang3.Validate;
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.InvalidSignInAttemptEntity;
import org.jbb.security.impl.lock.model.UserLockEntity;
import org.jbb.security.impl.lock.properties.UserLockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void lockUserIfQualify(Long memberID) {
        Validate.notNull(memberID, " Member ID cannot be null");

        if (isServiceAvailable()) {
            LocalDateTime dateCeiling = calculateDateCeiling(memberID);
            LocalDateTime now = LocalDateTime.now();

            if (now.isAfter(dateCeiling))
                remove(memberID, dateCeiling);

            addInvalidSignInAttemptToUser(memberID);

            if (invalidSignInAttemptRepository.findAllWithSpecifyMember(memberID).count() >= properties.userSignInMaximumAttempt())
                lockUser(memberID);
        }
    }

    private void remove(Long memberID, LocalDateTime dateCeiling) {
        LocalDateTime boundaryDateToBeRemove = LocalDateTime.now().minusMinutes(properties.userSignInLockMeasurementTimePeriod());
        List<InvalidSignInAttemptEntity> signInAttemptEntityList = invalidSignInAttemptRepository.findAllWithSpecifyMember(memberID).collect(Collectors.toList());
        List<InvalidSignInAttemptEntity> entitiesToRemove = signInAttemptEntityList.stream()
                .filter(invalidSignInAttemptEntity -> invalidSignInAttemptEntity.getInvalidAttemptDateTime().isBefore(boundaryDateToBeRemove) ||
                        invalidSignInAttemptEntity.getInvalidAttemptDateTime().isEqual(boundaryDateToBeRemove))
                .collect(Collectors.toList());

        invalidSignInAttemptRepository.delete(entitiesToRemove);
    }

    private LocalDateTime calculateDateCeiling(Long memberID) {
        Stream<InvalidSignInAttemptEntity> lastInvalidSignInStream = invalidSignInAttemptRepository.findAllInvalidSignInAttemptOrderByDateAsc(memberID);
        InvalidSignInAttemptEntity youngestInvalidAttempt = lastInvalidSignInStream.collect(Collectors.toList()).get(0);

        return youngestInvalidAttempt.getInvalidAttemptDateTime().plusMinutes(properties.userSignInLockMeasurementTimePeriod());
    }

    @Override
    public void releaseLockIfPresentAndQualified(Long memberID) {

    }

    @Override
    public boolean isUserHasAccountLock(Long memberID) {
        return false;
    }

    @Override
    public int getUserInvalidSignInAttempts(Long memberID) {
        return 0;
    }

    private boolean isServiceAvailable() {
        return properties.userSignInLockServiceEnable();
    }

    private void lockUser(Long memberID) {
        UserLockEntity entity = UserLockEntity.builder()
                .memberID(memberID)
                .accountExpireDate(calculateLockExpireDate())
                .build();

        userLockRepository.save(entity);
    }

    private LocalDateTime calculateLockExpireDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusMinutes(properties.userSignInLockTimePeriod());
    }

    private void addInvalidSignInAttemptToUser(Long memberID) {
        InvalidSignInAttemptEntity entity = InvalidSignInAttemptEntity.builder()
                .memberID(memberID)
                .invalidAttemptDateTime(LocalDateTime.now())
                .build();

        invalidSignInAttemptRepository.save(entity);
    }
}
