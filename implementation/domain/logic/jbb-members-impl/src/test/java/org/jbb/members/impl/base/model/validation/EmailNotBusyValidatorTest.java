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

import org.jbb.lib.core.security.UserDetailsSource;
import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.service.MemberService;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.data.MembersProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmailNotBusyValidatorTest {
    private static final ConstraintValidatorContext ANY_CONTEXT = null;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private MembersProperties propertiesMock;

    @Mock
    private UserDetailsSource userDetailsSourceMock;

    @Mock
    private MemberService memberServiceMock;

    @Mock
    private UserDetails userDetailsMock;

    @Mock
    private Email email;

    @InjectMocks
    private EmailNotBusyValidator validator;

    @Test
    public void shouldPass_whenDuplicationAllowed_andNoGivenEmail() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(true);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(email, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationAllowed_andEmailExists() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(true);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(4L);

        // when
        boolean validationResult = validator.isValid(email, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationForbidden_andNoGivenEmail() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(email, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationForbidden_andEmailExists_butItIsAnEmailOfCurrentUser() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(4L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("foo");

        Member memberMock = mock(Member.class);
        when(memberServiceMock.getMemberWithUsername(any(Username.class))).thenReturn(Optional.of(memberMock));
        when(memberMock.getEmail()).thenReturn(email);

        // when
        boolean validationResult = validator.isValid(email, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenDuplicationAllowed_andEmailExists_butItIsNOTAnEmailOfCurrentUser() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(4L);

        // when
        boolean validationResult = validator.isValid(email, ANY_CONTEXT);

        // then
        assertThat(validationResult).isFalse();
    }
}