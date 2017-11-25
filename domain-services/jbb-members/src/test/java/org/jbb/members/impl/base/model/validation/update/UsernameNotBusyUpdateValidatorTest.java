/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import javax.validation.ConstraintValidatorContext;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.role.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class UsernameNotBusyUpdateValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private UserDetailsSource userDetailsSourceMock;

    @Mock
    private RoleService roleServiceMock;

    private MemberEntity memberEntity;

    @InjectMocks
    private UsernameNotBusyUpdateValidator validator;

    @Before
    public void setUp() throws Exception {
        memberEntity = MemberEntity.builder()
            .username(Username.of("john"))
            .build();
        memberEntity.setId(1L);

        ConstraintValidatorContext.ConstraintViolationBuilder violationBuilderMock =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(any()))
                .thenReturn(violationBuilderMock);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilderMock =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(violationBuilderMock.addPropertyNode(any())).thenReturn(nodeBuilderMock);
    }

    @Test
    public void shouldPass_whenNoGivenUsername() throws Exception {
        // given
        when(memberRepositoryMock.findByUsername(any())).thenReturn(Optional.empty());

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenSingleUsernameExists_butItIsUnderValidation_andUserWithItTriggeredValidation()
        throws Exception {
        // given
        when(memberRepositoryMock.findByUsername(any())).thenReturn(Optional.of(memberEntity));
        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn(memberEntity.getUsername().toString());

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenSingleUsernameExists_butItIsUnderValidation_andAdministratorTriggeredValidation()
        throws Exception {
        // given
        when(memberRepositoryMock.findByUsername(any())).thenReturn(Optional.of(memberEntity));
        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("admin");
        when(roleServiceMock.hasAdministratorRole(any())).thenReturn(true);

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenSingleUsernameExists_andItIsNotUnderValidation_andUserWithItTriggeredValidation()
        throws Exception {
        // given
        when(memberRepositoryMock.findByUsername(any()))
            .thenReturn(Optional.of(anotherMemberEntity()));
        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn(memberEntity.getUsername().toString());

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void shouldFail_whenSingleUsernameExists_andItIsNotUnderValidation_andAdministratorTriggeredValidation()
        throws Exception {
        // given
        when(memberRepositoryMock.findByUsername(any()))
            .thenReturn(Optional.of(anotherMemberEntity()));
        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("admin");
        when(roleServiceMock.hasAdministratorRole(any())).thenReturn(true);

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isFalse();
    }


    private MemberEntity anotherMemberEntity() {
        MemberEntity memberEntity = MemberEntity.builder()
            .username(Username.of("mark"))
            .build();
        memberEntity.setId(2L);
        return memberEntity;
    }

}