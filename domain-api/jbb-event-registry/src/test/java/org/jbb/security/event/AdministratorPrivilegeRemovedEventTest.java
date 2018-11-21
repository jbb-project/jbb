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

public class AdministratorPrivilegeRemovedEventTest extends BaseEventTest {
    @Test
    public void shouldSetMemberId() throws Exception {
        // given
        Long expectedId = 1234L;
        AdministratorPrivilegeRemovedEvent event = new AdministratorPrivilegeRemovedEvent(
            expectedId);

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
        AdministratorPrivilegeRemovedEvent event = new AdministratorPrivilegeRemovedEvent(
                nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }
}