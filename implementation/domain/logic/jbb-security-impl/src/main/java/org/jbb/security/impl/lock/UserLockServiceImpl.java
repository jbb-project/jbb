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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLockServiceImpl implements UserLockService {

    @Autowired
    private UserLockRepository userLockRepository;

    @Autowired
    private InvalidSignInAttemptRepository invalidSignInAttemptRepository;


    @Override
    public void lockUser(Username username) {

    }

    @Override
    public int invalidSpecifyUserSignInAttempts(Username username) {
        return 0;
    }

    @Override
    public void isUserHasAccountLock(Username username) {

    }

    @Override
    public void isLockForSpecifyUserCanBeReleased(Username username) {

    }
}
