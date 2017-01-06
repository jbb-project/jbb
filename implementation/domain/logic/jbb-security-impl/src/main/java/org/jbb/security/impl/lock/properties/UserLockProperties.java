/*
 * Copyright (C) 2017 the original author or authors.
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

    String USER_SIGN_IN_ATTEMPT = "user.lock.maximum.sign.in.attempt";
    String USER_LOCK_TIME_PERIOD = "user.lock.time.period.in.minutes";
    String USER_LOCK_WRONG_ATTEMPT_MEASUREMENT_TIME_PERIOD = "user.lock.wrong.attempt.measurement.time.period";
    String USER_LOCK_SERVICE_AVAILABLE = "user.lock.enable";


    @Key(USER_SIGN_IN_ATTEMPT)
    int userSignInMaximumAttempt();

    @Key(USER_LOCK_TIME_PERIOD)
    long userSignInLockTimePeriod();

    @Key(USER_LOCK_WRONG_ATTEMPT_MEASUREMENT_TIME_PERIOD)
    long userSignInLockMeasurementTimePeriod();

    @Key(USER_LOCK_SERVICE_AVAILABLE)
    boolean userSignInLockServiceEnable();


}
