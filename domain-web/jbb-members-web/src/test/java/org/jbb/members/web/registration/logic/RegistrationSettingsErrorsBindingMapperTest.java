/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.registration.logic;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationSettingsErrorsBindingMapperTest {

    @InjectMocks
    RegistrationSettingsErrorsBindingMapper bindingMapper;

    @Test
    public void shouldProcessMinimumLengthViolation() throws Exception {
        // given
        ConstraintViolation<?> minimumLengthViolation = getViolation("minimumLength");
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        bindingMapper.map(Sets.newHashSet(minimumLengthViolation), bindingResult);

        // then
        verify(bindingResult, times(1))
                .rejectValue(eq("minPassLength"), any(), any());
    }

    @Test
    public void shouldProcessMaximumLengthViolation() throws Exception {
        // given
        ConstraintViolation<?> minimumLengthViolation = getViolation("maximumLength");
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        bindingMapper.map(Sets.newHashSet(minimumLengthViolation), bindingResult);

        // then
        verify(bindingResult, times(1))
                .rejectValue(eq("maxPassLength"), any(), any());
    }

    @Test
    public void shouldProcessAnotherViolationWithoutchanges() throws Exception {
        // given
        ConstraintViolation<?> minimumLengthViolation = getViolation("anotherProperty");
        BindingResult bindingResult = mock(BindingResult.class);

        // when
        bindingMapper.map(Sets.newHashSet(minimumLengthViolation), bindingResult);

        // then
        verify(bindingResult, times(1))
                .rejectValue(eq("anotherProperty"), any(), any());
    }

    private ConstraintViolation<?> getViolation(String property) {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path propertyPath = mock(Path.class);
        given(propertyPath.toString()).willReturn(property);
        given(violation.getPropertyPath()).willReturn(propertyPath);
        return violation;
    }
}