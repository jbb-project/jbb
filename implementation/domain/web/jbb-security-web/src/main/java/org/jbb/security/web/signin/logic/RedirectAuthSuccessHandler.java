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
import org.jbb.security.event.SignInSuccessEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedirectAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JbbEventBus eventBus;

    @Autowired
    public RedirectAuthSuccessHandler(JbbEventBus eventBus) {
        super();

        this.eventBus = eventBus;

        setDefaultTargetUrl("/");
        setUseReferer(true);
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        log.debug("Member '{}' sign in sucessful", user);
        eventBus.post(new SignInSuccessEvent(Login.builder().value(user.getUsername()).build()));
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
