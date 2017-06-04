/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.logic;

import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.role.RoleService;
import org.jbb.system.event.ConnectionToDatabaseEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FirstMemberCreatorTest {
    @Mock
    private JbbEventBus eventBusMock;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private RoleService roleServiceMock;

    @Mock
    private RegistrationService registrationServiceMock;

    @InjectMocks
    private FirstMemberCreator firstMemberCreator;

    @Test
    public void shouldDoNothing_whenSomeMemberAlreadyExists() throws Exception {
        // given
        given(memberRepositoryMock.count()).willReturn(1L);

        // when
        firstMemberCreator.createFirstMemberWithAdministratorRoleIfNeeded(new ConnectionToDatabaseEvent());

        // then
        verifyZeroInteractions(roleServiceMock);
        verifyZeroInteractions(registrationServiceMock);
    }

    @Test
    public void shouldRegisterAdmin_whenNoMemberExists() throws Exception {
        // given
        given(memberRepositoryMock.count()).willReturn(0L);
        given(memberRepositoryMock.findByUsername(any(Username.class))).willReturn(Optional.of(mock(MemberEntity.class)));

        // when
        firstMemberCreator.createFirstMemberWithAdministratorRoleIfNeeded(new ConnectionToDatabaseEvent());

        // then
        verify(registrationServiceMock, times(1)).register(any(RegistrationRequest.class));
        verify(roleServiceMock, times(1)).addAdministratorRole(any(Long.class));
    }

}