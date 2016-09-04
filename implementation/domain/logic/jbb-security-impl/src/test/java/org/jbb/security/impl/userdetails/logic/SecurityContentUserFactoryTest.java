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

import org.jbb.lib.core.vo.Login;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.Member;
import org.jbb.security.api.service.RoleService;
import org.jbb.security.impl.password.model.PasswordEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SecurityContentUserFactoryTest {
    @Mock
    private RoleService roleServiceMock;

    @InjectMocks
    private SecurityContentUserFactory securityContentUserFactory;

    @Test
    public void shouldReturnUserDetailsWithAdminRole_whenResponseFromRoleServiceIsPositive() throws Exception {
        // given
        PasswordEntity passwordEntity = preparePasswordEntity();
        Member member = prepareMember();

        given(roleServiceMock.hasAdministratorRole(eq(passwordEntity.getLogin()))).willReturn(true);

        // when
        UserDetails userDetails = securityContentUserFactory.create(passwordEntity, member);

        // then
        assertThat(userDetails.getAuthorities()).contains(new SimpleGrantedAuthority(SecurityContentUserFactory.ADMIN_ROLE_NAME));
    }

    @Test
    public void shouldReturnUserDetailsWithoutAdminRole_whenResponseFromRoleServiceIsNegative() throws Exception {
        // given
        PasswordEntity passwordEntity = preparePasswordEntity();
        Member member = prepareMember();

        given(roleServiceMock.hasAdministratorRole(eq(passwordEntity.getLogin()))).willReturn(false);

        // when
        UserDetails userDetails = securityContentUserFactory.create(passwordEntity, member);

        // then
        assertThat(userDetails.getAuthorities()).doesNotContain(new SimpleGrantedAuthority(SecurityContentUserFactory.ADMIN_ROLE_NAME));
    }

    private PasswordEntity preparePasswordEntity() {
        Login login = Login.builder().value("john").build();

        PasswordEntity pswdEntityMock = mock(PasswordEntity.class);
        given(pswdEntityMock.getLogin()).willReturn(login);
        given(pswdEntityMock.getPassword()).willReturn("encodedPass");

        return pswdEntityMock;
    }

    private Member prepareMember() {
        Member memberMock = mock(Member.class);
        given(memberMock.getDisplayedName()).willReturn(DisplayedName.builder().value("John").build());
        return memberMock;
    }

}