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

import org.jbb.security.impl.lock.dao.InvalidSignInAttemptRepository;
import org.jbb.security.impl.lock.dao.UserLockRepository;
import org.jbb.security.impl.lock.model.InvalidSignInAttemptEntity;
import org.jbb.security.impl.lock.model.UserLockEntity;
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

    @InjectMocks
    private UserLockServiceImpl userLockService;

    @Test
    public void whenUserHasMoreInvalidSignInAttemptsThenPropertiesAllowToThenUserAccountShouldBeLocked() {

    }

    @Test
    public void whenUserHasMoreInvalidSignInAttemptsInPeriodOfTimeThenUserAccountShouldBeLocked() {

    }

    @Test
    public void whenUserHasLockedAccountThenServiceShouldReturnTrue() {

    }

    @Test
    public void whenUserHasNotLockedAccountThenServiceShouldReturnFalse() {

    }

    @Test
    public void whenUserIsNotQualifyToLockAccountThenHisAccountShouldNotBeLocked() {

    }

    @Test
    public void whenUserIsQualifyToReleaseLockThenLockShouldBeReleased() {

    }

    private UserLockEntity createUserLockEntity() {
        return null;
    }

    private InvalidSignInAttemptEntity createInvalidSignInAttemptEntity() {
        return null;
    }


}
