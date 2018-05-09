/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.lockout;

import org.jbb.security.api.lockout.MemberLock;
import org.springframework.stereotype.Component;

@Component
public class MemberLockTranslator {

    public MemberLockDto toDto(MemberLock lock, Long id) {
        return MemberLockDto.builder()
                .memberId(id)
                .expirationDate(lock.getExpirationDate())
                .build();
    }

}