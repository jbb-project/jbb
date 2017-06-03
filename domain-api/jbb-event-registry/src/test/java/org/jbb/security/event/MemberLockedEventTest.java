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

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberLockedEventTest {

    @Test
    public void shouldSetMemberIdAndExpirationDateTime() throws Exception {
        // given
        Long expectedId = 1234L;
        LocalDateTime expectedExpirationDateTime = LocalDateTime.now();
        MemberLockedEvent event = new MemberLockedEvent(expectedId, expectedExpirationDateTime);

        // when
        Long memberId = event.getMemberId();
        LocalDateTime expirationDateTime = event.getExpirationDateTime();

        // then
        assertThat(memberId).isEqualTo(expectedId);
        assertThat(expectedExpirationDateTime).isEqualTo(expectedExpirationDateTime);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullIdPassed() throws Exception {
        // given
        Long nullId = null;

        // when
        new MemberLockedEvent(nullId, LocalDateTime.now());

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullExpirationDateTimePassed() throws Exception {
        // given
        LocalDateTime nullDateTime = null;

        // when
        new MemberLockedEvent(12L, nullDateTime);

        // then
        // throw NullPointerException
    }
}