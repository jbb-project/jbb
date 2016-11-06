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

import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.impl.password.data.PasswordProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PasswordLengthRequirementsTest {
    @Mock
    private PasswordProperties propertiesMock;

    @Mock
    private PasswordLengthValidator passwordLengthValidatorMock;

    @InjectMocks
    private PasswordLengthRequirements passwordLengthRequirements;

    @Test
    public void shouldNotSetMinimumLength_whenNullPropertyPassed() throws Exception {
        // given
        given(propertiesMock.passwordMinimumLength()).willReturn(null);

        // when
        Optional<Integer> minimumLength = passwordLengthRequirements.minimumLength();

        // then
        assertThat(minimumLength).isNotPresent();
    }

    @Test
    public void shouldNotSetMinimumLength_whenNegativeValuePropertyPassed() throws Exception {
        // given
        given(propertiesMock.passwordMinimumLength()).willReturn(-1);

        // when
        Optional<Integer> minimumLength = passwordLengthRequirements.minimumLength();

        // then
        assertThat(minimumLength).isNotPresent();
    }

    @Test
    public void shouldNotSetMinimumLength_whenZeroValuePropertyPassed() throws Exception {
        // given
        given(propertiesMock.passwordMinimumLength()).willReturn(0);

        // when
        Optional<Integer> minimumLength = passwordLengthRequirements.minimumLength();

        // then
        assertThat(minimumLength).isNotPresent();
    }

    @Test
    public void shouldSetMinimumLength_whenPositiveValuePropertyPassed() throws Exception {
        // given
        given(propertiesMock.passwordMinimumLength()).willReturn(4);

        // when
        Optional<Integer> minimumLength = passwordLengthRequirements.minimumLength();

        // then
        assertThat(minimumLength).isPresent().hasValue(4);
    }

    @Test
    public void shouldNotSetMaximumLength_whenNullPropertyPassed() throws Exception {
        // given
        given(propertiesMock.passwordMaximumLength()).willReturn(null);

        // when
        Optional<Integer> maximumLength = passwordLengthRequirements.maximumLength();

        // then
        assertThat(maximumLength).isNotPresent();
    }

    @Test
    public void shouldNotSetMaximumLength_whenNegativeValuePropertyPassed() throws Exception {
        // given
        given(propertiesMock.passwordMaximumLength()).willReturn(-1);

        // when
        Optional<Integer> maximumLength = passwordLengthRequirements.maximumLength();

        // then
        assertThat(maximumLength).isNotPresent();
    }

    @Test
    public void shouldNotSetMaximumLength_whenZeroValuePropertyPassed() throws Exception {
        // given
        given(propertiesMock.passwordMaximumLength()).willReturn(0);

        // when
        Optional<Integer> maximumLength = passwordLengthRequirements.maximumLength();

        // then
        assertThat(maximumLength).isNotPresent();
    }

    @Test
    public void shouldSetMaximumLength_whenPositiveValuePropertyPassed() throws Exception {
        // given
        given(propertiesMock.passwordMaximumLength()).willReturn(16);

        // when
        Optional<Integer> maximumLength = passwordLengthRequirements.maximumLength();

        // then
        assertThat(maximumLength).isPresent().hasValue(16);
    }

    @Test
    public void shouldUpdateMinimumLengthProperty_whenMinLengthPassedThroughNewRequirements() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.of(6));
        given(newRequirements.maximumLength()).willReturn(Optional.empty()); // not important for this test case

        // when
        passwordLengthRequirements.update(newRequirements);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MIN_LENGTH_KEY), eq(Integer.toString(6)));
    }

    @Test
    public void shouldUpdateMinimumLengthPropertyToOne_whenMinLengthNotPassedThroughNewRequirements() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.empty());
        given(newRequirements.maximumLength()).willReturn(Optional.empty()); // not important for this test case

        // when
        passwordLengthRequirements.update(newRequirements);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MIN_LENGTH_KEY), eq(Integer.toString(1)));
    }

    @Test
    public void shouldUpdateMaximumLengthProperty_whenMaxLengthPassedThroughNewRequirements() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.empty()); // not important for this test case
        given(newRequirements.maximumLength()).willReturn(Optional.of(10));

        // when
        passwordLengthRequirements.update(newRequirements);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MAX_LENGTH_KEY), eq(Integer.toString(10)));
    }

    @Test
    public void shouldUpdateMaximumLengthPropertyToMaxIntValue_whenMaxLengthNotPassedThroughNewRequirements() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.empty()); // not important for this test case
        given(newRequirements.maximumLength()).willReturn(Optional.empty());

        // when
        passwordLengthRequirements.update(newRequirements);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MAX_LENGTH_KEY), eq(Integer.toString(Integer.MAX_VALUE)));
    }
}