/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

public class MemberAccountChangedEventTest extends BaseEventTest {

    @Test
    public void shouldSetMemberId() throws Exception {
        // given
        Long expectedId = 344L;
        MemberAccountChangedEvent event = new MemberAccountChangedEvent(expectedId);

        // when
        eventBus.post(event);
        Long memberId = event.getMemberId();

        // then
        assertThat(memberId).isEqualTo(expectedId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullMemberIdPassed() throws Exception {
        // given
        Long nullId = null;
        MemberAccountChangedEvent event = new MemberAccountChangedEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}