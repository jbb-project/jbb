/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout;

import org.jbb.security.api.lockout.MemberLock;
import org.jbb.security.impl.lockout.model.MemberLockEntity;
import org.springframework.stereotype.Component;

@Component
public class MemberLockDomainTranslator {

    public MemberLock toModel(MemberLockEntity entity) {
        return MemberLock.builder()
            .memberId(entity.getMemberId())
            .active(entity.getActive())
            .createDateTime(entity.getCreateDateTime())
            .expirationDateTime(entity.getExpirationDate())
            .deactivationDateTime(entity.getDeactivationDate())
            .build();
    }

}
