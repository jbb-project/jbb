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


import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.UserLockEntity;
import org.jbb.security.impl.lock.properties.UserLockProperties;
import org.jbb.security.impl.lock.service.UserLockServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
        UserLockEntity user = createMemberWithExceedInvalidSignInAttempts();

    }

    private UserLockEntity createMemberWithExceedInvalidSignInAttempts() {
        return null;
    }

    private UserLockEntity createMemberWithEqualsInvalidSignInAttemptsToPropertiesValue() {
        return null;
    }

    private UserLockEntity createMemberWithNotExceedInvalidSignInAttempts() {
        return null;
    }
}
