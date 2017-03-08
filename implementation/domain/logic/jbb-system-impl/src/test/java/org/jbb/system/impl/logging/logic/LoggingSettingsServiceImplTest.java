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

import com.google.common.collect.Sets;

import org.jbb.lib.logging.ConfigurationRepository;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.system.api.exception.LoggingConfigurationException;
import org.jbb.system.api.model.logging.AddingModeGroup;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoggingSettingsServiceImplTest {
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

    @InjectMocks
    private LoggingSettingsServiceImpl loggingSettingsService;

    @Test
    public void shouldDelegateConfigurationFromRepository_toConfigMapper() throws Exception {
        // given
        Configuration conf = mock(Configuration.class);
        given(configRepositoryMock.getConfiguration()).willReturn(conf);

        // when
        loggingSettingsService.getLoggingConfiguration();

        // then
        Mockito.verify(configMapperMock, times(1)).buildConfiguration(eq(conf));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullAppenderPassedToAddMethod() throws Exception {
        // when
        loggingSettingsService.addAppender(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenValidationFailedDuringAddingAppender() throws Exception {
        // given
        given(validatorMock.validate(any(), anyVararg())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        loggingSettingsService.addAppender(mock(LogAppender.class));

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldDelegateToAppenderEdition_whenValidationPassedDuringAddingAppender() throws Exception {
        // given
        LogAppender appender = mock(LogAppender.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.addAppender(appender);

        // then
        verify(appenderEditorMock, times(1)).add(eq(appender));
        verify(validatorMock, times(1)).validate(any(), any(), eq(AddingModeGroup.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullAppenderPassedToUpdateMethod() throws Exception {
        // when
        loggingSettingsService.updateAppender(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenValidationFailedDuringUpdatingAppender() throws Exception {
        // given
        given(validatorMock.validate(any(), anyVararg())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        loggingSettingsService.updateAppender(mock(LogAppender.class));

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldDelegateToAppenderEdition_whenValidationPassedDuringUpdatingAppender() throws Exception {
        // given
        LogAppender appender = mock(LogAppender.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.updateAppender(appender);

        // then
        verify(appenderEditorMock, times(1)).update(eq(appender));
        verify(validatorMock, times(1)).validate(any());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullAppenderPassedToDeleteMethod() throws Exception {
        // when
        loggingSettingsService.deleteAppender(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldDelegateToAppenderDeletion_whenDeleteAppender() throws Exception {
        // given
        LogAppender appender = mock(LogAppender.class);

        // when
        loggingSettingsService.deleteAppender(appender);

        // then
        verify(appenderEditorMock, times(1)).delete(eq(appender));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullLoggerPassedToAddMethod() throws Exception {
        // when
        loggingSettingsService.addLogger(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenValidationFailedDuringAddingLogger() throws Exception {
        // given
        given(validatorMock.validate(any(), anyVararg())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        loggingSettingsService.addLogger(mock(AppLogger.class));

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldDelegateToLoggerEdition_whenValidationPassedDuringAddingLogger() throws Exception {
        // given
        AppLogger logger = mock(AppLogger.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.addLogger(logger);

        // then
        verify(loggerEditorMock, times(1)).add(eq(logger));
        verify(validatorMock, times(1)).validate(any(), any(), eq(AddingModeGroup.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullLoggerPassedToUpdateMethod() throws Exception {
        // when
        loggingSettingsService.updateLogger(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenValidationFailedDuringUpdatingLogger() throws Exception {
        // given
        given(validatorMock.validate(any(), anyVararg())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        loggingSettingsService.updateLogger(mock(AppLogger.class));

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldDelegateToLoggerEdition_whenValidationPassedDuringUpdatingLogger() throws Exception {
        // given
        AppLogger logger = mock(AppLogger.class);
        given(validatorMock.validate(any(), any())).willReturn(Sets.newHashSet());

        // when
        loggingSettingsService.updateLogger(logger);

        // then
        verify(loggerEditorMock, times(1)).update(eq(logger));
        verify(validatorMock, times(1)).validate(any());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullLoggerPassedToDeleteMethod() throws Exception {
        // when
        loggingSettingsService.deleteLogger(null);

        // then
        // throw NullPointerException
    }

    @Test
    public void shouldDelegateToLoggerDeletion_whenDeleteLogger() throws Exception {
        // given
        AppLogger logger = mock(AppLogger.class);

        // when
        loggingSettingsService.deleteLogger(logger);

        // then
        verify(loggerEditorMock, times(1)).delete(eq(logger));
    }

    @Test
    public void shouldPersistNewConfig_withEnableDebugInfoFlag() throws Exception {
        // given
        boolean enableDebugInfo = true;
        given(configRepositoryMock.getConfiguration()).willReturn(new Configuration());

        // when
        loggingSettingsService.enableDebugLoggingFrameworkMode(enableDebugInfo);

        // then
        verify(configRepositoryMock, times(1)).persistNewConfiguration(
                argThat(o -> ((Configuration) o).isDebug())
        );
    }

    @Test
    public void shouldPersistNewConfig_withPackagingData() throws Exception {
        // given
        boolean packagingData = true;
        given(configRepositoryMock.getConfiguration()).willReturn(new Configuration());

        // when
        loggingSettingsService.showPackagingData(packagingData);

        // then
        verify(configRepositoryMock, times(1)).persistNewConfiguration(
                argThat(o -> ((Configuration) o).isPackagingData())
        );
    }

    @Test
    public void shouldDelegateSearchQuery_toAppenderBrowser() throws Exception {
        // given
        LoggingConfiguration loggingConfigurationMock = mock(LoggingConfiguration.class);
        given(configMapperMock.buildConfiguration(any())).willReturn(loggingConfigurationMock);

        // when
        loggingSettingsService.getAppender("foo");

        // then
        verify(appenderBrowserMock, times(1)).searchForAppenderWithName(eq(loggingConfigurationMock), eq("foo"));
    }

    @Test
    public void shouldDelegateSearchQuery_toLoggerBrowser() throws Exception {
        // given
        LoggingConfiguration loggingConfigurationMock = mock(LoggingConfiguration.class);
        given(configMapperMock.buildConfiguration(any())).willReturn(loggingConfigurationMock);

        // when
        loggingSettingsService.getLogger("bar");

        // then
        verify(loggerBrowserMock, times(1)).searchForLoggerWithName(eq(loggingConfigurationMock), eq("bar"));
    }
}