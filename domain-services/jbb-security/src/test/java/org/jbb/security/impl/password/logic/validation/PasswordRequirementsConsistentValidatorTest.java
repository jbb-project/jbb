/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic.validation;


import org.jbb.security.impl.password.logic.PasswordRequirementsImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PasswordRequirementsConsistentValidatorTest {
    @Mock
    private ConstraintValidatorContext contextMock;

    private PasswordRequirementsConsistentValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new PasswordRequirementsConsistentValidator();
        validator.initialize(null);

        ConstraintValidatorContext.ConstraintViolationBuilder violationBuilderMock =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(contextMock.buildConstraintViolationWithTemplate(any()))
                .thenReturn(violationBuilderMock);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilderMock =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(violationBuilderMock.addPropertyNode(any())).thenReturn(nodeBuilderMock);
    }

    @Test
    public void shouldValid_whenMinimumIsLessThanMaximum() throws Exception {
        // given
        PasswordRequirementsImpl passwordRequirementsMock = mock(PasswordRequirementsImpl.class);
        given(passwordRequirementsMock.getMinimumLength()).willReturn(1);
        given(passwordRequirementsMock.getMaximumLength()).willReturn(7);

        // when
        boolean validationResult = validator.isValid(passwordRequirementsMock, contextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldValid_whenMinimumIsEqualToMaximum() throws Exception {
        // given
        PasswordRequirementsImpl passwordRequirementsMock = mock(PasswordRequirementsImpl.class);
        given(passwordRequirementsMock.getMinimumLength()).willReturn(6);
        given(passwordRequirementsMock.getMaximumLength()).willReturn(6);

        // when
        boolean validationResult = validator.isValid(passwordRequirementsMock, contextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldNotValid_whenMinimumIsMoreThanoMaximum() throws Exception {
        // given
        PasswordRequirementsImpl passwordRequirementsMock = mock(PasswordRequirementsImpl.class);
        given(passwordRequirementsMock.getMinimumLength()).willReturn(8);
        given(passwordRequirementsMock.getMaximumLength()).willReturn(1);

        // when
        boolean validationResult = validator.isValid(passwordRequirementsMock, contextMock);

        // then
        assertThat(validationResult).isFalse();
    }
}