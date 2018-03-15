/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.event;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberRegisteredEventTest extends BaseEventTest {
    @Test
    public void shouldSetMemberId() {
        // given
        Long expectedId = 344L;
        MemberRegisteredEvent event = new MemberRegisteredEvent(expectedId);

        // when
        eventBus.post(event);
        Long memberId = event.getMemberId();

        // then
        assertThat(memberId).isEqualTo(expectedId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullMemberIdPassed() {
        // given
        Long nullId = null;
        MemberRegisteredEvent event = new MemberRegisteredEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }
}