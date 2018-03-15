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
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AdministratorPermissionChangedEventTest extends BaseEventTest {

    @Test
    public void shouldSetMemberId() {
        // given
        Long expectedMemberId = 22L;
        String expectedIdentityGroupName = null;
        AdministratorPermissionChangedEvent event = new AdministratorPermissionChangedEvent(
                expectedMemberId, expectedIdentityGroupName
        );

        // when
        eventBus.post(event);
        Long memberId = event.getMemberId();
        String identityGroupName = event.getIdentityGroupName();

        // then
        assertThat(memberId).isEqualTo(expectedMemberId);
        assertThat(identityGroupName).isEqualTo(expectedIdentityGroupName);
    }

    @Test
    public void shouldSetIdentityGroupName() {
        // given
        Long expectedMemberId = null;
        String expectedIdentityGroupName = "REGISTERED_MEMBERS";
        AdministratorPermissionChangedEvent event = new AdministratorPermissionChangedEvent(
                expectedMemberId, expectedIdentityGroupName
        );

        // when
        eventBus.post(event);
        Long memberId = event.getMemberId();
        String identityGroupName = event.getIdentityGroupName();

        // then
        assertThat(memberId).isEqualTo(expectedMemberId);
        assertThat(identityGroupName).isEqualTo(expectedIdentityGroupName);
    }

}