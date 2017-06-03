/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.properties;


import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Config.Sources({"file:${jbb.home}/jbb-security.properties"})
public interface MemberLockProperties extends ModuleProperties { //NOSONAR

    String MEMBER_LOCKOUT_ENABLED = "member.lockout.enabled";
    String MEMBER_LOCKOUT_DURATION_MINUTES = "member.lockout.duration";
    String MEMBER_LOCKOUT_ATTEMPTS_THRESHOLD = "member.lockout.attempts.threshold";
    String MEMBER_LOCKOUT_ATTEMPTS_EXPIRATION = "member.lockout.attempts.expiration";

    @Key(MEMBER_LOCKOUT_ENABLED)
    boolean lockoutEnabled();

    @Key(MEMBER_LOCKOUT_DURATION_MINUTES)
    long lockoutDurationMinutes();

    @Key(MEMBER_LOCKOUT_ATTEMPTS_THRESHOLD)
    int failedAttemptsThreshold();

    @Key(MEMBER_LOCKOUT_ATTEMPTS_EXPIRATION)
    long failedAttemptsExpirationMinutes();


}
