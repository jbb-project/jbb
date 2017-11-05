/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

public class SessionTerminatedEventTest extends BaseEventTest {

    @Test
    public void shouldSetSessionId() throws Exception {
        // given
        String expectedId = "aaa";
        SessionTerminatedEvent event = new SessionTerminatedEvent(expectedId);

        // when
        eventBus.post(event);
        String sessionId = event.getSessionId();

        // then
        assertThat(sessionId).isEqualTo(expectedId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullSessionIdPassed() throws Exception {
        // given
        String nullId = null;
        SessionTerminatedEvent event = new SessionTerminatedEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}