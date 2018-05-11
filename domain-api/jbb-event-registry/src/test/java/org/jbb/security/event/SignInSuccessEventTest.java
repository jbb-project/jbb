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


import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

public class SignInSuccessEventTest extends BaseEventTest {
    @Test
    public void shouldSetUsername() {
        // given
        Long expectedId = 33L;
        String expectedCreatedSessionId = "aaa";
        Boolean expectedAutoSignIn = false;
        SignInSuccessEvent event = new SignInSuccessEvent(expectedId, expectedCreatedSessionId,
            expectedAutoSignIn);

        // when
        eventBus.post(event);
        Long id = event.getMemberId();
        String createdSessionId = event.getCreatedSessionId();
        Boolean autoSignIn = event.getAutoSignIn();

        // then
        assertThat(id).isEqualTo(expectedId);
        assertThat(createdSessionId).isEqualTo(expectedCreatedSessionId);
        assertThat(autoSignIn).isFalse();
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullMemberIdPassed() {
        // given
        Long nullId = null;
        String createdSessionId = "bbbb";
        Boolean autoSignIn = false;
        SignInSuccessEvent event = new SignInSuccessEvent(nullId, createdSessionId, autoSignIn);

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
        Boolean autoSignIn = false;
        SignInSuccessEvent event = new SignInSuccessEvent(id, nullCreatedSessionId, autoSignIn);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullAutoSignInPassed() {
        // given
        Long id = 5L;
        String createdSessionId = "bbbb";
        Boolean nullAutoSignIn = null;
        SignInSuccessEvent event = new SignInSuccessEvent(id, createdSessionId, nullAutoSignIn);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}