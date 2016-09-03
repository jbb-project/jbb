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

import org.jbb.lib.core.vo.Login;
import org.jbb.security.dao.AdministratorRepository;
import org.jbb.security.entities.AdministratorEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplTest {
    @Mock
    private AdministratorRepository adminRepositoryMock;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullLoginPassed_intoHasAdministratorRole() throws Exception {
        // when
        roleService.hasAdministratorRole(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullLoginPassed_intoAddAdministratorRole() throws Exception {
        // when
        roleService.addAdministratorRole(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldConfirmAdministratorRoleForLogin_whenRepositoryReturnsEntityWithGivenLogin() throws Exception {
        // given
        Login login = mock(Login.class);
        given(adminRepositoryMock.findByLogin(eq(login))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        boolean hasAdministratorRole = roleService.hasAdministratorRole(login);

        // then
        assertThat(hasAdministratorRole).isTrue();
    }

    @Test
    public void shouldDenyAdministratorRoleForLogin_whenRepositoryNotReturnEntityWithGivenLogin() throws Exception {
        // given
        Login login = mock(Login.class);
        given(adminRepositoryMock.findByLogin(eq(login))).willReturn(Optional.empty());

        // when
        boolean hasAdministratorRole = roleService.hasAdministratorRole(login);

        // then
        assertThat(hasAdministratorRole).isFalse();
    }

    @Test
    public void shouldSaveAdministratorRoleForLogin_whenLoginHasNotYet() throws Exception {
        // given
        Login login = mock(Login.class);
        given(adminRepositoryMock.findByLogin(eq(login))).willReturn(Optional.empty());

        // when
        roleService.addAdministratorRole(login);

        // then
        verify(adminRepositoryMock, times(1)).save(any(AdministratorEntity.class));
    }

    @Test
    public void shouldNotSaveAdministratorRoleForLoginAgain_whenLoginHasItAlready() throws Exception {
        // given
        Login login = mock(Login.class);
        given(adminRepositoryMock.findByLogin(eq(login))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        roleService.addAdministratorRole(login);

        // then
        verify(adminRepositoryMock, times(0)).save(any(AdministratorEntity.class));
    }
}