/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.password.validation;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import org.jbb.security.api.password.PasswordRequirements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MinimumLessOrEqualToMaximumValidatorTest {

    @Mock
    private ConstraintValidatorContext contextMock;

    private MinimumLessOrEqualToMaximumValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new MinimumLessOrEqualToMaximumValidator();
        validator.initialize(null);

        ConstraintValidatorContext.ConstraintViolationBuilder violationBuilderMock =
            mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(contextMock.buildConstraintViolationWithTemplate(any()))
            .thenReturn(violationBuilderMock);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilderMock =
            mock(
                ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(violationBuilderMock.addPropertyNode(any())).thenReturn(nodeBuilderMock);
    }

    @Test
    public void shouldValid_whenMinimumIsLessThanMaximum() throws Exception {
        // given
        PasswordRequirements requirements = PasswordRequirements.builder()
            .minimumLength(1)
            .maximumLength(7)
            .build();

        // when
        boolean validationResult = validator.isValid(requirements, contextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldValid_whenMinimumIsEqualToMaximum() throws Exception {
        // given
        PasswordRequirements requirements = PasswordRequirements.builder()
            .minimumLength(6)
            .maximumLength(6)
            .build();

        // when
        boolean validationResult = validator.isValid(requirements, contextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldNotValid_whenMinimumIsMoreThanMaximum() throws Exception {
        // given
        PasswordRequirements requirements = PasswordRequirements.builder()
            .minimumLength(5)
            .maximumLength(3)
            .build();

        // when
        boolean validationResult = validator.isValid(requirements, contextMock);

        // then
        assertThat(validationResult).isFalse();
    }

}