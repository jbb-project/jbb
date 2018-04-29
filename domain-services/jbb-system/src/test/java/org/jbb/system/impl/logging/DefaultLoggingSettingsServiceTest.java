/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.logging;

import com.google.common.collect.Sets;

import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.logging.ConfigurationRepository;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.system.api.logging.LoggingConfigurationException;
import org.jbb.system.api.logging.model.AddingModeGroup;
import org.jbb.system.api.logging.model.AppLogger;
import org.jbb.system.api.logging.model.LogAppender;
import org.jbb.system.api.logging.model.LoggingConfiguration;
import org.jbb.system.event.LogAppenderCreatedEvent;
import org.jbb.system.event.LogAppenderRemovedEvent;
import org.jbb.system.event.LogAppenderUpdatedEvent;
import org.jbb.system.event.LoggerCreatedEvent;
import org.jbb.system.event.LoggerRemovedEvent;
import org.jbb.system.event.LoggerUpdatedEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultLoggingSettingsServiceTest {
    @Mock
    private ConfigurationRepository configRepositoryMock;

    @Mock
    private LoggingConfigMapper configMapperMock;

    @Mock
    private AppenderEditor appenderEditorMock;

    @Mock
    private LoggerEditor loggerEditorMock;

    @Mock
    private AppenderBrowser appenderBrowserMock;

    @Mock
    private LoggerBrowser loggerBrowserMock;

    @Mock
    private Validator validatorMock;

    @Mock
    private JbbEventBus eventBusMock;

    @InjectMocks
    private DefaultLoggingSettingsService loggingSettingsService;

    @Test
    public void shouldDelegateConfigurationFromRepository_toConfigMapper() {
        // given
        Configuration conf = mock(Configuration.class);
        given(configRepositoryMock.getConfiguration()).willReturn(conf);

        // when
        loggingSettingsService.getLoggingConfiguration();

        // then
        Mockito.verify(configMapperMock, times(1)).buildConfiguration(eq(conf));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullAppenderPassedToAddMethod() {
        // when
        loggingSettingsService.addAppender(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenValidationFailedDuringAddingAppender() {
        // given
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        loggingSettingsService.addAppender(mock(LogAppender.class));

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldDelegateToAppenderEditor_whenValidationPassedDuringAddingAppender() {
        // given
        LogAppender appender = mock(LogAppender.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.addAppender(appender);

        // then
        verify(appenderEditorMock, times(1)).add(eq(appender));
        verify(validatorMock, times(1)).validate(any(), any(), eq(AddingModeGroup.class));
    }

    @Test
    public void shouldEmitLogAppenderAddedEvent_whenAddingAppenderInvoked() {
        // given
        LogAppender appender = mock(LogAppender.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.addAppender(appender);

        // then
        verify(eventBusMock).post(isA(LogAppenderCreatedEvent.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullAppenderPassedToUpdateMethod() {
        // when
        loggingSettingsService.updateAppender(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenValidationFailedDuringUpdatingAppender() {
        // given
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        loggingSettingsService.updateAppender(mock(LogAppender.class));

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldDelegateToAppenderEditor_whenValidationPassedDuringUpdatingAppender() {
        // given
        LogAppender appender = mock(LogAppender.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.updateAppender(appender);

        // then
        verify(appenderEditorMock, times(1)).update(eq(appender));
        verify(validatorMock, times(1)).validate(any());
    }

    @Test
    public void shouldEmitLogAppenderUpdatedEvent_whenUpdatingAppenderInvoked() {
        // given
        LogAppender appender = mock(LogAppender.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.updateAppender(appender);

        // then
        verify(eventBusMock).post(isA(LogAppenderUpdatedEvent.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullAppenderPassedToDeleteMethod() {
        // when
        loggingSettingsService.deleteAppender(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldDelegateToAppenderEditor_whenDeleteAppender() {
        // given
        LogAppender appender = mock(LogAppender.class);

        // when
        loggingSettingsService.deleteAppender(appender);

        // then
        verify(appenderEditorMock, times(1)).delete(eq(appender));
    }

    @Test
    public void shouldEmitLogAppenderRemovedEvent_whenRemovingAppenderInvoked() {
        // given
        LogAppender appender = mock(LogAppender.class);

        // when
        loggingSettingsService.deleteAppender(appender);

        // then
        verify(eventBusMock).post(isA(LogAppenderRemovedEvent.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullLoggerPassedToAddMethod() {
        // when
        loggingSettingsService.addLogger(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenValidationFailedDuringAddingLogger() {
        // given
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        loggingSettingsService.addLogger(mock(AppLogger.class));

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldDelegateToLoggerEditor_whenValidationPassedDuringAddingLogger() {
        // given
        AppLogger logger = mock(AppLogger.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.addLogger(logger);

        // then
        verify(loggerEditorMock, times(1)).add(eq(logger));
        verify(validatorMock, times(1)).validate(any(), any(), eq(AddingModeGroup.class));
    }


    @Test
    public void shouldEmitLoggerAddedEvent_whenAddingLoggerInvoked() {
        // given
        AppLogger logger = mock(AppLogger.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.addLogger(logger);

        // then
        verify(eventBusMock).post(isA(LoggerCreatedEvent.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullLoggerPassedToUpdateMethod() {
        // when
        loggingSettingsService.updateLogger(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenValidationFailedDuringUpdatingLogger() {
        // given
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        loggingSettingsService.updateLogger(mock(AppLogger.class));

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldDelegateToLoggerEditor_whenValidationPassedDuringUpdatingLogger() {
        // given
        AppLogger logger = mock(AppLogger.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.updateLogger(logger);

        // then
        verify(loggerEditorMock, times(1)).update(eq(logger));
        verify(validatorMock, times(1)).validate(any());
    }

    @Test
    public void shouldEmitLoggerUpdatedEvent_whenUpdatingLoggerInvoked() {
        // given
        AppLogger logger = mock(AppLogger.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.updateLogger(logger);

        // then
        verify(eventBusMock).post(isA(LoggerUpdatedEvent.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullLoggerPassedToDeleteMethod() {
        // when
        loggingSettingsService.deleteLogger(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldDelegateToLoggerEditor_whenDeleteLogger() {
        // given
        AppLogger logger = mock(AppLogger.class);

        // when
        loggingSettingsService.deleteLogger(logger);

        // then
        verify(loggerEditorMock, times(1)).delete(eq(logger));
    }

    @Test
    public void shouldEmitLoggerRemovedEvent_whenRemovingLoggerInvoked() {
        // given
        AppLogger logger = mock(AppLogger.class);

        // when
        loggingSettingsService.deleteLogger(logger);

        // then
        verify(eventBusMock).post(isA(LoggerRemovedEvent.class));
    }

    @Test
    public void shouldPersistNewConfig_withEnableDebugInfoFlag() {
        // given
        boolean enableDebugInfo = true;
        given(configRepositoryMock.getConfiguration()).willReturn(new Configuration());

        // when
        loggingSettingsService.enableDebugLoggingFrameworkMode(enableDebugInfo);

        // then
        verify(configRepositoryMock, times(1)).persistNewConfiguration(
                argThat(Configuration::isDebug)
        );
    }

    @Test
    public void shouldPersistNewConfig_withPackagingData() {
        // given
        boolean packagingData = true;
        given(configRepositoryMock.getConfiguration()).willReturn(new Configuration());

        // when
        loggingSettingsService.showPackagingData(packagingData);

        // then
        verify(configRepositoryMock, times(1)).persistNewConfiguration(
                argThat(Configuration::isPackagingData)
        );
    }

    @Test
    public void shouldDelegateSearchQuery_toAppenderBrowser() {
        // given
        LoggingConfiguration loggingConfigurationMock = mock(LoggingConfiguration.class);
        given(configMapperMock.buildConfiguration(any())).willReturn(loggingConfigurationMock);

        // when
        loggingSettingsService.getAppender("foo");

        // then
        verify(appenderBrowserMock, times(1)).searchForAppenderWithName(eq(loggingConfigurationMock), eq("foo"));
    }

    @Test
    public void shouldDelegateSearchQuery_toLoggerBrowser() {
        // given
        LoggingConfiguration loggingConfigurationMock = mock(LoggingConfiguration.class);
        given(configMapperMock.buildConfiguration(any())).willReturn(loggingConfigurationMock);

        // when
        loggingSettingsService.getLogger("bar");

        // then
        verify(loggerBrowserMock, times(1)).searchForLoggerWithName(eq(loggingConfigurationMock), eq("bar"));
    }
}