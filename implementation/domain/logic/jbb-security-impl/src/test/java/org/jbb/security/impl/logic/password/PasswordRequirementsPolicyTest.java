/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.logic.password;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbb.security.api.data.PasswordRequirements;
import org.jbb.security.impl.password.logic.PasswordRequirementsPolicy;
import org.jbb.security.impl.password.logic.UpdateAwarePasswordRequirements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PasswordRequirementsPolicyTest {
    @Mock
    private UpdateAwarePasswordRequirements currentRequirementsMock;

    @InjectMocks
    private PasswordRequirementsPolicy passwordRequirementsPolicy;

    @Test
    public void shouldReturnCurrentPasswordRequirements() throws Exception {
        // when
        PasswordRequirements requirements = passwordRequirementsPolicy.currentRequirements();

        // then
        assertThat(requirements).isEqualTo(currentRequirementsMock);
    }

    @Test
    public void shouldInvokeUpdateOfRequirements_whenUpdatePolicyPerformed() throws Exception {
        // given
        PasswordRequirements newPasswordRequirements = mock(PasswordRequirements.class);

        // when
        passwordRequirementsPolicy.update(newPasswordRequirements);

        // then
        verify(currentRequirementsMock, times(1)).update(eq(newPasswordRequirements));
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
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.empty());
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.empty());

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMinimumLengthIsSix_andFiveCharactersLongPassword() throws Exception {
        // given
        String password = "12345";
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.of(6));
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.empty());

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenMinimumLengthIsSix_andSixCharactersLongPassword() throws Exception {
        // given
        String password = "123456";
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.of(6));
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.empty());

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldMeetCriteria_whenMinimumLengthIsSix_andSevenCharactersLongPassword() throws Exception {
        // given
        String password = "1234567";
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.of(6));
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.empty());

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMaximumLengthIsNine_andTenCharactersLongPassword() throws Exception {
        // given
        String password = "1234567890";
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.empty());
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.of(9));

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldNotMeetCriteria_whenMaximumLengthIsNine_andNineCharactersLongPassword() throws Exception {
        // given
        String password = "123456789";
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.empty());
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.of(9));

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMaximumLengthIsNine_andEightCharactersLongPassword() throws Exception {
        // given
        String password = "12345678";
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.empty());
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.of(9));

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMinimumLengthIsSeven_andMaximumLengthIsSeven_andSixCharactersLongPassword() throws Exception {
        // given
        String password = "123456";
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.of(7));
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.of(7));

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenMinimumLengthIsSeven_andMaximumLengthIsSeven_andSevenCharactersLongPassword() throws Exception {
        // given
        String password = "1234567";
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.of(7));
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.of(7));

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }

    @Test
    public void shouldNotMeetCriteria_whenMinimumLengthIsSeven_andMaximumLengthIsSeven_andEightCharactersLongPassword() throws Exception {
        // given
        String password = "12345678";
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.of(7));
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.of(7));

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isFalse();
    }

    @Test
    public void shouldMeetCriteria_whenNoMaximumLength_andReallyLongPassword() throws Exception {
        // given
        String password = RandomStringUtils.randomAlphabetic(10000);
        given(currentRequirementsMock.minimumLength()).willReturn(Optional.empty());
        given(currentRequirementsMock.maximumLength()).willReturn(Optional.empty());

        // when
        boolean meet = passwordRequirementsPolicy.assertMeetCriteria(password);

        // then
        assertThat(meet).isTrue();
    }
}