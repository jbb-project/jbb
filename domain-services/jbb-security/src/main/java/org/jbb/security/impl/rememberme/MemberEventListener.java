/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.rememberme;

import com.google.common.eventbus.Subscribe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.lib.eventbus.JbbEventBusListener;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.event.MemberRemovedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventListener implements JbbEventBusListener {

    private final DatabaseTokenRepository databaseTokenRepository;
    private final MemberService memberService;

    @Subscribe
    @Transactional
    public void removePersistentLogins(MemberRemovedEvent event) throws MemberNotFoundException {
        log.debug("Remove 'remember me' tokens for removed member with id: {}",
            event.getMemberId());
        Member member = memberService.getMemberWithIdChecked(event.getMemberId());
        databaseTokenRepository.removeUserTokens(member.getUsername().getValue());
    }


}
