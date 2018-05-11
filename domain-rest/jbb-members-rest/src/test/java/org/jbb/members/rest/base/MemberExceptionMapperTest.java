/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import org.jbb.lib.restful.error.ErrorDetail;
import org.jbb.security.api.password.PasswordPolicy;
import org.jbb.security.api.password.PasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MemberExceptionMapperTest {

    @Mock
    private PasswordService passwordServiceMock;

    @InjectMocks
    private MemberExceptionMapper memberExceptionMapper;

    @Test
    public void shouldMapViolationOfVisiblePassword() throws Exception {
        // given
        ConstraintViolation violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(violation.getMessage()).thenReturn("Min: {0}, Max: {1}");
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("visiblePassword");

        when(passwordServiceMock.currentPolicy()).thenReturn(PasswordPolicy.builder()
                .minimumLength(5)
                .maximumLength(16)
                .build());

        // when
        ErrorDetail errorDetail = memberExceptionMapper.mapToErrorDetail(violation);

        // then
        assertThat(errorDetail.getName()).isEqualTo("password");
        assertThat(errorDetail.getMessage()).isEqualTo("Min: 5, Max: 16");
    }

    @Test
    public void shouldCutValueSuffix() throws Exception {
        // given
        ConstraintViolation violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(violation.getMessage()).thenReturn("Invalid email");
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("email.value");

        // when
        ErrorDetail errorDetail = memberExceptionMapper.mapToErrorDetail(violation);

        // then
        assertThat(errorDetail.getName()).isEqualTo("email");
        assertThat(errorDetail.getMessage()).isEqualTo("Invalid email");
    }
}