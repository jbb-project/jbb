/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.session;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSession;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(MockitoJUnitRunner.class)
public class JbbSessionRepositoryTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisherMock;

    private JbbSessionRepository jbbSessionRepository;

    @Before
    public void setup() {
        jbbSessionRepository = new JbbSessionRepository(applicationEventPublisherMock);
    }

    @Test
    public void shouldThrowIllegalArgumentException_whenNullSessionsProvided() {
        assertThatThrownBy(() -> {
            new JbbSessionRepository(null, applicationEventPublisherMock);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testMaxInactiveInternal() {
        // when
        jbbSessionRepository.setDefaultMaxInactiveInterval(200);
        Integer maxInactiveInterval = jbbSessionRepository.getDefaultMaxInactiveInterval();

        // then
        assertThat(maxInactiveInterval).isEqualTo(200);
    }

    @Test
    public void testExpiration() throws InterruptedException {
        jbbSessionRepository.setDefaultMaxInactiveInterval(1);
        ExpiringSession session = jbbSessionRepository.createSession();
        Thread.sleep(1000); //NOSONAR
        assertThat(session.isExpired()).isTrue();
    }

    @Test
    public void testGettingOfExpireSession() throws InterruptedException {
        jbbSessionRepository.setDefaultMaxInactiveInterval(1);
        ExpiringSession session = jbbSessionRepository.createSession();
        jbbSessionRepository.save(session);
        Thread.sleep(1000); //NOSONAR
        ExpiringSession returnedSession = jbbSessionRepository.getSession(session.getId());
        assertThat(returnedSession).isNull();
    }

    @Test
    public void testDeletingSession() {
        jbbSessionRepository.setDefaultMaxInactiveInterval(1000);
        ExpiringSession session = jbbSessionRepository.createSession();
        jbbSessionRepository.save(session);
        assertThat(jbbSessionRepository.getSession(session.getId())).isNotNull();
        jbbSessionRepository.delete(session.getId());
        ExpiringSession returnedSession = jbbSessionRepository.getSession(session.getId());
        assertThat(returnedSession).isNull();
    }

    @Test
    public void testGetNotExistSession() {
        ExpiringSession session = jbbSessionRepository.getSession("not-existing-session");
        assertThat(session).isNull();
    }

    public ExpiringSession expiringSession(String id) {
        return new MapSession(id);
    }
}