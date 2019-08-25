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

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.api.signin.SignInSettings;
import org.jbb.security.api.signin.SignInSettingsService;
import org.jbb.security.event.BasicAuthenticationSuccessEvent;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LockoutAwareBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final SignInSettingsService signInSettingsService;

    private final JbbEventBus eventBus;
    private final MemberLockoutService memberLockoutService;
    private HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

    public LockoutAwareBasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                                 JbbEventBus eventBus,
                                                 MemberLockoutService memberLockoutService,
                                                 SignInSettingsService signInSettingsService) {
        super(authenticationManager);
        this.eventBus = eventBus;
        this.memberLockoutService = memberLockoutService;
        this.signInSettingsService = signInSettingsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        SignInSettings signInSettings = signInSettingsService.getSignInSettings();
        if (isRequestWithBasicAuth(request) && !signInSettings.getBasicAuthEnabled()) {
            ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.BASIC_AUTH_DISABLED);
            ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
            outputMessage.setStatusCode(errorResponse.getStatus());
            messageConverter.write(errorResponse, MediaType.APPLICATION_JSON, outputMessage);
        } else {
            super.doFilterInternal(request, response, chain);
        }
    }

    private boolean isRequestWithBasicAuth(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return header != null && header.toLowerCase().startsWith("basic ");
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
