/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation.create;

import org.jbb.lib.commons.vo.Email;
import org.jbb.members.impl.base.MembersProperties;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class EmailNotBusyCreateValidatorTest {

    @Mock
    private MemberRepository memberRepositoryMock;

    @Mock
    private MembersProperties propertiesMock;

    @InjectMocks
    private EmailNotBusyCreateValidator validator;


    @Test
    public void shouldPass_whenDuplicationAllowed_andNoGivenEmail() throws Exception {
        // given
        Email email = Email.of("any@mail.com");
        when(propertiesMock.allowEmailDuplication()).thenReturn(true);
        when(memberRepositoryMock.countByEmail(eq(email))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(email, null);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationAllowed_andEmailExists() throws Exception {
        // given
        Email email = Email.of("any@mail.com");
        when(propertiesMock.allowEmailDuplication()).thenReturn(true);
        when(memberRepositoryMock.countByEmail(eq(email))).thenReturn(1L);

        // when
        boolean validationResult = validator.isValid(email, null);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldPass_whenDuplicationForbidden_andNoGivenEmail() throws Exception {
        // given
        Email email = Email.of("any@mail.com");
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.countByEmail(eq(email))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(email, null);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenDuplicationForbidden_andEmailExists() throws Exception {
        // given
        Email email = Email.of("any@mail.com");
        when(propertiesMock.allowEmailDuplication()).thenReturn(false);
        when(memberRepositoryMock.countByEmail(eq(email))).thenReturn(1L);

        // when
        boolean validationResult = validator.isValid(email, null);

        // then
        assertThat(validationResult).isFalse();
    }

}