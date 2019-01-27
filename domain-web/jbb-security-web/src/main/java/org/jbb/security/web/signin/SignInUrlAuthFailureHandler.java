/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin;

import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.event.SignInFailedEvent;
import org.jbb.security.web.SecurityWebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SignInUrlAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final MemberService memberService;
    private final JbbEventBus eventBus;
    private final MemberLockoutService memberLockoutService;

    @Autowired
    public SignInUrlAuthFailureHandler(MemberService memberService, JbbEventBus eventBus, MemberLockoutService memberLockoutService) {
        super(SecurityWebConfig.LOGIN_FAILURE_URL);
        this.memberService = memberService;
        this.memberLockoutService = memberLockoutService;
        this.eventBus = eventBus;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        String username = request.getParameter("username");
        Long memberId = tryToResolveMemberId(username);
        log.debug("Sign in attempt failure for member with username '{}' (member id: {})", username, memberId);
        memberLockoutService.lockMemberIfQualify(memberId);
        super.onAuthenticationFailure(request, response, e);
        eventBus.post(new SignInFailedEvent(memberId, username));
    }

    private Long tryToResolveMemberId(String username) {
        Optional<Member> memberWithUsername = memberService.getMemberWithUsername(Username.of(username));
        return memberWithUsername.map(Member::getId).orElse(null);
    }
}
