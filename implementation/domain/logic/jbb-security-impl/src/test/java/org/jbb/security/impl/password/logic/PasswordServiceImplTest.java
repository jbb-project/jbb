/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;

import com.google.common.collect.Sets;

import org.jbb.lib.core.vo.Password;
import org.jbb.lib.core.vo.Username;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.api.exception.PasswordException;
import org.jbb.security.event.PasswordChangedEvent;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private PasswordRepository passwordRepositoryMock;

    @Mock
    private PasswordEntityFactory passwordEntityFactoryMock;

    @Mock
    private PasswordRequirementsPolicy requirementsPolicyMock;

    @Mock
    private Validator validatorMock;

    @Mock
    private JbbEventBus eventBusMock;

    @InjectMocks
    private PasswordServiceImpl passwordService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullUsernameForChangePasswordPassed() throws Exception {
        // given
        Username username = null;
        Password password = Password.builder().value("any".toCharArray()).build();

        // when
        passwordService.changeFor(username, password);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPasswordForChangePasswordPassed() throws Exception {
        // given
        Username username = Username.builder().value("john").build();
        Password password = null;

        // when
        passwordService.changeFor(username, password);

        // then
        // throw NullPointerException
    }

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenSomethingWrongAfterValidation() throws Exception {
        // given
        Username username = Username.builder().value("john").build();
        Password password = Password.builder().value("myPassword1".toCharArray()).build();

        given(validatorMock.validate(any(PasswordEntity.class)))
                .willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        passwordService.changeFor(username, password);

        // then
        // throw PasswordException
    }

    @Test
    public void shouldPersistNewPassword_whenValidationPassed() throws Exception {
        // given
        Username username = Username.builder().value("john").build();
        Password password = Password.builder().value("myPassword1".toCharArray()).build();

        given(passwordEntityFactoryMock.create(any(), any())).willReturn(mock(PasswordEntity.class));
        given(validatorMock.validate(any(PasswordEntity.class))).willReturn(Sets.newHashSet());

        // when
        passwordService.changeFor(username, password);

        // then
        verify(passwordRepositoryMock, times(1)).save(any(PasswordEntity.class));
    }

    @Test
    public void shouldPublishEventAboutPassChange_whenPersisted() throws Exception {
        // given
        Username username = Username.builder().value("john").build();
        Password password = Password.builder().value("myPassword1".toCharArray()).build();

        given(passwordEntityFactoryMock.create(any(), any())).willReturn(mock(PasswordEntity.class));
        given(validatorMock.validate(any(PasswordEntity.class))).willReturn(Sets.newHashSet());

        // when
        passwordService.changeFor(username, password);

        // then
        verify(eventBusMock, times(1)).post(any(PasswordChangedEvent.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullUsernameForVerificationPasswordPassed() throws Exception {
        // given
        Username username = null;
        Password typedPassword = Password.builder().value("any".toCharArray()).build();

        // when
        passwordService.verifyFor(username, typedPassword);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPasswordForVerificationPasswordPassed() throws Exception {
        // given
        Username username = Username.builder().value("john").build();
        Password typedPassword = null;

        // when
        passwordService.verifyFor(username, typedPassword);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldFailVerification_whenUsernameNotFound() throws Exception {
        // given
        Username username = Username.builder().value("johnvsvsfdvd").build();
        Password typedPassword = Password.builder().value("incorrectpass".toCharArray()).build();

        given(passwordRepositoryMock.findTheNewestByUsername(eq(username)))
                .willReturn(Optional.empty());

        // when
        boolean verificationResult = passwordService.verifyFor(username, typedPassword);

        // then
        assertThat(verificationResult).isFalse();
    }


    @Test
    public void shouldReturnCurrentPassRequirements_accordingToCurrentPolicies() throws Exception {
        // when
        passwordService.currentRequirements();

        // then
        verify(requirementsPolicyMock, times(1)).currentRequirements();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullNewRequirementsPassed() throws Exception {
        // when
        passwordService.updateRequirements(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldUsePolicy_whenUpdateRequirementsInvoked() throws Exception {
        // when
        PasswordRequirements newPassRequirements = mock(PasswordRequirements.class);
        passwordService.updateRequirements(newPassRequirements);

        // then
        verify(requirementsPolicyMock, times(1)).update(eq(newPassRequirements));
    }
}