/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import javax.validation.ConstraintValidatorContext;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.Member;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.data.MembersProperties;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.role.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmailNotBusyValidatorTest {
    @Mock
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private MembersProperties propertiesMock;

    @Mock
    private UserDetailsSource userDetailsSourceMock;

    @Mock
    private SecurityContentUser userDetailsMock;

    @Mock
    private Email email;

    @Mock
    private MemberEntity memberEntityMock;

    @Mock
    private RoleService roleServiceMock;

    @InjectMocks
    private EmailNotBusyValidator validator;

    @Before
    public void setUp() throws Exception {
        when(memberEntityMock.getEmail()).thenReturn(email);

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
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationAllowed_andEmailExists() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(true);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(4L);

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationForbidden_andNoGivenEmail() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationForbidden_andEmailExists_butItIsAnEmailOfCurrentUser() throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(4L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("foo");
        when(memberEntityMock.getUsername()).thenReturn(Username.builder().value("foo").build());

        Member memberMock = mock(Member.class);
        when(memberMock.getUsername()).thenReturn(Username.builder().value("foo").build());

        when(memberRepositoryMock.findByEmail(eq(email))).thenReturn(Lists.newArrayList(memberMock));

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenDuplicationAllowed_andEmailExists_butItIsNOTAnEmailOfCurrentUser_andCallerIsNotAnAdministrator()
        throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(4L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("foo");
        when(memberEntityMock.getUsername()).thenReturn(Username.builder().value("bar").build());
        when(memberEntityMock.getEmail()).thenReturn(Email.builder().value("a@b.com").build());

        when(roleServiceMock.hasAdministratorRole(any())).thenReturn(false);

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void shouldPass_whenDuplicationAllowed_andEmailExists_butItIsNOTAnEmailOfCurrentUser_andCallerIsAdministrator()
        throws Exception {
        // given
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.countByEmail(any(Email.class))).thenReturn(4L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("foo");
        when(memberEntityMock.getUsername()).thenReturn(Username.builder().value("bar").build());
        when(memberEntityMock.getEmail()).thenReturn(Email.builder().value("a@b.com").build());

        when(roleServiceMock.hasAdministratorRole(any())).thenReturn(true);

        // when
        boolean validationResult = validator
            .isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }
}