/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordRequirements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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
public class PasswordRequirementsPolicyTest {

    @Mock
    private PasswordProperties propertiesMock;

    @Mock
    private Validator validatorMock;

    @InjectMocks
    private PasswordRequirementsPolicy passwordRequirementsPolicy;

    @Test
    public void shouldReturnCurrentPasswordRequirements() throws Exception {
        // when
        PasswordRequirements requirements = passwordRequirementsPolicy.currentRequirements();

        // then
        assertThat(requirements).isNotNull();
    }

    @Test
    public void shouldInvokeValidationRequirements_whenUpdatePolicyPerformed() throws Exception {
        // given
        PasswordRequirements requirements = new PasswordRequirements();

        // when
        passwordRequirementsPolicy.update(requirements);

        // then
        verify(validatorMock).validate(eq(requirements));
    }


    @Test(expected = PasswordException.class)
    public void shouldThrowPasswordException_whenRequirementsValidationFailed() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(
                Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        passwordRequirementsPolicy.update(mock(PasswordRequirements.class));

        // then
        // throw PasswordException
    }

    @Test
    public void shouldUpdateMinimumLengthProperty_whenMinLengthPassedThroughNewRequirements()
            throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        PasswordRequirements requirements = PasswordRequirements.builder()
                .minimumLength(6)
                .maximumLength(16)
                .build();

        // when
        passwordRequirementsPolicy.update(requirements);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MIN_LENGTH_KEY), eq(Integer.toString(6)));
    }

    @Test
    public void shouldUpdateMaximumLengthProperty_whenMaxLengthPassedThroughNewRequirements()
            throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        PasswordRequirements requirements = PasswordRequirements.builder()
                .minimumLength(6)
                .maximumLength(10)
                .build();

        // when
        passwordRequirementsPolicy.update(requirements);

        // then
        verify(propertiesMock, times(1))
                .setProperty(eq(PasswordProperties.PSWD_MAX_LENGTH_KEY), eq(Integer.toString(10)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullPassword() throws Exception {
        // when
        passwordRequirementsPolicy.assertMeetCriteria(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldNotMeetCriteriaAnytime_whenEmptyPassword() throws Exception {
        // given
        String emptyPassword = StringUtils.EMPTY;

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(emptyPassword);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenNoMinimumLength_andOneCharLongPassword() throws Exception {
        // given
        String password = "a";
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMinimumLengthIsSix_andFiveCharactersLongPassword() throws Exception {
        // given
        String password = "12345";
        given(propertiesMock.passwordMinimumLength()).willReturn(6);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenMinimumLengthIsSix_andSixCharactersLongPassword() throws Exception {
        // given
        String password = "123456";
        given(propertiesMock.passwordMinimumLength()).willReturn(6);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldMeetCriteria_whenMinimumLengthIsSix_andSevenCharactersLongPassword() throws Exception {
        // given
        String password = "1234567";
        given(propertiesMock.passwordMinimumLength()).willReturn(6);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMaximumLengthIsNine_andTenCharactersLongPassword() throws Exception {
        // given
        String password = "1234567890";
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(9);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldNotMeetCriteria_whenMaximumLengthIsNine_andNineCharactersLongPassword() throws Exception {
        // given
        String password = "123456789";
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(9);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMaximumLengthIsNine_andEightCharactersLongPassword() throws Exception {
        // given
        String password = "12345678";
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(9);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMinimumLengthIsSeven_andMaximumLengthIsSeven_andSixCharactersLongPassword() throws Exception {
        // given
        String password = "123456";
        given(propertiesMock.passwordMinimumLength()).willReturn(7);
        given(propertiesMock.passwordMaximumLength()).willReturn(7);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenMinimumLengthIsSeven_andMaximumLengthIsSeven_andSevenCharactersLongPassword() throws Exception {
        // given
        String password = "1234567";
        given(propertiesMock.passwordMinimumLength()).willReturn(7);
        given(propertiesMock.passwordMaximumLength()).willReturn(7);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMinimumLengthIsSeven_andMaximumLengthIsSeven_andEightCharactersLongPassword() throws Exception {
        // given
        String password = "12345678";
        given(propertiesMock.passwordMinimumLength()).willReturn(7);
        given(propertiesMock.passwordMaximumLength()).willReturn(7);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenNoMaximumLength_andReallyLongPassword() throws Exception {
        // given
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                .filteredBy(CharacterPredicates.LETTERS).build();
        String password = randomStringGenerator.generate(10000);
        given(propertiesMock.passwordMinimumLength()).willReturn(1);
        given(propertiesMock.passwordMaximumLength()).willReturn(Integer.MAX_VALUE);

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }
}