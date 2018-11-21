/*
 * Copyright (C) 2018 the original author or authors.
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

import com.google.common.collect.Lists;
import java.util.Optional;
import javax.validation.ConstraintValidatorContext;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.vo.Email;
import org.jbb.members.impl.base.MembersProperties;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.privilege.PrivilegeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EmailNotBusyUpdateValidatorTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private UserDetailsSource userDetailsSourceMock;

    @Mock
    private MembersProperties propertiesMock;

    @Mock
    private PrivilegeService privilegeServiceMock;

    private MemberEntity memberEntity;

    @InjectMocks
    private EmailNotBusyUpdateValidator validator;

    @Before
    public void setUp() throws Exception {
        memberEntity = MemberEntity.builder()
                .email(Email.of("foo@bar.com"))
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
    public void shouldPass_whenDuplicationAllowed_andNoGivenEmail() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(true);
        when(memberRepositoryMock.findByEmail(any())).thenReturn(Lists.newArrayList());

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationAllowed_andEmailExists() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(true);
        when(memberRepositoryMock.findByEmail(any())).thenReturn(Lists.newArrayList(memberEntity));

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationForbidden_andNoGivenEmail() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.findByEmail(any())).thenReturn(Lists.newArrayList());

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationForbidden_andEmailExists_butItIsUnderValidation_andUserWithItTriggeredValidation()
            throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.findByEmail(any())).thenReturn(Lists.newArrayList(memberEntity));
        when(memberRepositoryMock.findByUsername(any())).thenReturn(Optional.of(memberEntity));
        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationForbidden_andEmailExists_butItIsUnderValidation_andAdministratorTriggeredValidation()
            throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.findByEmail(any())).thenReturn(Lists.newArrayList(memberEntity));
        when(memberRepositoryMock.findByUsername(any())).thenReturn(Optional.empty());
        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(privilegeServiceMock.hasAdministratorPrivilege(any())).thenReturn(true);

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenDuplicationForbidden_andEmailExists_andItIsNotUnderValidation_andUserWithItTriggeredValidation()
            throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.findByEmail(any())).thenReturn(Lists.newArrayList(memberEntity));
        when(memberRepositoryMock.findByUsername(any()))
                .thenReturn(Optional.of(anotherMemberEntity()));
        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void shouldFail_whenDuplicationForbidden_andEmailExists_andItIsNotUnderValidation_andAdministratorTriggeredValidation()
            throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.findByEmail(any()))
                .thenReturn(Lists.newArrayList(anotherMemberEntity()));
        when(memberRepositoryMock.findByUsername(any())).thenReturn(Optional.of(memberEntity));
        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(privilegeServiceMock.hasAdministratorPrivilege(any())).thenReturn(true);

        // when
        boolean validationResult = validator.isValid(memberEntity, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isFalse();
    }

    private MemberEntity anotherMemberEntity() {
        MemberEntity memberEntity = MemberEntity.builder()
                .email(Email.of("anot@her.com"))
                .build();
        memberEntity.setId(2L);
        return memberEntity;
    }

}