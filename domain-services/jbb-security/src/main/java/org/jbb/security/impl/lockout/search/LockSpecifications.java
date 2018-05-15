/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.search;

import lombok.experimental.UtilityClass;
import org.jbb.security.impl.lockout.model.MemberLockEntity;
import org.jbb.security.impl.lockout.model.MemberLockEntity_;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public final class LockSpecifications {

    public static Specification<MemberLockEntity> withMemberId(Long memberId) {
        if (memberId != null) {
            return (root, cq, cb) ->
                cb.equal(root.get(MemberLockEntity_.memberId), memberId);
        } else {
            return null;
        }
    }

    public static Specification<MemberLockEntity> withActive(Boolean active) {
        if (active != null) {
            return (root, cq, cb) ->
                cb.equal(root.get(MemberLockEntity_.active), active);
        } else {
            return null;
        }
    }

}
