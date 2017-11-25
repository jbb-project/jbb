/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.system.api.stacktrace.StackTraceVisibilityLevel;
import org.jbb.system.impl.SystemProperties;
import org.jbb.system.impl.stacktrace.format.StackTraceStringFormatterStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultStackTraceServiceTest {
    private static final Exception ANY_EXCEPTION = new Exception();

    @Mock
    private SystemProperties properties;

    @Mock
    private UserDetailsSource userDetailsSource;

    @Spy
    private List<StackTraceStringFormatterStrategy> stackTraceStringFormatterStrategyList = Lists.newArrayList();

    @InjectMocks
    private DefaultStackTraceService stackTraceVisibilityUsersService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullExceptionHandled() throws Exception {
        // when
        stackTraceVisibilityUsersService.getStackTraceAsString(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldReturnEmptyStackTrace_whenThereIsNoStrategies() throws Exception {
        // given
        stackTraceStringFormatterStrategyList.clear();

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getStackTraceAsString(ANY_EXCEPTION);

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    public void shouldInvokeCanHandleCheck_whenSomeStrategyInjected() throws Exception {
        // given
        given(properties.stackTraceVisibilityLevel()).willReturn("users");

        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        given(userDetailsSource.getFromApplicationContext()).willReturn(userDetailsMock);

        StackTraceStringFormatterStrategy strategyMock = mock(StackTraceStringFormatterStrategy.class);
        stackTraceStringFormatterStrategyList.add(strategyMock);

        // when
        stackTraceVisibilityUsersService.getStackTraceAsString(ANY_EXCEPTION);

        // then
        verify(strategyMock, times(1)).canHandle(eq(StackTraceVisibilityLevel.USERS), eq(userDetailsMock));
    }

    @Test
    public void shouldNotInvokeGettingStacktraceForStrategy_whenCanHandleReturnFalse() throws Exception {
        // given
        given(properties.stackTraceVisibilityLevel()).willReturn("users");

        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        given(userDetailsSource.getFromApplicationContext()).willReturn(userDetailsMock);

        StackTraceStringFormatterStrategy strategyMock = mock(StackTraceStringFormatterStrategy.class);
        stackTraceStringFormatterStrategyList.add(strategyMock);

        given(strategyMock.canHandle(any(), any())).willReturn(false);

        // when
        stackTraceVisibilityUsersService.getStackTraceAsString(ANY_EXCEPTION);

        // then
        verify(strategyMock, times(0)).getStackTraceAsString(any());
    }

    @Test
    public void shouldInvokeGettingStacktraceForStrategy_whenCanHandleReturnTrue() throws Exception {
        // given
        given(properties.stackTraceVisibilityLevel()).willReturn("users");

        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        given(userDetailsSource.getFromApplicationContext()).willReturn(userDetailsMock);

        StackTraceStringFormatterStrategy strategyMock = mock(StackTraceStringFormatterStrategy.class);
        stackTraceStringFormatterStrategyList.add(strategyMock);

        given(strategyMock.canHandle(any(), any())).willReturn(true);

        // when
        stackTraceVisibilityUsersService.getStackTraceAsString(ANY_EXCEPTION);

        // then
        verify(strategyMock, times(1)).getStackTraceAsString(eq(ANY_EXCEPTION));
    }

    @Test
    public void whenFirstStrategyCanHandle_thenSecondIsNotUsed() throws Exception {
        // given
        given(properties.stackTraceVisibilityLevel()).willReturn("users");

        SecurityContentUser userDetailsMock = mock(SecurityContentUser.class);
        given(userDetailsSource.getFromApplicationContext()).willReturn(userDetailsMock);

        StackTraceStringFormatterStrategy firstStrategyMock = mock(StackTraceStringFormatterStrategy.class);
        StackTraceStringFormatterStrategy secondStrategyMock = mock(StackTraceStringFormatterStrategy.class);
        stackTraceStringFormatterStrategyList.add(firstStrategyMock);
        stackTraceStringFormatterStrategyList.add(secondStrategyMock);

        given(firstStrategyMock.canHandle(any(), any())).willReturn(true);

        // when
        stackTraceVisibilityUsersService.getStackTraceAsString(ANY_EXCEPTION);

        // then
        verify(firstStrategyMock, times(1)).getStackTraceAsString(eq(ANY_EXCEPTION));
        verifyZeroInteractions(secondStrategyMock);
    }

    @Test
    public void shouldReturnCurrentVisibilityLevel() throws Exception {
        // given
        given(properties.stackTraceVisibilityLevel()).willReturn("everybody");

        // when
        StackTraceVisibilityLevel level = stackTraceVisibilityUsersService.getCurrentStackTraceVisibilityLevel();

        // then
        assertThat(level).isEqualTo(StackTraceVisibilityLevel.EVERYBODY);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullLevelHandle() throws Exception {
        // when
        stackTraceVisibilityUsersService.setStackTraceVisibilityLevel(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldSetNewLevelToProperties() throws Exception {
        // given
        StackTraceVisibilityLevel newLevel = StackTraceVisibilityLevel.NOBODY;

        // when
        stackTraceVisibilityUsersService.setStackTraceVisibilityLevel(newLevel);

        // then
        verify(properties, times(1))
                .setProperty(eq(SystemProperties.STACK_TRACE_VISIBILITY_LEVEL_KEY),
                        eq(newLevel.toString().toLowerCase()));

    }
}
