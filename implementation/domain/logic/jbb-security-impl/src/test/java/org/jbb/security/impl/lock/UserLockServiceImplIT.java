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
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CleanHsqlDbAfterTestsConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.impl.MemberConfigMocks;
import org.jbb.security.impl.SecurityConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CoreConfigMocks.class, CleanHsqlDbAfterTestsConfig.class,
        SecurityConfig.class, PropertiesConfig.class,
        EventBusConfig.class, DbConfig.class, MemberConfigMocks.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserLockServiceImplIT {

    @Autowired
    private UserLockService userLockService;

    private Username username;

    @Before
    public void init() {

    }

    @After
    public void clean() {

    }

    @Test
    public void whenUserDoesNotHaveInvalidSignInAttempts_UserCanSignIn() {

    }

    @Test
    public void whenUserHasMoreInvalidSignInAttemptsThenPossible_UserHasLock_UserCanNotSignIn() {

    }

    @Test
    public void whenUserHasInvalidSignInAttemptsButLessThenPossible_UserCanSignIn() {

    }

    @Test
    public void afterLockTimePeriodUserDoesNotHaveAccountLockAndHeCanSignIn() {

    }

    @Test
    public void whenUserExceedInvalidSignInAttemptThenPossibleButAfterMeasurementPeriod_UserCanSignIn() {

    }

    @Test
    public void whenServiceIsDisableUserCouldHaveInvalidSignInAttempts() {

    }


}
