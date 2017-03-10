/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.logic;

import com.google.common.collect.Sets;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class SimpleErrorsBindingMapperTest {

    @Test
    public void testBindingMapper() throws Exception {
        // given
        BindingResult bindingResultMock = mock(BindingResult.class);

        ConstraintViolation firstConstraintViolation = mock(ConstraintViolation.class);
        Path firstPropertyPath = mock(Path.class);
        given(firstPropertyPath.toString()).willReturn("first");
        given(firstConstraintViolation.getPropertyPath()).willReturn(firstPropertyPath);
        given(firstConstraintViolation.getMessage()).willReturn("firstMsg");

        ConstraintViolation secondConstraintViolation = mock(ConstraintViolation.class);
        Path secondPropertyPath = mock(Path.class);
        given(secondPropertyPath.toString()).willReturn("second");
        given(secondConstraintViolation.getPropertyPath()).willReturn(secondPropertyPath);
        given(secondConstraintViolation.getMessage()).willReturn("secondMsg");

        // when
        new SimpleErrorsBindingMapper().map(Sets.newHashSet(firstConstraintViolation, secondConstraintViolation), bindingResultMock);

        // then
        Mockito.verify(bindingResultMock, times(1))
                .rejectValue(eq("first"), anyString(), eq("firstMsg"));


        Mockito.verify(bindingResultMock, times(1))
                .rejectValue(eq("second"), anyString(), eq("secondMsg"));
    }
}