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

    @Key("user.lock.maximum.sign.in.attempt")
    long userSignInMaximumAttempt();

    @Key("user.lock.time.period.in.minutes")
    long userSignInLockTimePeriod();

    @Key("user.lock.wrong.attempt.measurement.time.period")
    long userSignInLockMeasurementTimePeriod();

    @Key("user.lock.enable")
    boolean userSignInLockServiceEnable();

}
