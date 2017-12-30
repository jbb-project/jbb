/*
 * Copyright (C) 2017 the original author or authors.
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
    public void shouldSetMemberId() throws Exception {
        // given
        Long expectedId = 1234L;
        SignOutEvent event = new SignOutEvent(expectedId, true);

        // when
        eventBus.post(event);
        Long memberId = event.getMemberId();
        boolean sessionExpired = event.isSessionExpired();

        // then
        assertThat(memberId).isEqualTo(expectedId);
        assertThat(sessionExpired).isTrue();
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullIdPassed() throws Exception {
        // given
        Long nullId = null;
        SignOutEvent event = new SignOutEvent(nullId, false);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}