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
import org.jbb.lib.core.vo.Username;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsernameNotBusyValidatorTest {
    private static final ConstraintValidatorContext ANY_CONTEXT = null;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private UserDetailsSource userDetailsSourceMock;

    @Mock
    private UserDetails userDetailsMock;

    @Mock
    private Username username;

    @InjectMocks
    private UsernameNotBusyValidator validator;

    @Before
    public void setUp() throws Exception {
        when(username.getValue()).thenReturn("foo");
    }

    @Test
    public void shouldPass_whenNoGivenUsername() throws Exception {
        // given
        when(memberRepositoryMock.countByUsername(any(Username.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(username, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenSingleUsernameExists_butItIsUsernameOfCurrentUser() throws Exception {
        // given
        when(memberRepositoryMock.countByUsername(any(Username.class))).thenReturn(1L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("foo");

        // when
        boolean validationResult = validator.isValid(username, ANY_CONTEXT);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenSingleUsernameExists_butItIsNotUsernameOfCurrentUser() throws Exception {
        // given
        when(memberRepositoryMock.countByUsername(any(Username.class))).thenReturn(1L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("bar");

        // when
        boolean validationResult = validator.isValid(username, ANY_CONTEXT);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void shouldFail_whenSingleUsernameExists_undUserIsNotAuthenticated() throws Exception {
        // given
        when(memberRepositoryMock.countByUsername(any(Username.class))).thenReturn(1L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(null);

        // when
        boolean validationResult = validator.isValid(username, ANY_CONTEXT);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void shouldFail_whenMoreThanOneUsernameExists() throws Exception {
        // given
        when(memberRepositoryMock.countByUsername(any(Username.class))).thenReturn(2L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("foo");

        // when
        boolean validationResult = validator.isValid(username, ANY_CONTEXT);

        // then
        assertThat(validationResult).isFalse();
    }

}