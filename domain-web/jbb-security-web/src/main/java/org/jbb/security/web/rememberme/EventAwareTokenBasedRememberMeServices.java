/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.rememberme;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.event.SignInSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public class EventAwareTokenBasedRememberMeServices extends PersistentTokenBasedRememberMeServices {

    private final JbbEventBus eventBus;

    public EventAwareTokenBasedRememberMeServices(String key,
        UserDetailsService userDetailsService,
        PersistentTokenRepository tokenRepository, JbbEventBus eventBus) {
        super(key, userDetailsService, tokenRepository);
        this.eventBus = eventBus;
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens,
        HttpServletRequest request, HttpServletResponse response) {
        SecurityContentUser result = (SecurityContentUser) super
            .processAutoLoginCookie(cookieTokens, request, response);
        eventBus
            .post(new SignInSuccessEvent(result.getUserId(), request.getSession().getId(), true));
        return result;
    }
}
