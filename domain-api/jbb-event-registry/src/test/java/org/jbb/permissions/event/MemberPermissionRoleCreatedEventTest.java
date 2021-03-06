/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.event;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberPermissionRoleCreatedEventTest extends BaseEventTest {

    @Test
    public void shouldSetRoleId() {
        // given
        Long expectedId = 344L;
        MemberPermissionRoleCreatedEvent event = new MemberPermissionRoleCreatedEvent(expectedId);

        // when
        eventBus.post(event);
        Long roleId = event.getRoleId();

        // then
        assertThat(roleId).isEqualTo(expectedId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullRoleIdPassed() {
        // given
        Long nullId = null;
        MemberPermissionRoleCreatedEvent event = new MemberPermissionRoleCreatedEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}