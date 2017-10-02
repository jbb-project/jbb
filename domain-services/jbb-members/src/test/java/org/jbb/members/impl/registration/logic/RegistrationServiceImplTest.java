/*
 * Copyright (C) 2017 the original author or authors.
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

import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationException;
import org.jbb.members.event.MemberRegistrationEvent;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.data.MembersProperties;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;
import org.jbb.security.api.password.PasswordException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
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
        MemberEntity memberEntityMock = mock(MemberEntity.class);
        given(memberEntityMock.getId()).willReturn(23L);
        given(memberFactoryMock.create(any(), any())).willReturn(memberEntityMock);
        given(memberRepositoryMock.save(any(MemberEntity.class))).willReturn(memberEntityMock);
        registrationService.register(mock(RegistrationRequest.class));

        // then
        verify(eventBusMock, times(1)).post(any(MemberRegistrationEvent.class));
    }

    @Test(expected = RegistrationException.class)
    public void shouldThrowRegistrationException_whenValidationForMemberEntityFailed() throws Exception {
        // given
        MemberEntity memberEntityMock = mock(MemberEntity.class);
        given(memberEntityMock.getId()).willReturn(23L);
        given(memberFactoryMock.create(any(), any())).willReturn(memberEntityMock);
        given(memberRepositoryMock.save(any(MemberEntity.class))).willReturn(memberEntityMock);
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
        doThrow(passwordExceptionMock).when(passwordSaverMock).save(any(), any());
        given(memberRepositoryMock.save(nullable(MemberEntity.class))).willReturn(MemberEntity.builder().build());

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

    @Test
    public void shouldGetEmailDuplicationAllowedFlag_fromProperties() throws Exception {
        // given
        given(propertiesMock.allowEmailDuplication()).willReturn(true);

        // when
        boolean response = registrationService.isEmailDuplicationAllowed();

        // then
        assertThat(response).isTrue();
        verify(propertiesMock, times(1)).allowEmailDuplication();
    }

    @Test(expected = UsernameNotFoundException.class)
    public void shouldThrowUsernameNotFoundException_whenGetMetadataForNotExistingMember() throws Exception {
        // given
        given(memberRepositoryMock.findOne(any(Long.class))).willReturn(null);

        // when
        registrationService.getRegistrationMetaData(12L);

        // then
        // throw UsernameNotFoundException
    }

    @Test
    public void shouldReturnMemberRegistrationMetadata_whenGetMetadataForExistingMember() throws Exception {
        // given
        Long memberId = 233L;
        MemberEntity memberEntityMock = mock(MemberEntity.class);
        RegistrationMetaDataEntity registrationMetaDataMock = mock(RegistrationMetaDataEntity.class);
        given(memberEntityMock.getRegistrationMetaData()).willReturn(registrationMetaDataMock);
        given(memberRepositoryMock.findOne(eq(memberId))).willReturn(memberEntityMock);

        // when
        RegistrationMetaData metaData = registrationService.getRegistrationMetaData(memberId);

        // then
        assertThat(metaData).isEqualTo(registrationMetaDataMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullMemberIdPassedWhileGettingMetadata() throws Exception {
        // when
        registrationService.getRegistrationMetaData(null);

        // then
        // throw NullPointerException

    }
}