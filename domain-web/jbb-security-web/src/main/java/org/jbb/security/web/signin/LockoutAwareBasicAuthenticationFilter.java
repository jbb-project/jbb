/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.event.BasicAuthenticationSuccessEvent;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LockoutAwareBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final JbbEventBus eventBus;
    private final MemberLockoutService memberLockoutService;

    public LockoutAwareBasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                                 JbbEventBus eventBus,
                                                 MemberLockoutService memberLockoutService) {
        super(authenticationManager);
        this.eventBus = eventBus;
        this.memberLockoutService = memberLockoutService;
    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        SecurityContentUser user = (SecurityContentUser) authResult.getPrincipal();
        log.debug("Member with id '{}' basic authentication successful", user.getUserId());
        memberLockoutService.cleanFailedAttemptsForMember(user.getUserId());
        eventBus.post(new BasicAuthenticationSuccessEvent(user.getUserId()));
        super.onSuccessfulAuthentication(request, response, authResult);
    }

}
