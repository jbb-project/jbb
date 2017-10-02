/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.logging.logic;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.jbb.system.api.logging.model.AppLogger;
import org.jbb.system.api.logging.model.LoggingConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class LoggerBrowserTest {
    @Mock
    private LoggingConfiguration loggingConfigurationMock;

    private LoggerBrowser loggerBrowser = new LoggerBrowser();

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullConfigurationPassed() throws Exception {
        // when
        loggerBrowser.searchForLoggerWithName(null, "anyname");

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldReturnAbsent_whenNullName() throws Exception {
        // when
        Optional<AppLogger> result = loggerBrowser.searchForLoggerWithName(loggingConfigurationMock, null);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnAbsent_whenEmptyName() throws Exception {
        // when
        Optional<AppLogger> result = loggerBrowser.searchForLoggerWithName(loggingConfigurationMock, StringUtils.EMPTY);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnAppLogger_withSpecifiedName() throws Exception {
        // given
        AppLogger firstLogger = mock(AppLogger.class);
        given(firstLogger.getName()).willReturn("foo");

        AppLogger secondLogger = mock(AppLogger.class);
        given(secondLogger.getName()).willReturn("bar");

        given(loggingConfigurationMock.getLoggers()).willReturn(Lists.newArrayList(firstLogger, secondLogger));

        // when
        Optional<AppLogger> appLogger = loggerBrowser.searchForLoggerWithName(loggingConfigurationMock, "bar");

        // then
        assertThat(appLogger).isPresent();
        assertThat(appLogger.get().getName()).isEqualTo("bar");
        assertThat(appLogger.get()).isEqualTo(secondLogger);
    }

    @Test
    public void shouldReturnEmpty_whenLoggerWithSpecifiedNameDoNotExists() throws Exception {
        // given
        AppLogger firstLogger = mock(AppLogger.class);
        given(firstLogger.getName()).willReturn("foo");

        AppLogger secondLogger = mock(AppLogger.class);
        given(secondLogger.getName()).willReturn("bar");

        given(loggingConfigurationMock.getLoggers()).willReturn(Lists.newArrayList(firstLogger, secondLogger));

        // when
        Optional<AppLogger> appLogger = loggerBrowser.searchForLoggerWithName(loggingConfigurationMock, "winamp");

        // then
        assertThat(appLogger).isNotPresent();
    }

}