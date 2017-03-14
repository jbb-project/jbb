/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;

import com.google.common.collect.Sets;

import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.api.exception.PasswordException;
import org.jbb.security.impl.password.data.PasswordProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PasswordLengthRequirementsTest {

    @Mock
    private PasswordProperties propertiesMock;

    @Mock
    private Validator validatorMock;

    @InjectMocks
    private PasswordLengthRequirements passwordLengthRequirements;

    @Test
    public void shouldReturnMinimumLengthFromProperties_whenMinimumLengthMethodInvoked() throws Exception {
        // given
        int expectedMinimumLength = 4;
        given(propertiesMock.passwordMinimumLength()).willReturn(expectedMinimumLength);

        // when
        int result = passwordLengthRequirements.minimumLength();

        // then
        assertThat(result).isEqualTo(expectedMinimumLength);
    }

    @Test
    public void shouldReturnMaximumLengthFromProperties_whenMaximumLengthMethodInvoked() throws Exception {
        // given
        int expectedMaximumLength = 16;
        given(propertiesMock.passwordMaximumLength()).willReturn(expectedMaximumLength);

        // when
        int result = passwordLengthRequirements.maximumLength();

        // then
        assertThat(result).isEqualTo(expectedMaximumLength);
    }

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenRequirementsValidationFailed() throws Exception {
        // given
        given(validatorMock.validate(any(PasswordRequirementsImpl.class))).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        passwordLengthRequirements.update(mock(PasswordRequirements.class));

        // then
        // throw PasswordException
    }

    @Test
    public void shouldUpdateMinimumLengthProperty_whenMinLengthPassedThroughNewRequirements() throws Exception {
        // given
        given(validatorMock.validate(any(PasswordRequirementsImpl.class))).willReturn(Sets.newHashSet());

        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(6);
        given(newRequirements.maximumLength()).willReturn(16);

        // when
        passwordLengthRequirements.update(newRequirements);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MIN_LENGTH_KEY), eq(Integer.toString(6)));
    }

    @Test
    public void shouldUpdateMaximumLengthProperty_whenMaxLengthPassedThroughNewRequirements() throws Exception {
        // given
        given(validatorMock.validate(any(PasswordRequirementsImpl.class))).willReturn(Sets.newHashSet());

        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(6);
        given(newRequirements.maximumLength()).willReturn(10);

        // when
        passwordLengthRequirements.update(newRequirements);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MAX_LENGTH_KEY), eq(Integer.toString(10)));
    }
}