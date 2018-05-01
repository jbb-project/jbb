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

public class SignInSuccessEventTest extends BaseEventTest {
    @Test
    public void shouldSetUsername() {
        // given
        Long expectedId = 33L;
        String expectedCreatedSessionId = "aaa";
        SignInSuccessEvent event = new SignInSuccessEvent(expectedId, expectedCreatedSessionId);

        // when
        eventBus.post(event);
        Long id = event.getMemberId();
        String createdSessionId = event.getCreatedSessionId();

        // then
        assertThat(id).isEqualTo(expectedId);
        assertThat(createdSessionId).isEqualTo(expectedCreatedSessionId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullMemberIdPassed() {
        // given
        Long nullId = null;
        String createdSessionId = "bbbb";
        SignInSuccessEvent event = new SignInSuccessEvent(nullId, createdSessionId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullCreatedSessionIdPassed() {
        // given
        Long id = 5L;
        String nullCreatedSessionId = null;
        SignInSuccessEvent event = new SignInSuccessEvent(id, nullCreatedSessionId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}