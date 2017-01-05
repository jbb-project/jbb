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

import lombok.extern.slf4j.Slf4j;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime dateCeiling = calculateDateCeiling(memberID,now);

            if (now.isAfter(dateCeiling))
                remove(memberID);

            addInvalidSignInAttemptToUser(memberID);

            if (invalidSignInAttemptRepository.findAllWithSpecifyMember(memberID).size() >= properties.userSignInMaximumAttempt()) {
                lockUser(memberID);
                invalidSignInAttemptRepository.deleteAllInvalidAttemptsForSpecifyUser(memberID);
            }
        }
    }

    @Override
    public boolean isUserHasAccountLock(Long memberID) {
        Optional<UserLockEntity> byMemberID = userLockRepository.findByMemberID(memberID);

        return releaseLockIfPresentAndQualified(byMemberID);
    }

    private void remove(Long memberID) {
        LocalDateTime boundaryDateToBeRemove = LocalDateTime.now().minusMinutes(properties.userSignInLockMeasurementTimePeriod());
        List<InvalidSignInAttemptEntity> signInAttemptEntityList = invalidSignInAttemptRepository.findAllWithSpecifyMember(memberID);
        List<InvalidSignInAttemptEntity> entitiesToRemove = signInAttemptEntityList.stream()
                .filter(invalidSignInAttemptEntity -> invalidSignInAttemptEntity.getInvalidAttemptDateTime().isBefore(boundaryDateToBeRemove) ||
                        invalidSignInAttemptEntity.getInvalidAttemptDateTime().isEqual(boundaryDateToBeRemove))
                .collect(Collectors.toList());

        invalidSignInAttemptRepository.delete(entitiesToRemove);
    }

    private LocalDateTime calculateDateCeiling(Long memberID, LocalDateTime now) {
        List<InvalidSignInAttemptEntity> invalidSignInAttemptEntities = invalidSignInAttemptRepository.findAllInvalidSignInAttemptOrderByDateAsc(memberID);

        if(invalidSignInAttemptEntities.isEmpty())
            return now;
        else
            return invalidSignInAttemptEntities.get(0).getInvalidAttemptDateTime().plusMinutes(properties.userSignInLockMeasurementTimePeriod());

    }

    private boolean releaseLockIfPresentAndQualified(Optional<UserLockEntity> byMemberID) {
        boolean isStillLocked = true;
        LocalDateTime accountLockExpireDate = byMemberID.get().getAccountExpireDate();
        if (LocalDateTime.now().isAfter(accountLockExpireDate) || LocalDateTime.now().isEqual(accountLockExpireDate)) {
            userLockRepository.delete(byMemberID.get());
            isStillLocked = false;
        }
        return isStillLocked;
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
