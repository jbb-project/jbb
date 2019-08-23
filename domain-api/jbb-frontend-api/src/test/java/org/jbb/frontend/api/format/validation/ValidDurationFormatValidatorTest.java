/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.api.format.validation;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ValidDurationFormatValidatorTest {

    private static ConstraintValidatorContext ANY_VALIDATOR_CONTEXT = null;

    private ValidDurationFormatValidator validator;

    @Before
    public void setUp() {
        validator = new ValidDurationFormatValidator();

    }

    @Test
    public void shouldFailed_whenNull() {
        // given
        String pattern = null;

        // when
        boolean result = validator.isValid(pattern, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldFailed_whenEmpty() {
        // given
        String pattern = StringUtils.EMPTY;

        // when
        boolean result = validator.isValid(pattern, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldFailed_whenIncorrectFormat() {
        // given
        String pattern = "lorem ipsum xD";

        // when
        boolean result = validator.isValid(pattern, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldPassed_whenCorrectFormat() {
        // given
        String pattern = "HH:mm:ss";

        // when
        boolean result = validator.isValid(pattern, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(result).isTrue();
    }
}