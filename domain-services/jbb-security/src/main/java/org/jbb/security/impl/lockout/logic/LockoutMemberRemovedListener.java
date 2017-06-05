/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.logic;

import com.google.common.eventbus.Subscribe;

import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LockoutMemberRemovedListener {
    private final MemberLockoutService lockoutService;

    @Autowired
    public LockoutMemberRemovedListener(MemberLockoutService lockoutService, JbbEventBus eventBus) {
        this.lockoutService = lockoutService;
        eventBus.register(this);
    }

    @Subscribe
    @Transactional
    public void removeLockAndFailedAttempts(MemberRemovedEvent event) {
        Long memberId = event.getMemberId();

        log.debug("MemberRemovedEvent received. Try to remove lock entity and/or failed signin attempts entity for member id {} (if applicable)", memberId);
        lockoutService.cleanFailedAttemptsForMember(memberId);
        lockoutService.releaseMemberLock(memberId);
    }
}
