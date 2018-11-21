/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_SEE_STACKTRACE;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.jbb.permissions.api.PermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PermissionBasedStackTraceProviderTest {

    @Mock
    private PermissionService permissionServiceMock;

    @InjectMocks
    private PermissionBasedStackTraceProvider permissionBasedStackTraceProvider;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenExceptionIsNull() throws Exception {
        // when
        permissionBasedStackTraceProvider.getClientStackTrace(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldReturnStackTrace_whenCurrentMemberHasPermission() throws Exception {
        // given
        given(permissionServiceMock.checkPermission(eq(CAN_SEE_STACKTRACE))).willReturn(true);

        // when
        Optional<String> clientStackTrace = permissionBasedStackTraceProvider
            .getClientStackTrace(new Exception());

        // then
        assertThat(clientStackTrace).isPresent();
        assertThat(clientStackTrace.get()).isNotBlank();
    }

    @Test
    public void shouldNotReturnStackTrace_whenCurrentMemberHasNotPermission() throws Exception {
        // given
        given(permissionServiceMock.checkPermission(eq(CAN_SEE_STACKTRACE))).willReturn(false);

        // when
        Optional<String> clientStackTrace = permissionBasedStackTraceProvider
            .getClientStackTrace(new Exception());

        // then
        assertThat(clientStackTrace).isNotPresent();
    }
}