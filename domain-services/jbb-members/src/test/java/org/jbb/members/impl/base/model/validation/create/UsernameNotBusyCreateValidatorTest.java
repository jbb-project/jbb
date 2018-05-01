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

import org.jbb.lib.commons.vo.Username;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UsernameNotBusyCreateValidatorTest {

    @Mock
    private MemberRepository memberRepositoryMock;

    @InjectMocks
    private UsernameNotBusyCreateValidator validator;


    @Test
    public void shouldPass_whenNoGivenUsername() throws Exception {
        // given
        Username username = Username.of("john");
        when(memberRepositoryMock.countByUsername(eq(username))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(username, null);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenUsernameExists() throws Exception {
        // given
        Username username = Username.of("john");
        when(memberRepositoryMock.countByUsername(eq(username))).thenReturn(1L);

        // when
        boolean validationResult = validator.isValid(username, null);

        // then
        assertThat(validationResult).isFalse();
    }

}