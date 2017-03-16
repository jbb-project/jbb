/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LoggerContext.class, Logger.class})
public class AggregateProxyAppenderTest {
    @Mock
    private ILoggingEvent loggingEvent;

    @Test
    public void shouldUseLoggerName_whenAppend() throws Exception {
        // given
        given(loggingEvent.getLoggerName()).willReturn("ROOT");
        AggregateProxyAppender aggregateProxyAppender = new AggregateProxyAppender();
        aggregateProxyAppender = PowerMockito.spy(aggregateProxyAppender);
        LoggerContext loggerContextMock = PowerMockito.mock(LoggerContext.class);
        PowerMockito.when(aggregateProxyAppender.getContext()).thenReturn(loggerContextMock);
        Mockito.when(loggerContextMock.getLogger(eq("ROOT"))).thenReturn(PowerMockito.mock(Logger.class));

        // when
        aggregateProxyAppender.append(loggingEvent);

        // then
        Mockito.verify(loggingEvent, Mockito.times(1)).getLoggerName();
    }
}