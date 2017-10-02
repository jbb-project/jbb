/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration.logic;

import com.google.common.collect.Sets;

import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.impl.registration.data.PasswordPair;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
        Long anyId = 44L;
        given(validatorMock.validate(any(PasswordPair.class)))
                .willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        passwordSaver.save(mock(RegistrationRequest.class), anyId);

        // then
        // throw PasswordException
    }

    @Test
    public void shouldInvokePasswordService_whenValidationPassed() throws Exception {
        // given
        Long id = 3934L;
        Username username = Username.builder().value("john").build();
        Password newPass = Password.builder().value("encrypt".toCharArray()).build();

        given(validatorMock.validate(any(PasswordPair.class)))
                .willReturn(Sets.newHashSet());

        RegistrationRequest registrationRequest = mock(RegistrationRequest.class);
        given(registrationRequest.getPassword()).willReturn(newPass);

        // when
        passwordSaver.save(registrationRequest, id);

        // then
        Mockito.verify(passwordServiceMock, times(1)).changeFor(eq(id), eq(newPass));
    }
}