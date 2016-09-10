/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stactrace.strategy;

import org.assertj.core.util.Lists;
import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersValues;
import org.jbb.frontend.impl.logic.stacktrace.strategy.NobodyStackTraceVisibilityStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(MockitoJUnitRunner.class)
public class NobodyStackTraceVisibilityStrategyTest {
    @Mock
    private UserDetails userDetailsMock;

    @InjectMocks
    private NobodyStackTraceVisibilityStrategy strategy;

    @Test
    public void shouldHandle_whenVisibilityValueIsEqualToNobody() throws Exception {
        // given
        StackTraceVisibilityUsersValues visibility = StackTraceVisibilityUsersValues.NOBODY;

        // when
        boolean canHandle = strategy.canHandle(visibility, userDetailsMock);

        // then
        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldNotHandle_whenVisibilityValueIsNotEqualToNobody() throws Exception {
        // given
        List<StackTraceVisibilityUsersValues> visibilities = Lists.newArrayList(Arrays.asList(StackTraceVisibilityUsersValues.values()));
        visibilities.remove(StackTraceVisibilityUsersValues.NOBODY);

        // when
        for (StackTraceVisibilityUsersValues visibility : visibilities) {
            if (strategy.canHandle(visibility, userDetailsMock)) {
                // then
                fail("Should not be handled with visibility: " + visibility);
            }
        }
    }

    @Test
    public void shouldNotReturnStackTrace() throws Exception {
        // given
        Exception ex = new Exception("Example");

        // when
        Optional<String> stackTraceString = strategy.getStackTraceString(ex);

        // then
        assertThat(stackTraceString).isEmpty();
    }
}
