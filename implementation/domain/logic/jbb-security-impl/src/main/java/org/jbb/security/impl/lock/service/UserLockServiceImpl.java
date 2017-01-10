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
import org.jbb.lib.db.DbConfig;
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.InvalidSignInAttemptEntity;
import org.jbb.security.impl.lock.model.UserLockEntity;
import org.jbb.security.impl.lock.properties.UserLockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
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

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED,transactionManager = DbConfig.JPA_MANAGER_BEAN_NAME)
    public void lockUserIfQualify(Long memberID) {
        Validate.notNull(memberID, "Member ID cannot be null");

        if (isServiceAvailable() && !isUserHasAccountLock(memberID)) {
            removeOldEntriesFromInvalidSignInRepositoryIfNeeded(memberID);
            addInvalidSignInAttempt(memberID);
            lockUserIfNeeded(memberID);
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, transactionManager = DbConfig.JPA_MANAGER_BEAN_NAME)
    public boolean isUserHasAccountLock(Long memberID) {
        Optional<UserLockEntity> userLockEntity = userLockRepository.findByMemberID(memberID);
        boolean hasLock = false;
        if (userLockEntity.isPresent()) {
            if (calculateIfLockShouldBeRemoved(userLockEntity.get())) {
                userLockRepository.delete(userLockEntity.get());
                log.debug("Account lock for member with ID {} is removed", memberID);
            } else
                hasLock = true;
        }
        log.debug("UserLockService response about user account lock for member with ID {} is: ", Boolean.toString(hasLock).toUpperCase());
        return hasLock;
    }

    @Override
    public void setPropertiesValue(String propertiesKey, String propertiesValue) {
        Validate.notEmpty(propertiesKey, "Property key cannot be empty");
        Validate.notEmpty(propertiesValue, "Property value cannot be empty");

        Set<String> propertyNames = properties.propertyNames();
        propertyNames.stream()
                .filter(property -> property.equals(propertiesKey))
                .findFirst()
                .ifPresent(foundedValue -> properties.setProperty(propertiesKey, propertiesValue));

    }

    private void removeOldEntriesFromInvalidSignInRepositoryIfNeeded(Long memberID) {
        LocalDateTime now = JBBTime.now();
        LocalDateTime dateCeiling = calculateDateCeilingWhereYoungerEntriesShouldBeDeleted(memberID, now);

        if (now.isAfter(dateCeiling)) {
            log.debug("Some entries for user {} are qualified to be delete.All entries for that user before {} will be deleted", memberID, dateCeiling);
            removeTooOldEntriesFromInvalidSignInRepository(memberID);
        }
    }


    private void lockUserIfNeeded(Long memberID) {
        if (invalidSignInAttemptRepository.findAllWithSpecifyMember(memberID).size() >= properties.userSignInMaximumAttempt()) {
            buildAndSaveUserLock(memberID);
            invalidSignInAttemptRepository.deleteAllInvalidAttemptsForSpecifyUser(memberID);
        }
    }

    private void removeTooOldEntriesFromInvalidSignInRepository(Long memberID) {
        LocalDateTime boundaryDateToBeRemove = JBBTime.now().minusMinutes(properties.userSignInLockMeasurementTimePeriod());
        List<InvalidSignInAttemptEntity> signInAttemptEntityList = invalidSignInAttemptRepository.findAllWithSpecifyMember(memberID);
        List<InvalidSignInAttemptEntity> entitiesToRemove = signInAttemptEntityList.stream()
                .filter(invalidSignInAttemptEntity -> invalidSignInAttemptEntity.getInvalidAttemptDateTime().isBefore(boundaryDateToBeRemove) ||
                        invalidSignInAttemptEntity.getInvalidAttemptDateTime().isEqual(boundaryDateToBeRemove))
                .collect(Collectors.toList());

        invalidSignInAttemptRepository.delete(entitiesToRemove);
        invalidSignInAttemptRepository.flush();
    }

    private LocalDateTime calculateDateCeilingWhereYoungerEntriesShouldBeDeleted(Long memberID, LocalDateTime now) {
        List<InvalidSignInAttemptEntity> invalidSignInAttemptEntities = invalidSignInAttemptRepository.findAllInvalidSignInAttemptOrderByDateAsc(memberID);

        if(invalidSignInAttemptEntities.isEmpty())
            return now;
        else
            return invalidSignInAttemptEntities.get(0).getInvalidAttemptDateTime().plusMinutes(properties.userSignInLockMeasurementTimePeriod());

    }

    private boolean isServiceAvailable() {
        return properties.userSignInLockServiceEnable();
    }

    private void buildAndSaveUserLock(Long memberID) {
        UserLockEntity entity = UserLockEntity.builder()
                .memberID(memberID)
                .accountExpireDate(calculateLockExpireDate())
                .build();

        userLockRepository.saveAndFlush(entity);
        log.debug("Account for user with id {} is locked. Lock expires time is {}", memberID, entity.getAccountExpireDate());
    }

    private LocalDateTime calculateLockExpireDate() {
        LocalDateTime localDateTime = JBBTime.now();
        return localDateTime.plusMinutes(properties.userSignInLockTimePeriod());
    }

    private void addInvalidSignInAttempt(Long memberID) {
        InvalidSignInAttemptEntity entity = InvalidSignInAttemptEntity.builder()
                .memberID(memberID)
                .invalidAttemptDateTime(JBBTime.now())
                .build();

        invalidSignInAttemptRepository.saveAndFlush(entity);
        log.debug("Invalid sign in attempt for user {}", memberID);
    }

    public boolean calculateIfLockShouldBeRemoved(UserLockEntity userLockEntity) {
        LocalDateTime accountLockExpireDate = userLockEntity.getAccountExpireDate();
        return JBBTime.now().isAfter(accountLockExpireDate) || JBBTime.now().isEqual(accountLockExpireDate);
    }
}
