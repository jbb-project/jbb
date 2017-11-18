/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.session;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.event.SignOutEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.session.Session;
import org.springframework.session.events.SessionDestroyedEvent;

@RunWith(MockitoJUnitRunner.class)
public class SessionDestroyListenerTest {

    @Mock
    private JbbEventBus jbbEventBusMock;

    @InjectMocks
    private SessionDestroyListener sessionDestroyListener;

    @Test
    public void shouldEmitSIgnOutEvent_whenSpringSessionDestroyedEventPresent() throws Exception {
        // given
        SessionDestroyedEvent sessionDestroyedEvent = prepareEventMock(SessionDestroyedEvent.class);

        // when
        sessionDestroyListener.onApplicationEvent(sessionDestroyedEvent);

        // then
        verify(jbbEventBusMock, times(1)).post(any(SignOutEvent.class));
    }

    private SessionDestroyedEvent prepareEventMock(Class<SessionDestroyedEvent> clazz) {
        SessionDestroyedEvent sessionDestroyedEvent = mock(SessionDestroyedEvent.class);
        Session sessionObject = mock(Session.class);
        SecurityContextImpl sessionContext = mock(SecurityContextImpl.class);
        Authentication authentication = mock(Authentication.class);
        SecurityContentUser principal = mock(SecurityContentUser.class);

        given(sessionDestroyedEvent.getSession()).willReturn(sessionObject);
        given(sessionObject.getAttribute(eq("SPRING_SECURITY_CONTEXT"))).willReturn(sessionContext);
        given(sessionContext.getAuthentication()).willReturn(authentication);
        given(authentication.getPrincipal()).willReturn(principal);

        return sessionDestroyedEvent;
    }
}