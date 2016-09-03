/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.services;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Login;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Member;
import org.jbb.members.api.services.MemberService;
import org.jbb.security.api.services.RoleService;
import org.jbb.security.dao.PasswordRepository;
import org.jbb.security.entities.PasswordEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {
    @Mock
    private MemberService memberServiceMock;

    @Mock
    private RoleService roleServiceMock;

    @Mock
    private PasswordRepository passwordRepositoryMock;

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

    @Test
    public void shouldReturnUserDetailsWithAdminRole_whenResponseFromRoleServiceIsPositive() throws Exception {
        // given
        Login login = prepareMocks();

        given(roleServiceMock.hasAdministratorRole(eq(login))).willReturn(true);

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(login.getValue());

        // then
        assertThat(userDetails.getAuthorities()).contains(new SimpleGrantedAuthority(UserDetailsServiceImpl.ADMIN_ROLE_NAME));
    }

    @Test
    public void shouldReturnUserDetailsWithoutAdminRole_whenResponseFromRoleServiceIsNegative() throws Exception {
        // given
        Login login = prepareMocks();

        given(roleServiceMock.hasAdministratorRole(eq(login))).willReturn(false);

        // when
        UserDetails userDetails = userDetailsService.loadUserByUsername(login.getValue());

        // then
        assertThat(userDetails.getAuthorities()).doesNotContain(new SimpleGrantedAuthority(UserDetailsServiceImpl.ADMIN_ROLE_NAME));
    }

    private Login prepareMocks() {
        Login login = Login.builder().value("john").build();

        PasswordEntity pswdEntityMock = mock(PasswordEntity.class);
        given(pswdEntityMock.getLogin()).willReturn(login);
        given(pswdEntityMock.getPassword()).willReturn("encodedPass");
        given(passwordRepositoryMock.findTheNewestByLogin(eq(login))).willReturn(Optional.of(pswdEntityMock));

        Member memberMock = mock(Member.class);
        given(memberMock.getDisplayedName()).willReturn(DisplayedName.builder().value("john").build());
        given(memberServiceMock.getMemberWithLogin(eq(login))).willReturn(Optional.of(memberMock));
        return login;
    }
}