/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.service;


import org.jbb.security.api.model.MemberLockoutSettings;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberLockoutService {

    void lockMemberIfQualify(Long memberId);

    boolean isMemberHasLock(Long memberId);

    MemberLockoutSettings getLockoutSettings();

    void setLockoutSettings(MemberLockoutSettings settings);

    Optional<LocalDateTime> getUserLockExpireTime(Long memberId);

    void releaseUserLock(Long memberId);

    void cleanFailedAttemptsForMember(Long memberId);

}
