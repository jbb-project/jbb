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

import com.google.common.collect.Sets;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordPolicy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PasswordPolicyManagerTest {

    @Mock
    private PasswordProperties propertiesMock;

    @Mock
    private Validator validatorMock;

    @InjectMocks
    private PasswordPolicyManager passwordPolicyManager;

    @Test
    public void shouldReturnCurrentPasswordPolicy() {
        // when
        PasswordPolicy passwordPolicy = passwordPolicyManager.currentPolicy();

        // then
        assertThat(passwordPolicy).isNotNull();
    }

    @Test
    public void shouldInvokeValidationPolicy_whenUpdatePolicyPerformed() {
        // given
        PasswordPolicy passwordPolicy = new PasswordPolicy();
        passwordPolicy.setMinimumLength(4);
        passwordPolicy.setMaximumLength(16);

        // when
        passwordPolicyManager.update(passwordPolicy);

        // then
        verify(validatorMock).validate(eq(passwordPolicy));
    }


    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenPolicyValidationFailed() {
        // given
        given(validatorMock.validate(any())).willReturn(
                Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        passwordPolicyManager.update(mock(PasswordPolicy.class));

        // then
        // throw PasswordException
    }

    @Test
    public void shouldUpdateMinimumLengthProperty_whenMinLengthPassedThroughNewPolicy() {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        PasswordPolicy passwordPolicy = PasswordPolicy.builder()
                .minimumLength(6)
                .maximumLength(16)
                .build();

        // when
        passwordPolicyManager.update(passwordPolicy);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MIN_LENGTH_KEY), eq(Integer.toString(6)));
    }

    @Test
    public void shouldUpdateMaximumLengthProperty_whenMaxLengthPassedThroughNewPolicy() {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        PasswordPolicy policy = PasswordPolicy.builder()
                .minimumLength(6)
                .maximumLength(10)
                .build();

        // when
        passwordPolicyManager.update(policy);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MAX_LENGTH_KEY), eq(Integer.toString(10)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPassword() {
        // when
        passwordPolicyManager.assertMeetCriteria(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldNotMeetCriteriaAnytime_whenEmptyPassword() {
        // given
        String emptyPassword = StringUtils.EMPTY;

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(emptyPassword);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenNoMinimumLength_andOneCharLongPassword() {
        // given
        String password = "a";
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMinimumLengthIsSix_andFiveCharactersLongPassword() {
        // given
        String password = "12345";
        given(propertiesMock.passwordMinimumLength()).willReturn(6);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenMinimumLengthIsSix_andSixCharactersLongPassword() {
        // given
        String password = "123456";
        given(propertiesMock.passwordMinimumLength()).willReturn(6);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldMeetCriteria_whenMinimumLengthIsSix_andSevenCharactersLongPassword() {
        // given
        String password = "1234567";
        given(propertiesMock.passwordMinimumLength()).willReturn(6);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMaximumLengthIsNine_andTenCharactersLongPassword() {
        // given
        String password = "1234567890";
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(9);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldNotMeetCriteria_whenMaximumLengthIsNine_andNineCharactersLongPassword() {
        // given
        String password = "123456789";
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(9);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMaximumLengthIsNine_andEightCharactersLongPassword() {
        // given
        String password = "12345678";
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(9);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMinimumLengthIsSeven_andMaximumLengthIsSeven_andSixCharactersLongPassword() {
        // given
        String password = "123456";
        given(propertiesMock.passwordMinimumLength()).willReturn(7);
        given(propertiesMock.passwordMaximumLength()).willReturn(7);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenMinimumLengthIsSeven_andMaximumLengthIsSeven_andSevenCharactersLongPassword() {
        // given
        String password = "1234567";
        given(propertiesMock.passwordMinimumLength()).willReturn(7);
        given(propertiesMock.passwordMaximumLength()).willReturn(7);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMinimumLengthIsSeven_andMaximumLengthIsSeven_andEightCharactersLongPassword() {
        // given
        String password = "12345678";
        given(propertiesMock.passwordMinimumLength()).willReturn(7);
        given(propertiesMock.passwordMaximumLength()).willReturn(7);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenNoMaximumLength_andReallyLongPassword() {
        // given
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                .filteredBy(CharacterPredicates.LETTERS).build();
        String password = randomStringGenerator.generate(10000);
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordPolicyManager.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }
}