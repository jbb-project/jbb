/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.base.data.validation;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.jbb.board.api.base.validation.ValidDateFormatValidator;
import org.junit.Before;
import org.junit.Test;

public class ValidDateFormatValidatorTest {
    private static ConstraintValidatorContext ANY_VALIDATOR_CONTEXT = null;

    private ValidDateFormatValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new ValidDateFormatValidator();

    }

    @Test
    public void shouldFailed_whenNull() throws Exception {
        // given
        String pattern = null;

        // when
        boolean result = validator.isValid(pattern, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldFailed_whenEmpty() throws Exception {
        // given
        String pattern = StringUtils.EMPTY;

        // when
        boolean result = validator.isValid(pattern, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldFailed_whenIncorrectFormat() throws Exception {
        // given
        String pattern = "lorem ipsum xD";

        // when
        boolean result = validator.isValid(pattern, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldPassed_whenCorrectFormat() throws Exception {
        // given
        String pattern = "dd/MM/yyyy HH:mm:ss";

        // when
        boolean result = validator.isValid(pattern, ANY_VALIDATOR_CONTEXT);

        // then
        assertThat(result).isTrue();
    }
}