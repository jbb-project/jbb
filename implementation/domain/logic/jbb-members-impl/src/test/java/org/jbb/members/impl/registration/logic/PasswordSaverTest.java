/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration.logic;

import com.google.common.collect.Sets;

import org.jbb.lib.core.vo.Login;
import org.jbb.lib.core.vo.Password;
import org.jbb.members.api.data.RegistrationRequest;
import org.jbb.members.impl.registration.data.PasswordPair;
import org.jbb.security.api.exception.PasswordException;
import org.jbb.security.api.service.PasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class PasswordSaverTest {
    @Mock
    private PasswordService passwordServiceMock;

    @Mock
    private Validator validatorMock;

    @InjectMocks
    private PasswordSaver passwordSaver;

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenValidationFailed() throws Exception {
        // given
        given(validatorMock.validate(any(PasswordPair.class)))
                .willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        passwordSaver.save(mock(RegistrationRequest.class));

        // then
        // throw PasswordException
    }

    @Test
    public void shouldInvokePasswordService_whenValidationPassed() throws Exception {
        // given
        Login login = Login.builder().value("john").build();
        Password newPass = Password.builder().value("encrypt".toCharArray()).build();

        given(validatorMock.validate(any(PasswordPair.class)))
                .willReturn(Sets.newHashSet());

        RegistrationRequest registrationRequest = mock(RegistrationRequest.class);
        given(registrationRequest.getLogin()).willReturn(login);
        given(registrationRequest.getPassword()).willReturn(newPass);

        // when
        passwordSaver.save(registrationRequest);

        // then
        Mockito.verify(passwordServiceMock, times(1)).changeFor(eq(login), eq(newPass));
    }
}