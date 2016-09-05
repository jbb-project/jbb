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

import org.jbb.lib.core.vo.Login;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.event.SignInFailedEvent;
import org.jbb.security.web.SecurityWebConfig;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SignInUrlAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final JbbEventBus eventBus;

    public SignInUrlAuthFailureHandler(JbbEventBus eventBus) {
        super(SecurityWebConfig.LOGIN_FAILURE_URL);

        this.eventBus = eventBus;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {
        String username = request.getParameter("login");
        log.debug("Sign in attempt failure for member '{}'", username);
        eventBus.post(new SignInFailedEvent(Login.builder().value(username).build()));
        super.onAuthenticationFailure(request, response, e);
    }
}
