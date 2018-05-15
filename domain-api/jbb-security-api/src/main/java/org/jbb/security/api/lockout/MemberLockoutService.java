/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.lockout;


import java.util.Optional;
import org.springframework.data.domain.Page;

public interface MemberLockoutService {

    void lockMemberIfQualify(Long memberId);

    boolean ifMemberHasActiveLock(Long memberId);

    Optional<MemberLock> getMemberActiveLock(Long memberId);

    void releaseMemberLock(Long memberId);

    void cleanFailedAttemptsForMember(Long memberId);

    void deleteAllMemberLocks(Long memberId);

    Page<MemberLock> getLocksWithCriteria(LockSearchCriteria criteria);

}
