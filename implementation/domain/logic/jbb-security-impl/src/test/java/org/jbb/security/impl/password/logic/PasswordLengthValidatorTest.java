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
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class PasswordLengthValidatorTest {
    private PasswordLengthValidator passwordLengthValidator;

    @Before
    public void setUp() throws Exception {
        passwordLengthValidator = new PasswordLengthValidator();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMinimumLengthIsGreaterThanMaximumLength() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.of(6));
        given(newRequirements.maximumLength()).willReturn(Optional.of(4));

        // when
        passwordLengthValidator.validate(newRequirements);

        // then
        // throws IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMinimumLengthIsZeroLength() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.of(0));
        given(newRequirements.maximumLength()).willReturn(Optional.empty()); // not important for this test case

        // when
        passwordLengthValidator.validate(newRequirements);

        // then
        // throws IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMinimumLengthIsNegative() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.of(-1));
        given(newRequirements.maximumLength()).willReturn(Optional.empty()); // not important for this test case

        // when
        passwordLengthValidator.validate(newRequirements);

        // then
        // throws IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMaximumLengthIsZeroLength() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.empty()); // not important for this test case
        given(newRequirements.maximumLength()).willReturn(Optional.of(0));

        // when
        passwordLengthValidator.validate(newRequirements);

        // then
        // throws IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIAE_whenMaximumLengthIsNegative() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.empty()); // not important for this test case
        given(newRequirements.maximumLength()).willReturn(Optional.of(-1));

        // when
        passwordLengthValidator.validate(newRequirements);

        // then
        // throws IllegalArgumentException
    }

    @Test
    public void shouldPassed_whenBothLengthAreEqualAndPositive() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.of(1));
        given(newRequirements.maximumLength()).willReturn(Optional.of(1));

        // when
        passwordLengthValidator.validate(newRequirements);

        // then
        // not throws exception
    }

    @Test
    public void shouldPassed_whenMinimumLengthIsOne_andMaximumLengthIsEmpty() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.of(1));
        given(newRequirements.maximumLength()).willReturn(Optional.empty());

        // when
        passwordLengthValidator.validate(newRequirements);

        // then
        // not throws exception
    }

    @Test
    public void shouldPassed_whenMinimumLengthIsEmpty_andMaximumLengthIsOne() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.empty());
        given(newRequirements.maximumLength()).willReturn(Optional.of(1));

        // when
        passwordLengthValidator.validate(newRequirements);

        // then
        // not throws exception
    }

    @Test
    public void shouldPassed_whenBothLengthAreEmpty() throws Exception {
        // given
        PasswordRequirements newRequirements = mock(PasswordRequirements.class);
        given(newRequirements.minimumLength()).willReturn(Optional.empty());
        given(newRequirements.maximumLength()).willReturn(Optional.empty());

        // when
        passwordLengthValidator.validate(newRequirements);

        // then
        // not throws exception
    }
}