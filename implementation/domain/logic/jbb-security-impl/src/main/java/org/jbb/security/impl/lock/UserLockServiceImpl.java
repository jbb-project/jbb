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

@Component
public class UserLockServiceImpl implements UserLockService {

    @Autowired
    private UserLockProperties properties;

    @Autowired
    private UserLockRepository userLockRepository;

    @Autowired
    private InvalidSignInAttemptRepository invalidSignInAttemptRepository;


    @Override
    public void lockUser(Username username) {

        if (isLockServiceIsAvailable() && !isUserHasAccountLock(username)) {
            invalidSignInAttemptRepository.removeAllEntiriesWhereUsernameIsEqual(username);
            saveUserEntity(username);
        }
    }

    @Override
    public boolean isUserHasAccountLock(Username username) {
        if (isLockServiceIsAvailable())
            return userLockRepository.findByUsername(username).isPresent();
        else
            return false;
    }

    private void saveUserEntity(Username username) {
        UserLockEntity userLockEntity = UserLockEntity.builder()
                .localDateTime(getLockEndTime())
                .username(username)
                .build();

        userLockRepository.save(userLockEntity);
    }

    private LocalDateTime getLockEndTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTime.plusMinutes(Long.valueOf(properties.userSignInLockTimePeriod()));
    }

    private boolean isLockServiceIsAvailable() {
        return Boolean.valueOf(properties.userSignInLockServiceEnable());
    }
}
