/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation;

import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class DisplayedNameNotBusyValidatorTest {
    private static final ConstraintValidatorContext ANY_CONTEXT = null;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private DisplayedName displayedName;

    @InjectMocks
    private DisplayedNameNotBusyValidator validator;

    @Test
    public void shouldPass_whenNoGivenDisplayedName() throws Exception {
        // given
        Mockito.when(memberRepositoryMock.countByDisplayedName(any(DisplayedName.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(displayedName, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenLoginExists() throws Exception {
        // given
        Mockito.when(memberRepositoryMock.countByDisplayedName(any(DisplayedName.class))).thenReturn(1L);

        // when
        boolean validationResult = validator.isValid(displayedName, ANY_CONTEXT);

        // then
        assertThat(validationResult).isFalse();
    }

}