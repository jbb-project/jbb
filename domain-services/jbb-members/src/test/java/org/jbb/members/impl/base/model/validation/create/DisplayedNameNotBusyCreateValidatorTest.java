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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DisplayedNameNotBusyCreateValidatorTest {

    @Mock
    private MemberRepository memberRepositoryMock;

    @InjectMocks
    private DisplayedNameNotBusyCreateValidator validator;

    @Test
    public void shouldPass_whenNoGivenDisplayedName() throws Exception {
        // given
        DisplayedName displayedName = DisplayedName.of("any");
        when(memberRepositoryMock.countByDisplayedName(eq(displayedName))).thenReturn(0L);

        // when
        boolean validationResult = validator.isValid(displayedName, null);

        // then
        assertThat(validationResult).isTrue();
    }

    @Test
    public void shouldFail_whenDisplayedNameExists() throws Exception {
        // given
        DisplayedName displayedName = DisplayedName.of("any");
        when(memberRepositoryMock.countByDisplayedName(eq(displayedName))).thenReturn(1L);

        // when
        boolean validationResult = validator.isValid(displayedName, null);

        // then
        assertThat(validationResult).isFalse();
    }
}