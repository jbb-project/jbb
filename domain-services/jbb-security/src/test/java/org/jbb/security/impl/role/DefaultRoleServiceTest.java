/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.security.event.AdministratorRoleAddedEvent;
import org.jbb.security.event.AdministratorRoleRemovedEvent;
import org.jbb.security.impl.role.dao.AdministratorRepository;
import org.jbb.security.impl.role.model.AdministratorEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultRoleServiceTest {
    @Mock
    private AdministratorRepository adminRepositoryMock;

    @Mock
    private AdministratorEntityFactory adminFactoryMock;

    @Mock
    private JbbEventBus eventBusMock;


    @InjectMocks
    private DefaultRoleService roleService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdPassed_intoHasAdministratorRole() throws Exception {
        // when
        roleService.hasAdministratorRole(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdPassed_intoAddAdministratorRole() throws Exception {
        // when
        roleService.addAdministratorRole(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdPassed_intoRemoveAdministratorRole() throws Exception {
        // when
        roleService.removeAdministratorRole(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldConfirmAdministratorRoleForMember_whenRepositoryReturnsEntityWithGivenMemberId() throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        boolean hasAdministratorRole = roleService.hasAdministratorRole(memberId);

        // then
        assertThat(hasAdministratorRole).isTrue();
    }

    @Test
    public void shouldDenyAdministratorRoleForMember_whenRepositoryNotReturnEntityWithGivenMemberId() throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.empty());

        // when
        boolean hasAdministratorRole = roleService.hasAdministratorRole(memberId);

        // then
        assertThat(hasAdministratorRole).isFalse();
    }

    @Test
    public void shouldSaveAdministratorRoleForMemberId_whenMemberHasNotYet() throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.empty());

        // when
        roleService.addAdministratorRole(memberId);

        // then
        verify(adminRepositoryMock, times(1)).save(nullable(AdministratorEntity.class));
    }

    @Test
    public void shouldEmitEvent_afterSaveAdministratorRoleForMemberId_whenMemberHasNotYet() throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.empty());

        // when
        roleService.addAdministratorRole(memberId);

        // then
        verify(eventBusMock, times(1)).post(any(AdministratorRoleAddedEvent.class));
    }

    @Test
    public void shouldNotSaveAdministratorRoleForMemberAgain_whenMemberHasItAlready() throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        roleService.addAdministratorRole(memberId);

        // then
        verify(adminRepositoryMock, times(0)).save(any(AdministratorEntity.class));
    }

    @Test
    public void shouldFalse_whenRemoveAdministratorRole_forUserWhichHasNotThisRole() throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.empty());

        // when
        boolean result = roleService.removeAdministratorRole(memberId);

        // then
        assertThat(result).isFalse();
        verify(adminRepositoryMock, times(0)).delete(any(AdministratorEntity.class));
        verify(eventBusMock, times(0)).post(any(AdministratorRoleRemovedEvent.class));
    }

    @Test
    public void shouldTrue_whenRemoveAdministratorRole_forUserWhichHasThisRole() throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        boolean result = roleService.removeAdministratorRole(memberId);

        // then
        assertThat(result).isTrue();
        verify(adminRepositoryMock, times(1)).delete(any(AdministratorEntity.class));
    }

    @Test
    public void shouldPostEvent_whenRemoveAdministratorRole_forUserWhichHasThisRole() throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        boolean result = roleService.removeAdministratorRole(memberId);

        // then
        verify(eventBusMock, times(1)).post(any(AdministratorRoleRemovedEvent.class));
    }
}