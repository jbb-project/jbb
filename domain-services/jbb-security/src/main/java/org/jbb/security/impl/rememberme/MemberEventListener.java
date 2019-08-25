/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.rememberme;

import com.google.common.eventbus.Subscribe;

import org.jbb.lib.eventbus.JbbEventBusListener;
import org.jbb.members.event.MemberRemovedEvent;
import org.jbb.security.impl.rememberme.dao.PersistentLoginRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventListener implements JbbEventBusListener {

    private final PersistentLoginRepository persistentLoginRepository;

    @Subscribe
    @Transactional
    public void removePersistentLogins(MemberRemovedEvent event) {
        log.debug("Remove 'remember me' tokens for removed member with id: {}",
            event.getMemberId());
        persistentLoginRepository.findByMemberId(event.getMemberId())
            .forEach(persistentLoginRepository::delete);
    }

}
