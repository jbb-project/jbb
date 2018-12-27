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
import org.jbb.security.event.SignInSuccessEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedirectAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JbbEventBus eventBus;
    private final MemberLockoutService memberLockoutService;

    @Autowired
    public RedirectAuthSuccessHandler(JbbEventBus eventBus, MemberLockoutService memberLockoutService) {
        super();

        this.memberLockoutService = memberLockoutService;
        this.eventBus = eventBus;

        setDefaultTargetUrl("/");
        setUseReferer(true);
        setAlwaysUseDefaultTargetUrl(false);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        SecurityContentUser user = (SecurityContentUser) authentication.getPrincipal();
        log.debug("Member with id '{}' sign in successful", user.getUserId());
        memberLockoutService.cleanFailedAttemptsForMember(user.getUserId());
        super.onAuthenticationSuccess(request, response, authentication);
        eventBus
            .post(new SignInSuccessEvent(user.getUserId(), request.getSession().getId(), false));
    }
}
