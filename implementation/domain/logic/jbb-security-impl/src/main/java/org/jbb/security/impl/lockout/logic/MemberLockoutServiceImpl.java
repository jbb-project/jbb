/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.logic;

import org.apache.commons.lang3.Validate;
import org.jbb.lib.core.time.JBBTime;
import org.jbb.security.api.model.MemberLock;
import org.jbb.security.api.model.MemberLockoutSettings;
import org.jbb.security.api.service.MemberLockoutService;
import org.jbb.security.impl.lockout.dao.FailedSignInAttemptRepository;
import org.jbb.security.impl.lockout.dao.MemberLockRepository;
import org.jbb.security.impl.lockout.data.MemberLockoutSettingsImpl;
import org.jbb.security.impl.lockout.model.FailedSignInAttemptEntity;
import org.jbb.security.impl.lockout.model.MemberLockEntity;
import org.jbb.security.impl.lockout.properties.MemberLockProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MemberLockoutServiceImpl implements MemberLockoutService {
    private final static String MEMBER_VALIDATION_MESSAGE = "Member ID cannot be null";

    private final MemberLockProperties properties;
    private final MemberLockRepository lockRepository;
    private final FailedSignInAttemptRepository failedAttemptRepository;

    @Autowired
    public MemberLockoutServiceImpl(MemberLockProperties properties,
                                    MemberLockRepository lockRepository,
                                    FailedSignInAttemptRepository failedAttemptRepository) {
        this.properties = properties;
        this.lockRepository = lockRepository;
        this.failedAttemptRepository = failedAttemptRepository;
    }

    @Override
    @Transactional
    public void lockMemberIfQualify(Long memberId) {
        Validate.notNull(memberId, MEMBER_VALIDATION_MESSAGE);

        if (isLockoutEnabled() && !isMemberHasLock(memberId)) {
            removeOldEntriesFromInvalidSignInRepositoryIfNeeded(memberId);
            addInvalidSignInAttempt(memberId);
            lockUserIfNeeded(memberId);
        }
    }

    @Override
    @Transactional
    public boolean isMemberHasLock(Long memberId) {
        Validate.notNull(memberId, MEMBER_VALIDATION_MESSAGE);

        Optional<MemberLockEntity> userLockEntity = lockRepository.findByMemberId(memberId);
        boolean hasLock = false;
        if (userLockEntity.isPresent()) {
            if (calculateIfLockShouldBeRemoved(userLockEntity.get())) {
                lockRepository.delete(userLockEntity.get());
                log.debug("Account lock for member with ID {} is removed", memberId);
            } else
                hasLock = true;
        }
        log.debug("MemberLockoutService response about user account lock for member with ID {} is: ", Boolean.toString(hasLock).toUpperCase());
        return hasLock;
    }

    @Override
    public MemberLockoutSettings getLockoutSettings() {
        MemberLockoutSettingsImpl settings = new MemberLockoutSettingsImpl();
        settings.setLockoutDuration(properties.lockoutDurationMinutes());
        settings.setFailedAttemptsExpiration(properties.failedAttemptsExpirationMinutes());
        settings.setFailedAttemptsThreshold(properties.failedAttemptsThreshold());
        settings.setEnabled(properties.lockoutEnabled());

        return settings;
    }

    @Override
    public void setLockoutSettings(MemberLockoutSettings settings) {

        log.debug("New values of UserLock Service properties: " + settings.toString());
        properties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ENABLED, Boolean.toString(settings.isEnabled()));
        properties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_DURATION_MINUTES, String.valueOf(settings.getLockoutDurationMinutes()));
        properties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ATTEMPTS_EXPIRATION, String.valueOf(settings.getFailedSignInAttemptsExpirationMinutes()));
        properties.setProperty(MemberLockProperties.MEMBER_LOCKOUT_ATTEMPTS_THRESHOLD, String.valueOf(settings.getFailedAttemptsThreshold()));

    }

    @Override
    public Optional<MemberLock> getMemberLock(Long memberId) {
        Validate.notNull(memberId, MEMBER_VALIDATION_MESSAGE);

        Optional<MemberLockEntity> lockOptional = lockRepository.findByMemberId(memberId);
        return Optional.ofNullable(lockOptional.orElse(null));
    }

    @Override
    @Transactional
    public void releaseMemberLock(Long memberId) {
        Validate.notNull(memberId, MEMBER_VALIDATION_MESSAGE);

        log.debug("Clean all data from repositories {} and {} for member {}", MemberLockRepository.class.getName(), FailedSignInAttemptRepository.class.getName(), memberId);

        Optional<MemberLockEntity> userLockEntity = lockRepository.findByMemberId(memberId);
        userLockEntity.ifPresent(entity -> {
            lockRepository.delete(entity);
            lockRepository.flush();
        });
    }

    @Override
    @Transactional
    public void cleanFailedAttemptsForMember(Long memberId) {
        Validate.notNull(memberId, MEMBER_VALIDATION_MESSAGE);

        log.debug("Remove all invalid attempts for member with id {}", memberId);
        failedAttemptRepository.deleteAllForMember(memberId);
    }

    private void removeOldEntriesFromInvalidSignInRepositoryIfNeeded(Long memberId) {
        LocalDateTime now = JBBTime.now();
        LocalDateTime dateCeiling = calculateDateCeilingWhereYoungerEntriesShouldBeDeleted(memberId, now);

        if (now.isAfter(dateCeiling)) {
            log.debug("Some entries for member {} are qualified to be delete .All entries for that member before {} will be deleted", memberId, dateCeiling);
            removeTooOldEntriesFromInvalidSignInRepository(memberId);
        }
    }


    private void lockUserIfNeeded(Long memberId) {
        if (failedAttemptRepository.findAllForMember(memberId).size() >= properties.failedAttemptsThreshold()) {
            buildAndSaveUserLock(memberId);
            failedAttemptRepository.deleteAllForMember(memberId);
        }
    }

    private void removeTooOldEntriesFromInvalidSignInRepository(Long memberId) {
        LocalDateTime boundaryDateToBeRemove = JBBTime.now().minusMinutes(properties.failedAttemptsExpirationMinutes());
        List<FailedSignInAttemptEntity> signInAttemptEntityList = failedAttemptRepository.findAllForMember(memberId);
        List<FailedSignInAttemptEntity> entitiesToRemove = signInAttemptEntityList.stream()
                .filter(failedSignInAttemptEntity -> failedSignInAttemptEntity.getAttemptDateTime().isBefore(boundaryDateToBeRemove) ||
                        failedSignInAttemptEntity.getAttemptDateTime().isEqual(boundaryDateToBeRemove))
                .collect(Collectors.toList());

        failedAttemptRepository.delete(entitiesToRemove);
        failedAttemptRepository.flush();
    }

    private LocalDateTime calculateDateCeilingWhereYoungerEntriesShouldBeDeleted(Long memberId, LocalDateTime dateTime) {
        List<FailedSignInAttemptEntity> invalidSignInAttemptEntities = failedAttemptRepository.findAllForMemberOrderByDateAsc(memberId);

        if (invalidSignInAttemptEntities.isEmpty())
            return dateTime;
        else
            return invalidSignInAttemptEntities.get(0).getAttemptDateTime().plusMinutes(properties.failedAttemptsExpirationMinutes());

    }

    private boolean isLockoutEnabled() {
        return properties.lockoutEnabled();
    }

    private void buildAndSaveUserLock(Long memberId) {
        MemberLockEntity entity = MemberLockEntity.builder()
                .memberId(memberId)
                .expirationDate(calculateLockExpireDate())
                .build();

        lockRepository.saveAndFlush(entity);
        log.debug("Account for member with id {} is locked. Lock expiration time is {}", memberId, entity.getExpirationDate());
    }

    private LocalDateTime calculateLockExpireDate() {
        LocalDateTime localDateTime = JBBTime.now();
        return localDateTime.plusMinutes(properties.lockoutDurationMinutes());
    }

    private void addInvalidSignInAttempt(Long memberId) {
        FailedSignInAttemptEntity entity = FailedSignInAttemptEntity.builder()
                .memberId(memberId)
                .attemptDateTime(JBBTime.now())
                .build();

        failedAttemptRepository.saveAndFlush(entity);
        log.debug("Invalid sign in attempt for member {}", memberId);
    }

    private boolean calculateIfLockShouldBeRemoved(MemberLockEntity memberLockEntity) {
        LocalDateTime accountLockExpireDate = memberLockEntity.getExpirationDate();
        return JBBTime.now().isAfter(accountLockExpireDate) || JBBTime.now().isEqual(accountLockExpireDate);
    }
}
