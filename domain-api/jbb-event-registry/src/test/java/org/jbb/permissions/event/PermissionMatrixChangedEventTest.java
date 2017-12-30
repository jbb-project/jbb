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

import org.apache.commons.lang3.StringUtils;
import org.jbb.BaseEventTest;
import org.jbb.lib.eventbus.EventValidationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PermissionMatrixChangedEventTest extends BaseEventTest {

    @Test
    public void shouldSetPermissionAndSecurityIdentity() throws Exception {
        // given
        String expectedPermissionType = "aaa";
        Long expectedSecurityIdentityId = 22L;
        String expectedSecurityIdentityType = "bbb";
        PermissionMatrixChangedEvent event = new PermissionMatrixChangedEvent(
                expectedPermissionType, expectedSecurityIdentityId, expectedSecurityIdentityType
        );

        // when
        eventBus.post(event);
        String permissionType = event.getPermissionType();
        Long securityIdentityId = event.getSecurityIdentityId();
        String securityIdentityType = event.getSecurityIdentityType();

        // then
        assertThat(permissionType).isEqualTo(expectedPermissionType);
        assertThat(securityIdentityId).isEqualTo(securityIdentityId);
        assertThat(securityIdentityType).isEqualTo(securityIdentityType);
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullPermissionTypePassed()
            throws Exception {
        // given
        String nullPermissionType = null;
        Long expectedSecurityIdentityId = 22L;
        String expectedSecurityIdentityType = "bbb";
        PermissionMatrixChangedEvent event = new PermissionMatrixChangedEvent(
                nullPermissionType, expectedSecurityIdentityId, expectedSecurityIdentityType
        );

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenEmptyPermissionTypePassed()
            throws Exception {
        // given
        String emptyPermissionType = StringUtils.EMPTY;
        Long expectedSecurityIdentityId = 22L;
        String expectedSecurityIdentityType = "bbb";
        PermissionMatrixChangedEvent event = new PermissionMatrixChangedEvent(
                emptyPermissionType, expectedSecurityIdentityId, expectedSecurityIdentityType
        );

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenBlankPermissionTypePassed()
            throws Exception {
        // given
        String blankPermissionType = StringUtils.SPACE;
        Long expectedSecurityIdentityId = 22L;
        String expectedSecurityIdentityType = "bbb";
        PermissionMatrixChangedEvent event = new PermissionMatrixChangedEvent(
                blankPermissionType, expectedSecurityIdentityId, expectedSecurityIdentityType
        );

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullSecurityIdentityIdPassed()
            throws Exception {
        // given
        String expectedPermissionType = "aaa";
        Long nullSecurityIdentityId = null;
        String expectedSecurityIdentityType = "bbb";
        PermissionMatrixChangedEvent event = new PermissionMatrixChangedEvent(
                expectedPermissionType, nullSecurityIdentityId, expectedSecurityIdentityType
        );

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenNullSecurityIdentityTypePassed()
            throws Exception {
        // given
        String expectedPermissionType = "aaa";
        Long expectedSecurityIdentityId = 22L;
        String nullSecurityIdentityType = null;
        PermissionMatrixChangedEvent event = new PermissionMatrixChangedEvent(
                expectedPermissionType, expectedSecurityIdentityId, nullSecurityIdentityType
        );

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenEmptySecurityIdentityTypePassed()
            throws Exception {
        // given
        String expectedPermissionType = "aaa";
        Long expectedSecurityIdentityId = 22L;
        String emptySecurityIdentityType = StringUtils.EMPTY;
        PermissionMatrixChangedEvent event = new PermissionMatrixChangedEvent(
                expectedPermissionType, expectedSecurityIdentityId, emptySecurityIdentityType
        );

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

    @Test(expected = EventValidationException.class)
    public void shouldThrowEventValidationException_whenBlankSecurityIdentityTypePassed()
            throws Exception {
        // given
        String expectedPermissionType = "aaa";
        Long expectedSecurityIdentityId = 22L;
        String blankSecurityIdentityType = StringUtils.SPACE;
        PermissionMatrixChangedEvent event = new PermissionMatrixChangedEvent(
                expectedPermissionType, expectedSecurityIdentityId, blankSecurityIdentityType
        );

        // when
        eventBus.post(event);

        // then
        // throw EventValidationException
    }

}