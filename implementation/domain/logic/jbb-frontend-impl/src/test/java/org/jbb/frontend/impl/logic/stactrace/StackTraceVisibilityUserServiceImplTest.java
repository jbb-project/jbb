/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stactrace;

import com.google.common.collect.Lists;

import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersValues;
import org.jbb.frontend.impl.logic.stacktrace.StackTraceVisibilityUsersServiceImpl;
import org.jbb.frontend.impl.logic.stacktrace.UserDetailsExtractor;
import org.jbb.frontend.impl.logic.stacktrace.strategy.StackTraceStrategy;
import org.jbb.frontend.impl.properties.FrontendProperties;
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
public class StackTraceVisibilityUserServiceImplTest {
    private static final Exception ANY_EXCEPTION = new Exception();

    @Mock
    private FrontendProperties properties;

    @Mock
    private UserDetailsExtractor userDetailsExtractor;

    @Spy
    private List<StackTraceStrategy> stackTraceStrategyList = Lists.newArrayList();

    @InjectMocks
    private StackTraceVisibilityUsersServiceImpl stackTraceVisibilityUsersService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullExceptionHandled() throws Exception {
        // when
        stackTraceVisibilityUsersService.getPermissionToStackTraceVisibility(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldReturnEmptyStackTrace_whenThereIsNoStrategies() throws Exception {
        // given
        stackTraceStrategyList.clear();

        // when
        Optional<String> stack = stackTraceVisibilityUsersService.getPermissionToStackTraceVisibility(ANY_EXCEPTION);

        // then
        assertThat(stack).isEmpty();
    }

    @Test
    public void shouldInvokeCanHandleCheck_whenSomeStrategyInjected() throws Exception {
        // given
        given(properties.stackTraceVisibilityUsers()).willReturn("users");

        UserDetails userDetailsMock = mock(UserDetails.class);
        given(userDetailsExtractor.getUserDetailsFromApplicationContext()).willReturn(userDetailsMock);

        StackTraceStrategy strategyMock = mock(StackTraceStrategy.class);
        stackTraceStrategyList.add(strategyMock);

        // when
        stackTraceVisibilityUsersService.getPermissionToStackTraceVisibility(ANY_EXCEPTION);

        // then
        verify(strategyMock, times(1)).canHandle(eq(StackTraceVisibilityUsersValues.USERS), eq(userDetailsMock));
    }

    @Test
    public void shouldNotInvokeGettingStacktraceForStrategy_whenCanHandleReturnFalse() throws Exception {
        // given
        given(properties.stackTraceVisibilityUsers()).willReturn("users");

        UserDetails userDetailsMock = mock(UserDetails.class);
        given(userDetailsExtractor.getUserDetailsFromApplicationContext()).willReturn(userDetailsMock);

        StackTraceStrategy strategyMock = mock(StackTraceStrategy.class);
        stackTraceStrategyList.add(strategyMock);

        given(strategyMock.canHandle(any(), any())).willReturn(false);

        // when
        stackTraceVisibilityUsersService.getPermissionToStackTraceVisibility(ANY_EXCEPTION);

        // then
        verify(strategyMock, times(0)).getStackTraceString(any());
    }

    @Test
    public void shouldInvokeGettingStacktraceForStrategy_whenCanHandleReturnTrue() throws Exception {
        // given
        given(properties.stackTraceVisibilityUsers()).willReturn("users");

        UserDetails userDetailsMock = mock(UserDetails.class);
        given(userDetailsExtractor.getUserDetailsFromApplicationContext()).willReturn(userDetailsMock);

        StackTraceStrategy strategyMock = mock(StackTraceStrategy.class);
        stackTraceStrategyList.add(strategyMock);

        given(strategyMock.canHandle(any(), any())).willReturn(true);

        // when
        stackTraceVisibilityUsersService.getPermissionToStackTraceVisibility(ANY_EXCEPTION);

        // then
        verify(strategyMock, times(1)).getStackTraceString(eq(ANY_EXCEPTION));
    }

    @Test
    public void whenFirstStrategyCanHandle_thenSecondIsNotUsed() throws Exception {
        // given
        given(properties.stackTraceVisibilityUsers()).willReturn("users");

        UserDetails userDetailsMock = mock(UserDetails.class);
        given(userDetailsExtractor.getUserDetailsFromApplicationContext()).willReturn(userDetailsMock);

        StackTraceStrategy firstStrategyMock = mock(StackTraceStrategy.class);
        StackTraceStrategy secondStrategyMock = mock(StackTraceStrategy.class);
        stackTraceStrategyList.add(firstStrategyMock);
        stackTraceStrategyList.add(secondStrategyMock);

        given(firstStrategyMock.canHandle(any(), any())).willReturn(true);

        // when
        stackTraceVisibilityUsersService.getPermissionToStackTraceVisibility(ANY_EXCEPTION);

        // then
        verify(firstStrategyMock, times(1)).getStackTraceString(eq(ANY_EXCEPTION));
        verifyZeroInteractions(secondStrategyMock);
    }
}
