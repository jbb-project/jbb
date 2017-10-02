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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

public class MemberLockedEventTest extends BaseEventTest {

    @Test
    public void shouldSetMemberIdAndExpirationDateTime() throws Exception {
        // given
        Long expectedId = 1234L;
        LocalDateTime expectedExpirationDateTime = LocalDateTime.now();
        MemberLockedEvent event = new MemberLockedEvent(expectedId, expectedExpirationDateTime);

        // when
        eventBus.post(event);
        Long memberId = event.getMemberId();
        LocalDateTime expirationDateTime = event.getExpirationDateTime();

        // then
        assertThat(memberId).isEqualTo(expectedId);
        assertThat(expirationDateTime).isEqualTo(expectedExpirationDateTime);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullIdPassed() throws Exception {
        // given
        Long nullId = null;
        MemberLockedEvent event = new MemberLockedEvent(nullId, LocalDateTime.now());

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullExpirationDateTimePassed()
        throws Exception {
        // given
        LocalDateTime nullDateTime = null;
        MemberLockedEvent event = new MemberLockedEvent(12L, nullDateTime);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }
}