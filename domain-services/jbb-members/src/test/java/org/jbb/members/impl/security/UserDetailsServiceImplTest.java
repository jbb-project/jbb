/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.security;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.password.PasswordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

    @Mock
    private MemberService memberServiceMock;

    @Mock
    private PasswordService passwordServiceMock;

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
    public void shouldThrowUsernameNotFoundException_whenUsernameNotFoundInMemberService()
        throws Exception {
        // given
        Username username = Username.builder().value("john").build();

        given(memberServiceMock.getMemberWithUsername(eq(username))).willReturn(Optional.empty());

        // when
        userDetailsService.loadUserByUsername(username.getValue());

        // then
        // throw UsernameNotFoundException
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundException_whenUsernameNotFoundInPasswordRepository()
        throws Exception {
        // given
        Long id = 233L;
        Username username = Username.builder().value("john").build();

        Member memberMock = mock(Member.class);
        given(memberMock.getId()).willReturn(id);
        given(memberServiceMock.getMemberWithUsername(eq(username)))
            .willReturn(Optional.of(memberMock));
        given(passwordServiceMock.getPasswordHash(eq(id))).willReturn(Optional.empty());

        // when
        userDetailsService.loadUserByUsername(username.getValue());

        // then
        // throw UsernameNotFoundException
    }

}