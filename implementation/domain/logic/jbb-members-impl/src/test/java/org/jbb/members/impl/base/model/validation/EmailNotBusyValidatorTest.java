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

import org.jbb.lib.core.vo.Email;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.data.MembersProperties;
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
public class EmailNotBusyValidatorTest {
    private static final ConstraintValidatorContext ANY_CONTEXT = null;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private MembersProperties propertiesMock;

    @Mock
    private Email email;

    @InjectMocks
    private EmailNotBusyValidator validator;

    @Test
    public void shouldPass_whenDuplicationAllowed_andNoGivenEmail() throws Exception {
        // given
        Mockito.when(propertiesMock.allowEmailDuplication()).thenReturn(true);
        Mockito.when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(email, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationAllowed_andEmailExists() throws Exception {
        // given
        Mockito.when(propertiesMock.allowEmailDuplication()).thenReturn(true);
        Mockito.when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(4L);

        // when
        boolean validationResult = validator.isValid(email, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationForbidden_andNoGivenEmail() throws Exception {
        // given
        Mockito.when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        Mockito.when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(email, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenDuplicationAllowed_andEmailExists() throws Exception {
        // given
        Mockito.when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        Mockito.when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(4L);

        // when
        boolean validationResult = validator.isValid(email, ANY_CONTEXT);

        // then
        assertThat(validationResult).isFalse();
    }
}