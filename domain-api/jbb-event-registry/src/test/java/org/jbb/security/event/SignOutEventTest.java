/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.event;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignOutEventTest extends BaseEventTest {

    @Test
    public void shouldSetMemberId() {
        // given
        Long expectedId = 1234L;
        String expectedSessionId = "aaa";
        SignOutEvent event = new SignOutEvent(expectedId, expectedSessionId, true);

        // when
        eventBus.post(event);
        Long memberId = event.getMemberId();
        String sessionId = event.getSessionId();
        boolean sessionExpired = event.isSessionExpired();

        // then
        assertThat(memberId).isEqualTo(expectedId);
        assertThat(sessionId).isEqualTo(expectedSessionId);
        assertThat(sessionExpired).isTrue();
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullIdPassed() {
        // given
        Long nullId = null;
        String sessionId = "bbb";
        SignOutEvent event = new SignOutEvent(nullId, sessionId, false);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullSessionIdPassed() {
        // given
        Long id = 1L;
        String nullSessionId = null;
        SignOutEvent event = new SignOutEvent(id, nullSessionId, false);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}