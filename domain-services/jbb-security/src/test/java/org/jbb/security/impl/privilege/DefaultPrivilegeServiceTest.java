/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.privilege;

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
import org.jbb.security.event.AdministratorPrivilegeAddedEvent;
import org.jbb.security.event.AdministratorPrivilegeRemovedEvent;
import org.jbb.security.impl.privilege.dao.AdministratorRepository;
import org.jbb.security.impl.privilege.model.AdministratorEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultPrivilegeServiceTest {
    @Mock
    private AdministratorRepository adminRepositoryMock;

    @Mock
    private AdministratorEntityFactory administratorEntityFactoryMock;

    @Mock
    private JbbEventBus eventBusMock;


    @InjectMocks
    private DefaultPrivilegeService privilegeService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdPassed_intoHasAdministratorPrivilege()
        throws Exception {
        // when
        privilegeService.hasAdministratorPrivilege(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdPassed_intoAddAdministratorPrivilege()
        throws Exception {
        // when
        privilegeService.addAdministratorPrivilege(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdPassed_intoRemoveAdministratorPrivilege()
        throws Exception {
        // when
        privilegeService.removeAdministratorPrivilege(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldConfirmAdministratorPrivilegeForMember_whenRepositoryReturnsEntityWithGivenMemberId()
        throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        boolean hasAdministratorPrivilege = privilegeService.hasAdministratorPrivilege(memberId);

        // then
        assertThat(hasAdministratorPrivilege).isTrue();
    }

    @Test
    public void shouldDenyAdministratorPrivilegeForMember_whenRepositoryNotReturnEntityWithGivenMemberId()
        throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.empty());

        // when
        boolean hasAdministratorPrivilege = privilegeService.hasAdministratorPrivilege(memberId);

        // then
        assertThat(hasAdministratorPrivilege).isFalse();
    }

    @Test
    public void shouldSaveAdministratorPrivilegeForMemberId_whenMemberHasNotYet() throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.empty());

        // when
        privilegeService.addAdministratorPrivilege(memberId);

        // then
        verify(adminRepositoryMock, times(1)).save(nullable(AdministratorEntity.class));
    }

    @Test
    public void shouldEmitEvent_afterSaveAdministratorPrivilegeForMemberId_whenMemberHasNotYet()
        throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.empty());

        // when
        privilegeService.addAdministratorPrivilege(memberId);

        // then
        verify(eventBusMock, times(1)).post(any(AdministratorPrivilegeAddedEvent.class));
    }

    @Test
    public void shouldNotSaveAdministratorPrivilegeForMemberAgain_whenMemberHasItAlready()
        throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        privilegeService.addAdministratorPrivilege(memberId);

        // then
        verify(adminRepositoryMock, times(0)).save(any(AdministratorEntity.class));
    }

    @Test
    public void shouldFalse_whenRemoveAdministratorPrivilege_forUserWhichHasNotThisPrivilege()
        throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.empty());

        // when
        boolean result = privilegeService.removeAdministratorPrivilege(memberId);

        // then
        assertThat(result).isFalse();
        verify(adminRepositoryMock, times(0)).delete(any(AdministratorEntity.class));
        verify(eventBusMock, times(0)).post(any(AdministratorPrivilegeRemovedEvent.class));
    }

    @Test
    public void shouldTrue_whenRemoveAdministratorPrivilege_forUserWhichHasThisPrivilege()
        throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        boolean result = privilegeService.removeAdministratorPrivilege(memberId);

        // then
        assertThat(result).isTrue();
        verify(adminRepositoryMock, times(1)).delete(any(AdministratorEntity.class));
    }

    @Test
    public void shouldPostEvent_whenRemoveAdministratorPrivilege_forUserWhichHasThisPrivilege()
        throws Exception {
        // given
        Long memberId = 343L;
        given(adminRepositoryMock.findByMemberId(eq(memberId))).willReturn(Optional.of(mock(AdministratorEntity.class)));

        // when
        boolean result = privilegeService.removeAdministratorPrivilege(memberId);

        // then
        verify(eventBusMock, times(1)).post(any(AdministratorPrivilegeRemovedEvent.class));
    }
}