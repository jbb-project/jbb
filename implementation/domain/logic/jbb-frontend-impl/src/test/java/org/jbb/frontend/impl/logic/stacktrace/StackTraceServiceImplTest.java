/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stacktrace;

import com.google.common.collect.Lists;

import org.jbb.frontend.api.data.StackTraceVisibilityLevel;
import org.jbb.frontend.impl.base.properties.FrontendProperties;
import org.jbb.frontend.impl.stacktrace.data.UserDetailsSource;
import org.jbb.frontend.impl.stacktrace.logic.StackTraceServiceImpl;
import org.jbb.frontend.impl.stacktrace.logic.format.StackTraceStringFormatterStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class StackTraceServiceImplTest {
    private static final Exception ANY_EXCEPTION = new Exception();

    @Mock
    private FrontendProperties properties;

    @Mock
    private UserDetailsSource userDetailsSource;

    @Spy
    private List<StackTraceStringFormatterStrategy> stackTraceStringFormatterStrategyList = Lists.newArrayList();

    @InjectMocks
    private StackTraceServiceImpl stackTraceVisibilityUsersService;

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

        UserDetails userDetailsMock = mock(UserDetails.class);
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

        UserDetails userDetailsMock = mock(UserDetails.class);
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

        UserDetails userDetailsMock = mock(UserDetails.class);
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

        UserDetails userDetailsMock = mock(UserDetails.class);
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
}
