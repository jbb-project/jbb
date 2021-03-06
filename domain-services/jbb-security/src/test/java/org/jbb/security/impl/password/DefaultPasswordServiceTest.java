/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.collect.Sets;
import java.util.Optional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordPolicy;
import org.jbb.security.event.PasswordChangedEvent;
import org.jbb.security.event.PasswordPolicyChangedEvent;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPasswordServiceTest {
    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private PasswordRepository passwordRepositoryMock;

    @Mock
    private PasswordEntityFactory passwordEntityFactoryMock;

    @Mock
    private PasswordPolicyManager passwordPolicyManagerMock;

    @Mock
    private Validator validatorMock;

    @Mock
    private JbbEventBus eventBusMock;

    @InjectMocks
    private DefaultPasswordService passwordService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdForChangePasswordPassed() {
        // given
        Long memberId = null;
        Password password = Password.builder().value("any".toCharArray()).build();

        // when
        passwordService.changeFor(memberId, password);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPasswordForChangePasswordPassed() {
        // given
        Long memberId = 233L;
        Password password = null;

        // when
        passwordService.changeFor(memberId, password);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberId_whenGettingPasswordHash() {
        // when
        passwordService.getPasswordHash(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenSomethingWrongAfterValidation() {
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
    public void shouldPersistNewPassword_whenValidationPassed() {
        // given
        Long memberId = 233L;
        Password password = Password.builder().value("myPassword1".toCharArray()).build();

        PasswordEntity passwordEntityMock = mock(PasswordEntity.class);
        given(passwordEntityFactoryMock.create(any(), any())).willReturn(passwordEntityMock);
        given(validatorMock.validate(any(PasswordEntity.class))).willReturn(Sets.newHashSet());

        // when
        passwordService.changeFor(memberId, password);

        // then
        verify(passwordRepositoryMock, times(1)).save(any(PasswordEntity.class));
    }

    @Test
    public void shouldPublishEventAboutPassChange_whenPersisted_andItIsNotFirstChange() {
        // given
        Long memberId = 233L;
        Password password = Password.builder().value("myPassword1".toCharArray()).build();
        PasswordEntity passwordEntityMock = mock(PasswordEntity.class);

        given(passwordEntityMock.getMemberId()).willReturn(memberId);
        given(passwordEntityFactoryMock.create(any(), any())).willReturn(passwordEntityMock);
        given(validatorMock.validate(any(PasswordEntity.class))).willReturn(Sets.newHashSet());
        given(passwordRepositoryMock.countByMemberId(eq(memberId))).willReturn(1L);

        // when
        passwordService.changeFor(memberId, password);

        // then
        verify(eventBusMock, times(1)).post(any(PasswordChangedEvent.class));
    }

    @Test
    public void shouldNotPublishEventAboutPassChange_whenPersisted_andItIsFirstChange() {
        // given
        Long memberId = 233L;
        Password password = Password.builder().value("myPassword1".toCharArray()).build();
        PasswordEntity passwordEntityMock = mock(PasswordEntity.class);

        given(passwordEntityFactoryMock.create(any(), any())).willReturn(passwordEntityMock);
        given(validatorMock.validate(any(PasswordEntity.class))).willReturn(Sets.newHashSet());
        given(passwordRepositoryMock.countByMemberId(eq(memberId))).willReturn(0L);

        // when
        passwordService.changeFor(memberId, password);

        // then
        verifyZeroInteractions(eventBusMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdForVerificationPasswordPassed() {
        // given
        Long memberId = null;
        Password typedPassword = Password.builder().value("any".toCharArray()).build();

        // when
        passwordService.verifyFor(memberId, typedPassword);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPasswordForVerificationPasswordPassed() {
        // given
        Long memberId = 233L;
        Password typedPassword = null;

        // when
        passwordService.verifyFor(memberId, typedPassword);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldFailVerification_whenMemberNotFound() {
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
    public void shouldReturnPasswordHash_whenMemberFound() {
        // given
        Long memberId = 233L;
        PasswordEntity passwordEntity = PasswordEntity.builder().password("passwd").build();

        given(passwordRepositoryMock.findTheNewestByMemberId(eq(memberId)))
                .willReturn(Optional.of(passwordEntity));

        // when
        Optional<String> passwordHash = passwordService.getPasswordHash(memberId);

        // then
        assertThat(passwordHash).isPresent();
        assertThat(passwordHash.get()).isEqualTo("passwd");
    }

    @Test
    public void shouldOptionalEmptyWithPasswordHash_whenMemberNotFound() {
        // given
        Long memberId = 233L;

        given(passwordRepositoryMock.findTheNewestByMemberId(eq(memberId)))
                .willReturn(Optional.empty());

        // when
        Optional<String> passwordHash = passwordService.getPasswordHash(memberId);

        // then
        assertThat(passwordHash).isEmpty();
    }

    @Test
    public void shouldReturnCurrentPassPolicy_accordingToCurrentPolicies() {
        // when
        passwordService.currentPolicy();

        // then
        verify(passwordPolicyManagerMock, times(1)).currentPolicy();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullNewPolicyPassed() {
        // when
        passwordService.updatePolicy(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldUsePolicy_whenUpdatePolicyInvoked() {
        // when
        PasswordPolicy newPassPolicy = new PasswordPolicy();
        passwordService.updatePolicy(newPassPolicy);

        // then
        verify(passwordPolicyManagerMock, times(1)).update(eq(newPassPolicy));
    }

    @Test
    public void shouldPublishEventAboutPasswordPolicyChange_whenChanged() {
        // when
        PasswordPolicy newPassPolicy = new PasswordPolicy();
        passwordService.updatePolicy(newPassPolicy);

        // then
        verify(eventBusMock, times(1)).post(any(PasswordPolicyChangedEvent.class));
    }
}