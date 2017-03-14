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

import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.lib.core.security.UserDetailsSource;
import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.Member;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.service.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintValidatorContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DisplayedNameNotBusyValidatorTest {
    @Mock
    private ConstraintValidatorContext constraintValidatorContextMock;

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private UserDetailsSource userDetailsSourceMock;

    @Mock
    private SecurityContentUser userDetailsMock;

    @Mock
    private MemberEntity memberEntityMock;

    @Mock
    private RoleService roleServiceMock;

    @Mock
    private DisplayedName displayedName;

    @InjectMocks
    private DisplayedNameNotBusyValidator validator;

    @Before
    public void setUp() throws Exception {
        when(memberEntityMock.getDisplayedName()).thenReturn(displayedName);

        ConstraintValidatorContext.ConstraintViolationBuilder violationBuilderMock =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.class);
        when(constraintValidatorContextMock.buildConstraintViolationWithTemplate(any()))
                .thenReturn(violationBuilderMock);
        ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilderMock =
                mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class);
        when(violationBuilderMock.addPropertyNode(any())).thenReturn(nodeBuilderMock);
    }

    @Test
    public void shouldPass_whenNoGivenDisplayedName() throws Exception {
        // given
        when(memberRepositoryMock.countByDisplayedName(any(DisplayedName.class))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenSingleDisplayedNameExists_butItIsDisplayedNameOfCurrentUser() throws Exception {
        // given
        when(memberRepositoryMock.countByDisplayedName(any(DisplayedName.class))).thenReturn(1L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("foo");

        Member memberMock = mock(Member.class);
        when(memberMock.getUsername()).thenReturn(Username.builder().value("foo").build());

        when(memberRepositoryMock.findByDisplayedName(eq(displayedName))).thenReturn(memberMock);

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenSingleDisplayedNameExists_butItIsNotDisplayedNameOfCurrentUser() throws Exception {
        // given
        when(memberRepositoryMock.countByDisplayedName(any(DisplayedName.class))).thenReturn(1L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(userDetailsMock);
        when(userDetailsMock.getUsername()).thenReturn("foo");

        Member memberMock = mock(Member.class);
        when(memberMock.getUsername()).thenReturn(Username.builder().value("bar").build());

        when(memberRepositoryMock.findByDisplayedName(eq(displayedName))).thenReturn(memberMock);

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void shouldFail_whenSingleDisplayedNameExists_butUserIsNotAuthenticated() throws Exception {
        // given
        when(memberRepositoryMock.countByDisplayedName(any(DisplayedName.class))).thenReturn(1L);
        when(userDetailsSourceMock.getFromApplicationContext()).thenReturn(null);

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isFalse();
    }

    @Test
    public void shouldFail_whenDisplayedNameExists() throws Exception {
        // given
        when(memberRepositoryMock.countByDisplayedName(any(DisplayedName.class))).thenReturn(1L);

        // when
        boolean validationResult = validator.isValid(memberEntityMock, constraintValidatorContextMock);

        // then
        assertThat(validationResult).isFalse();
    }

}