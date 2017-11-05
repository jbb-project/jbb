/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

public class PermissionRoleCreatedEventTest extends BaseEventTest {

    @Test
    public void shouldSetRoleId() throws Exception {
        // given
        Long expectedId = 344L;
        PermissionRoleCreatedEvent event = new PermissionRoleCreatedEvent(expectedId);

        // when
        eventBus.post(event);
        Long roleId = event.getRoleId();

        // then
        assertThat(roleId).isEqualTo(expectedId);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullRoleIdPassed() throws Exception {
        // given
        Long nullId = null;
        PermissionRoleCreatedEvent event = new PermissionRoleCreatedEvent(nullId);

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}