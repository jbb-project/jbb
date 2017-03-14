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
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class AppenderBrowserTest {
    @Mock
    private LoggingConfiguration loggingConfigurationMock;

    private AppenderBrowser appenderBrowser = new AppenderBrowser();

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullConfigurationPassed() throws Exception {
        // when
        appenderBrowser.searchForAppenderWithName(null, "anyname");

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldReturnAbsent_whenNullName() throws Exception {
        // when
        Optional<LogAppender> result = appenderBrowser.searchForAppenderWithName(loggingConfigurationMock, null);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnAbsent_whenEmptyName() throws Exception {
        // when
        Optional<LogAppender> result = appenderBrowser.searchForAppenderWithName(loggingConfigurationMock, StringUtils.EMPTY);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnConsoleAppender_withSpecifiedName() throws Exception {
        // given
        LogConsoleAppender firstConsoleAppender = mock(LogConsoleAppender.class);
        given(firstConsoleAppender.getName()).willReturn("nok");

        LogConsoleAppender secondConsoleAppender = mock(LogConsoleAppender.class);
        given(secondConsoleAppender.getName()).willReturn("ok");

        given(loggingConfigurationMock.getConsoleAppenders()).willReturn(Lists.newArrayList(firstConsoleAppender, secondConsoleAppender));
        given(loggingConfigurationMock.getFileAppenders()).willReturn(Lists.newArrayList());

        // when
        Optional<LogAppender> logAppender = appenderBrowser.searchForAppenderWithName(loggingConfigurationMock, "ok");

        // then
        assertThat(logAppender).isPresent();
        assertThat(logAppender.get().getName()).isEqualTo("ok");
        assertThat(logAppender.get()).isEqualTo(secondConsoleAppender);
    }

    @Test
    public void shouldReturnFileAppender_withSpecifiedName() throws Exception {
        // given
        LogFileAppender firstFileAppender = mock(LogFileAppender.class);
        given(firstFileAppender.getName()).willReturn("nok");

        LogFileAppender secondFileAppender = mock(LogFileAppender.class);
        given(secondFileAppender.getName()).willReturn("ok");

        given(loggingConfigurationMock.getConsoleAppenders()).willReturn(Lists.newArrayList());
        given(loggingConfigurationMock.getFileAppenders()).willReturn(Lists.newArrayList(firstFileAppender, secondFileAppender));

        // when
        Optional<LogAppender> logAppender = appenderBrowser.searchForAppenderWithName(loggingConfigurationMock, "ok");

        // then
        assertThat(logAppender).isPresent();
        assertThat(logAppender.get().getName()).isEqualTo("ok");
        assertThat(logAppender.get()).isEqualTo(secondFileAppender);
    }

    @Test
    public void shouldReturnEmpty_whenAppenderWithSpecifiedNameDoNotExists() throws Exception {
        // given
        LogConsoleAppender firstAppender = mock(LogConsoleAppender.class);
        given(firstAppender.getName()).willReturn("nok");

        LogFileAppender secondAppender = mock(LogFileAppender.class);
        given(secondAppender.getName()).willReturn("ok");

        given(loggingConfigurationMock.getConsoleAppenders()).willReturn(Lists.newArrayList(firstAppender));
        given(loggingConfigurationMock.getFileAppenders()).willReturn(Lists.newArrayList(secondAppender));

        // when
        Optional<LogAppender> logAppender = appenderBrowser.searchForAppenderWithName(loggingConfigurationMock, "differentName");

        // then
        assertThat(logAppender).isNotPresent();
    }
}