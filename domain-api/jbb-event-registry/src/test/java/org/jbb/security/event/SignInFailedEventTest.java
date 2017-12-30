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
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class SignInFailedEventTest extends BaseEventTest {
    @Test
    public void shouldSetUsername() throws Exception {
        // given
        Long expectedId = 22L;
        Username expectedUsername = Username.builder().value("john").build();
        SignInFailedEvent event = new SignInFailedEvent(expectedId, expectedUsername);

        // when
        eventBus.post(event);
        Username username = event.getUsername();
        Optional<Long> memberId = event.getMemberId();

        // then
        assertThat(memberId).hasValue(expectedId);
        assertThat(username).isEqualTo(expectedUsername);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullUsernamePassed() throws Exception {
        // given
        Username nullUsername = null;
        SignInFailedEvent event = new SignInFailedEvent(1L, nullUsername);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test
    public void shouldReturnEmptyOptional_whenNullIdPassed() throws Exception {
        // given
        Long nullId = null;
        Username anyUsername = Username.builder().value("john").build();

        // when
        SignInFailedEvent event = new SignInFailedEvent(nullId, anyUsername);
        eventBus.post(event);

        // then
        assertThat(event.getMemberId()).isEmpty();
    }
}