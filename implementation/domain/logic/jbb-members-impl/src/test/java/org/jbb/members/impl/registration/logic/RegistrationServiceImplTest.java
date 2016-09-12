/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration.logic;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;

import org.jbb.members.api.data.RegistrationRequest;
import org.jbb.members.api.exception.RegistrationException;
import org.jbb.members.event.MemberRegistrationEvent;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.data.MembersProperties;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.exception.PasswordException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationServiceImplTest {
    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private RegistrationMetaDataEntityFactory registrationMetaDataFactoryMock;

    @Mock
    private MemberEntityFactory memberFactoryMock;

    @Mock
    private Validator validatorMock;

    @Mock
    private EventBus eventBusMock;

    @Mock
    private MembersProperties propertiesMock;

    @Mock
    private PasswordSaver passwordSaverMock;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullRegistrationRequestHandled() throws Exception {
        // when
        registrationService.register(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldEmitMemberRegistrationEvent_whenRegistrationCompleted() throws Exception {
        // when
        given(memberFactoryMock.create(any(), any())).willReturn(mock(MemberEntity.class));
        registrationService.register(mock(RegistrationRequest.class));

        // then
        verify(eventBusMock, times(1)).post(any(MemberRegistrationEvent.class));
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenValidationForMemberEntityFailed() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        registrationService.register(mock(RegistrationRequest.class));

        // then
        // throw RegistrationException
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenValidationOfPasswordFailed() throws Exception {
        // given
        PasswordException passwordExceptionMock = mock(PasswordException.class);
        given(passwordExceptionMock.getConstraintViolations()).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));
        doThrow(passwordExceptionMock).when(passwordSaverMock).save(any());

        // when
        registrationService.register(mock(RegistrationRequest.class));

        // then
        // throw RegistrationException
    }

    @Test
    public void shouldUpdateProperty_whenAllowEmailDuplicationMethodInvoked() throws Exception {
        // when
        registrationService.allowEmailDuplication(true);

        // then
        verify(propertiesMock, times(1)).setProperty(eq(MembersProperties.EMAIL_DUPLICATION_KEY), eq("true"));
    }
}