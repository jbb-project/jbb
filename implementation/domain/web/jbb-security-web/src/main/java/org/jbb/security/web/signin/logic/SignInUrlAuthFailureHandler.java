/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin.logic;

import org.jbb.lib.core.vo.Username;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.service.MemberService;
import org.jbb.security.event.SignInFailedEvent;
import org.jbb.security.web.SecurityWebConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SignInUrlAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final MemberService memberService;
    private final JbbEventBus eventBus;

    @Autowired
    public SignInUrlAuthFailureHandler(MemberService memberService, JbbEventBus eventBus) {
        super(SecurityWebConfig.LOGIN_FAILURE_URL);
        this.memberService = memberService;
        this.eventBus = eventBus;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        Username username = Username.builder().value(request.getParameter("username")).build();
        Long memberId = tryToResolveMemberId(username);
        log.debug("Sign in attempt failure for member with username '{}' (member id: {})", username.getValue(), memberId);

        eventBus.post(new SignInFailedEvent(memberId, username));
        super.onAuthenticationFailure(request, response, e);
    }

    private Long tryToResolveMemberId(Username username) {
        Optional<Member> memberWithUsername = memberService.getMemberWithUsername(username);
        if (memberWithUsername.isPresent()) {
            return memberWithUsername.get().getId();
        } else {
            return null;
        }
    }
}
