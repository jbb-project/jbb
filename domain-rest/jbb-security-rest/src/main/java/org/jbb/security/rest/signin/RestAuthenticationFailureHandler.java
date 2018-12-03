/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.signin;

import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.restful.error.RestAuthenticationEntryPoint;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.event.SignInFailedEvent;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component("restAuthenticationFailureHandler")
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final MemberLockoutService memberLockoutService;
    private final MemberService memberService;
    private final JbbEventBus eventBus;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException {
        Username username = Username.of(request.getParameter("username"));
        Long memberId = tryToResolveMemberId(username);
        log.debug("Sign in attempt failure for member with username '{}' (member id: {})", username.getValue(), memberId);
        memberLockoutService.lockMemberIfQualify(memberId);
        restAuthenticationEntryPoint.commence(request, response, e);
        eventBus.post(new SignInFailedEvent(memberId, username));
    }

    private Long tryToResolveMemberId(Username username) {
        Optional<Member> memberWithUsername = memberService.getMemberWithUsername(username);
        return memberWithUsername.map(Member::getId).orElse(null);
    }

}
