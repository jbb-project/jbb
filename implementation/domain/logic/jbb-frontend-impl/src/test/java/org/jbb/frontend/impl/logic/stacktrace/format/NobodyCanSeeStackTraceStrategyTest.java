/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stacktrace.format;

import org.assertj.core.util.Lists;
import org.jbb.frontend.api.data.StackTraceVisibilityLevel;
import org.jbb.frontend.impl.stacktrace.logic.format.NobodyCanSeeStackTraceStrategy;
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
public class NobodyCanSeeStackTraceStrategyTest {
    @Mock
    private UserDetails userDetailsMock;

    @InjectMocks
    private NobodyCanSeeStackTraceStrategy strategy;

    @Test
    public void shouldHandle_whenVisibilityValueIsEqualToNobody() throws Exception {
        // given
        StackTraceVisibilityLevel visibility = StackTraceVisibilityLevel.NOBODY;

        // when
        boolean canHandle = strategy.canHandle(visibility, userDetailsMock);

        // then
        assertThat(canHandle).isTrue();
    }

    @Test
    public void shouldNotHandle_whenVisibilityValueIsNotEqualToNobody() throws Exception {
        // given
        List<StackTraceVisibilityLevel> visibilities = Lists.newArrayList(Arrays.asList(StackTraceVisibilityLevel.values()));
        visibilities.remove(StackTraceVisibilityLevel.NOBODY);

        // when
        for (StackTraceVisibilityLevel visibility : visibilities) {
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
        Optional<String> stackTraceString = strategy.getStackTraceAsString(ex);

        // then
        assertThat(stackTraceString).isEmpty();
    }
}
