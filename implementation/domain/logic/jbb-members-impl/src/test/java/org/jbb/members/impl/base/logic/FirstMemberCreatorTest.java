/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.logic;

import org.jbb.members.api.data.RegistrationRequest;
import org.jbb.members.api.service.RegistrationService;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.security.api.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class FirstMemberCreatorTest {

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
        firstMemberCreator.createFirstMemberWithAdministratorRoleIfNeeded();

        // then
        verifyZeroInteractions(roleServiceMock);
        verifyZeroInteractions(registrationServiceMock);
    }

    @Test
    public void shouldRegisterAdmin_whenNoMemberExists() throws Exception {
        // given
        given(memberRepositoryMock.count()).willReturn(0L);

        // when
        firstMemberCreator.createFirstMemberWithAdministratorRoleIfNeeded();

        // then
        verify(registrationServiceMock, times(1)).register(any(RegistrationRequest.class));
        verify(roleServiceMock, times(1)).addAdministratorRole(eq(FirstMemberCreator.ADMIN_LOGIN));
    }

}