/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.services;

import org.jbb.lib.core.vo.Password;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEqualityValidatorTest {
    private static Password FOO_PASSWORD = Password.builder().value("foo".toCharArray()).build();
    private static Password FOO_PASSWORD_AGAIN = Password.builder().value("foo".toCharArray()).build();
    private static Password BAR_PASSWORD = Password.builder().value("bar".toCharArray()).build();

    private static ConstraintValidatorContext ANY_VALIDATOR_CONTEXT = null;

    private PasswordEqualityValidator passwordEqualityValidator;

    @Before
    public void setUp() throws Exception {
        passwordEqualityValidator = new PasswordEqualityValidator();
    }

    @Test
    public void shouldPassValidation_whenPasswordAreTheSame() throws Exception {
        // given
        PasswordPair passwordPair = new PasswordPair(FOO_PASSWORD, FOO_PASSWORD_AGAIN);

        // when
        boolean passwordsAreTheSame = passwordEqualityValidator.isValid(passwordPair, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(passwordsAreTheSame).isTrue();
    }

    @Test
    public void shouldFailValidation_whenPasswordAreNotTheSame() throws Exception {
        // given
        PasswordPair passwordPair = new PasswordPair(FOO_PASSWORD, BAR_PASSWORD);

        // when
        boolean passwordsAreTheSame = passwordEqualityValidator.isValid(passwordPair, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(passwordsAreTheSame).isFalse();
    }
}