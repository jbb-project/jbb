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
import org.jbb.lib.core.time.JBBTime;
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.InvalidSignInAttemptEntity;
import org.jbb.security.impl.lock.model.UserLockEntity;
import org.jbb.security.impl.lock.properties.UserLockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private JBBTime jbbTime;

    @Override
    @Transactional
    public void lockUserIfQualify(Long memberID) {
        Validate.notNull(memberID, " Member ID cannot be null");

        if (isServiceAvailable()) {
            LocalDateTime now = JBBTime.now();
            LocalDateTime dateCeiling = calculateDateCeiling(memberID,now);

            if (now.isAfter(dateCeiling))
                remove(memberID);

            addInvalidSignInAttemptToUser(memberID);

            if (invalidSignInAttemptRepository.findAllWithSpecifyMember(memberID).size() + 1 >= properties.userSignInMaximumAttempt()) {
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

    @Override
    public void setPropertiesValue(String propertiesKey, String propertiesValue) {
        Validate.notEmpty(propertiesKey, " Property key cannot be empty");
        Validate.notEmpty(propertiesValue, " Property value cannot be empty");

        Set<String> propertyNames = properties.propertyNames();
        propertyNames.stream()
                .filter(property -> property.equals(propertiesKey))
                .findFirst()
                .ifPresent(foundedValue -> properties.setProperty(propertiesKey, propertiesValue));

    }

    private void remove(Long memberID) {
        LocalDateTime boundaryDateToBeRemove = JBBTime.now().minusMinutes(properties.userSignInLockMeasurementTimePeriod());
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
        if (byMemberID.isPresent()) {
            LocalDateTime accountLockExpireDate = byMemberID.get().getAccountExpireDate();
            if (JBBTime.now().isAfter(accountLockExpireDate) || JBBTime.now().isEqual(accountLockExpireDate)) {
                userLockRepository.delete(byMemberID.get());
                isStillLocked = false;
            }
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

        UserLockEntity save = userLockRepository.save(entity);
        System.out.println(save);
    }

    private LocalDateTime calculateLockExpireDate() {
        LocalDateTime localDateTime = JBBTime.now();
        return localDateTime.plusMinutes(properties.userSignInLockTimePeriod());
    }

    private void addInvalidSignInAttemptToUser(Long memberID) {
        InvalidSignInAttemptEntity entity = InvalidSignInAttemptEntity.builder()
                .memberID(memberID)
                .invalidAttemptDateTime(JBBTime.now())
                .build();

        invalidSignInAttemptRepository.save(entity);
    }
}
