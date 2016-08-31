/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.entities.validation;

import org.jbb.lib.core.vo.Login;
import org.jbb.members.dao.MemberRepository;
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
public class LoginNotBusyValidatorTest {
    private static final ConstraintValidatorContext ANY_CONTEXT = null;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private Login login;

    @InjectMocks
    private LoginNotBusyValidator validator;

    @Test
    public void shouldPass_whenNoGivenLogin() throws Exception {
        // given
        Mockito.when(memberRepositoryMock.countByLogin(any(Login.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(login, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenLoginExists() throws Exception {
        // given
        Mockito.when(memberRepositoryMock.countByLogin(any(Login.class))).thenReturn(1L);

        // when
        boolean validationResult = validator.isValid(login, ANY_CONTEXT);

        // then
        assertThat(validationResult).isFalse();
    }

}