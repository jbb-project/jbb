/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session.logic;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.event.SignOutEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.events.SessionDestroyedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Component;

import static org.jbb.system.impl.session.logic.SessionServiceImpl.SESSION_CONTEXT_ATTRIBUTE_NAME;

@Component
public class SessionExpirationListener implements ApplicationListener<SessionDestroyedEvent> {
    private final JbbEventBus jbbEventBus;

    @Autowired
    public SessionExpirationListener(JbbEventBus jbbEventBus) {
        this.jbbEventBus = jbbEventBus;
    }

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        Object sessionContext = event.getSession().getAttribute(SESSION_CONTEXT_ATTRIBUTE_NAME);
        if (!(event instanceof SessionExpiredEvent) && sessionContext == null) {
            return;
        }

        SecurityContentUser securityContentUser = (SecurityContentUser) ((SecurityContextImpl) sessionContext).getAuthentication().getPrincipal();
        jbbEventBus.post(new SignOutEvent(securityContentUser.getUserId(), true));
    }

}