/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock.properties;


import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Config.Sources({"file:${jbb.home}/jbb-security.properties"})
public interface UserLockProperties extends ModuleProperties { //NOSONAR

    String MAXIMUM_USER_SIGN_IN_ATTEMPT_TO_LOCK_ACCOUNT = "user.lock.maximum.sign.in.attempt";

    String USER_LOCK_TIME_PERIOD = "user.lock.time.period.in.minutes";

    String WRONG_USER_LOCK_MEASUREMENT_TIME_PERIOD = "user.lock.time.period.in.minutes";

    String USER_LOCK_SERVICE_ENABLE = "user.lock.enable";


    @Key(MAXIMUM_USER_SIGN_IN_ATTEMPT_TO_LOCK_ACCOUNT)
    String userSignInMaximumAttempt();

    @Key(USER_LOCK_TIME_PERIOD)
    String userSignInLockTimePeriod();

    @Key(WRONG_USER_LOCK_MEASUREMENT_TIME_PERIOD)
    String userSignInLockMeasurementTimePeriod();

    @Key(USER_LOCK_SERVICE_ENABLE)
    String userSignInLockServiceEnable();

}
