/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;

import com.google.common.collect.Sets;

import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.password.PasswordRequirements;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.event.PasswordChangedEvent;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
    public void shouldThrowNPE_whenNullMemberIdForChangePasswordPassed() throws Exception {
        // given
        Long memberId = null;
        Password password = Password.builder().value("any".toCharArray()).build();

        // when
        passwordService.changeFor(memberId, password);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPasswordForChangePasswordPassed() throws Exception {
        // given
        Long memberId = 233L;
        Password password = null;

        // when
        passwordService.changeFor(memberId, password);

        // then
        // throw NullPointerException
    }

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenSomethingWrongAfterValidation() throws Exception {
        // given
        Long memberId = 233L;
        Password password = Password.builder().value("myPassword1".toCharArray()).build();
        given(passwordEntityFactoryMock.create(any(), any())).willReturn(PasswordEntity.builder().memberId(123L).build());
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        passwordService.changeFor(memberId, password);

        // then
        // throw PasswordException
    }

    @Test
    public void shouldPersistNewPassword_whenValidationPassed() throws Exception {
        // given
        Long memberId = 233L;
        Password password = Password.builder().value("myPassword1".toCharArray()).build();

        PasswordEntity passwordEntityMock = mock(PasswordEntity.class);
        given(passwordEntityMock.getMemberId()).willReturn(memberId);
        given(passwordEntityFactoryMock.create(any(), any())).willReturn(passwordEntityMock);
        given(validatorMock.validate(any(PasswordEntity.class))).willReturn(Sets.newHashSet());

        // when
        passwordService.changeFor(memberId, password);

        // then
        verify(passwordRepositoryMock, times(1)).save(any(PasswordEntity.class));
    }

    @Test
    public void shouldPublishEventAboutPassChange_whenPersisted() throws Exception {
        // given
        Long memberId = 233L;
        Password password = Password.builder().value("myPassword1".toCharArray()).build();
        PasswordEntity passwordEntityMock = mock(PasswordEntity.class);

        given(passwordEntityMock.getMemberId()).willReturn(memberId);
        given(passwordEntityFactoryMock.create(any(), any())).willReturn(passwordEntityMock);
        given(validatorMock.validate(any(PasswordEntity.class))).willReturn(Sets.newHashSet());

        // when
        passwordService.changeFor(memberId, password);

        // then
        verify(eventBusMock, times(1)).post(any(PasswordChangedEvent.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdForVerificationPasswordPassed() throws Exception {
        // given
        Long memberId = null;
        Password typedPassword = Password.builder().value("any".toCharArray()).build();

        // when
        passwordService.verifyFor(memberId, typedPassword);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPasswordForVerificationPasswordPassed() throws Exception {
        // given
        Long memberId = 233L;
        Password typedPassword = null;

        // when
        passwordService.verifyFor(memberId, typedPassword);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldFailVerification_whenMemberNotFound() throws Exception {
        // given
        Long memberId = 233L;
        Password typedPassword = Password.builder().value("incorrectpass".toCharArray()).build();

        given(passwordRepositoryMock.findTheNewestByMemberId(eq(memberId)))
                .willReturn(Optional.empty());

        // when
        boolean verificationResult = passwordService.verifyFor(memberId, typedPassword);

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