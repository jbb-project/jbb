/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.lockout;

import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.MemberLock;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemberLockRowTranslator {

    private final MemberService memberService;

    public MemberLockRow toRow(MemberLock memberLock) {
        try {
            return MemberLockRow.builder()
                .displayedName(memberService.getMemberWithIdChecked(memberLock.getMemberId())
                    .getDisplayedName().getValue())
                .active(memberLock.isActive())
                .createDateTime(memberLock.getCreateDateTime())
                .deactivationDateTime(memberLock.getDeactivationDateTime())
                .expirationDateTime(memberLock.getExpirationDateTime())
                .build();
        } catch (MemberNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

}
