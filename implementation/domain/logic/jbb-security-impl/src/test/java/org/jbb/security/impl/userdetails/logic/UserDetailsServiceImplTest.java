/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.userdetails.logic;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Login;
import org.jbb.members.api.service.MemberService;
import org.jbb.security.impl.password.dao.PasswordRepository;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {
    @Mock
    private MemberService memberServiceMock;

    @Mock
    private PasswordRepository passwordRepositoryMock;

    @Mock
    private SecurityContentUserFactory securityContentUserFactoryMock;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundException_whenNullUsernamePassed() throws Exception {
        // when
        userDetailsService.loadUserByUsername(null);

        // then
        // throw UsernameNotFoundException
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundException_whenEmptyUsernamePassed() throws Exception {
        // when
        userDetailsService.loadUserByUsername(StringUtils.EMPTY);

        // then
        // throw UsernameNotFoundException
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundException_whenUsernameNotFoundInPasswordRepository() throws Exception {
        // given
        Login login = Login.builder().value("john").build();

        given(passwordRepositoryMock.findTheNewestByLogin(eq(login))).willReturn(Optional.empty());

        // when
        userDetailsService.loadUserByUsername(login.getValue());

        // then
        // throw UsernameNotFoundException
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundException_whenUsernameNotFoundInMemberService() throws Exception {
        // given
        Login login = Login.builder().value("john").build();

        PasswordEntity pswdEntityMock = mock(PasswordEntity.class);
        given(pswdEntityMock.getLogin()).willReturn(login);
        given(passwordRepositoryMock.findTheNewestByLogin(eq(login))).willReturn(Optional.of(pswdEntityMock));
        given(memberServiceMock.getMemberWithLogin(eq(login))).willReturn(Optional.empty());

        // when
        userDetailsService.loadUserByUsername(login.getValue());

        // then
        // throw UsernameNotFoundException
    }

}