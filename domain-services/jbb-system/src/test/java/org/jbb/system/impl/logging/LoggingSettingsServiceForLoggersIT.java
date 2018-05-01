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

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.jbb.system.api.logging.LoggingConfigException;
import org.jbb.system.api.logging.LoggingConfigurationException;
import org.jbb.system.api.logging.LoggingSettingsService;
import org.jbb.system.api.logging.model.AppLogger;
import org.jbb.system.api.logging.model.LogFileAppender;
import org.jbb.system.api.logging.model.LogLevel;
import org.jbb.system.api.logging.model.LogLevelFilter;
import org.jbb.system.api.logging.model.LoggingConfiguration;
import org.jbb.system.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LoggingSettingsServiceForLoggersIT extends BaseIT {

    @Autowired
    private LoggingSettingsService loggingSettingsService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenTryToAddNullLogger() {
        // when
        loggingSettingsService.addLogger(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithNullName() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName(null);

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithEmptyName() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName(StringUtils.EMPTY);

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithNameWhichIsNotPackageOrClassName() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName("Incorrect logger name");

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithExistingName() {
        // given
        AppLogger appLogger = correctAppLogger();

        // when
        loggingSettingsService.addLogger(appLogger);
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithNullLevel() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setLevel(null);

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithNullAppenderList() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setAppenders(null);

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldAddAppLogger_withPackageAsName() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName("org.jbbtestbed.logging");

        // when
        loggingSettingsService.addLogger(appLogger);
        Optional<AppLogger> logger = loggingSettingsService.getLogger(appLogger.getName());

        // then
        assertThat(logger).isPresent();
    }

    @Test
    public void shouldAddAppLogger_withClassAsName() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName("org.jbbtestbed.logging.LoggingService");

        // when
        loggingSettingsService.addLogger(appLogger);
        Optional<AppLogger> logger = loggingSettingsService.getLogger(appLogger.getName());

        // then
        assertThat(logger).isPresent();
    }

    @Test
    public void shouldUpgradeLogger() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName("logger.for.Update");

        // when
        loggingSettingsService.addLogger(appLogger);
        appLogger.setAddivity(true);
        appLogger.setLevel(LogLevel.TRACE);
        appLogger.setAppenders(Lists.newArrayList());
        loggingSettingsService.updateLogger(appLogger);
        Optional<AppLogger> logger = loggingSettingsService.getLogger(appLogger.getName());

        // then
        assertThat(logger).isPresent();
        assertThat(logger.get().isAddivity()).isTrue();
        assertThat(logger.get().getLevel()).isEqualTo(LogLevel.TRACE);
        assertThat(logger.get().getAppenders()).isEmpty();
    }

    @Test
    public void shouldRemoveNotRootAppLogger() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName("LoggerForRemoving");

        // when
        loggingSettingsService.addLogger(appLogger);
        loggingSettingsService.deleteLogger(appLogger);
        Optional<AppLogger> logger = loggingSettingsService.getLogger(appLogger.getName());
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        // then
        assertThat(logger).isNotPresent();
        assertThat(loggingConfiguration.getLoggers()).doesNotContain(appLogger);

    }

    @Test(expected = LoggingConfigException.class)
    public void shouldThrowLoggingConfigurationException_whenTryToRemoveNotExistingAppLogger() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName("org.jbb.NotExistingLogger");

        // when
        loggingSettingsService.deleteLogger(appLogger);

        // then
        // throw LoggingConfigException
    }

    @Test(expected = LoggingConfigException.class)
    public void shouldThrowLoggingConfigurationException_whenTryToRemoveRootAppLogger() {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName("org.jbb.NotExistingLogger");

        // when
        Optional<AppLogger> rootLogger = loggingSettingsService.getLogger(AppLogger.ROOT_LOGGER_NAME);
        loggingSettingsService.deleteLogger(rootLogger.get());

        // then
        // throw LoggingConfigException
    }

    private AppLogger correctAppLogger() {
        AppLogger appLogger = new AppLogger();
        appLogger.setName("org.jbb.testing");
        appLogger.setLevel(LogLevel.ERROR);
        appLogger.setAddivity(false);

        LogFileAppender fileAppender = correctAppender();
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder()
                .filteredBy(CharacterPredicates.LETTERS).build();
        fileAppender.setName(randomStringGenerator.generate(20));
        loggingSettingsService.addAppender(fileAppender);

        appLogger.setAppenders(Lists.newArrayList(fileAppender));

        return appLogger;
    }

    private LogFileAppender correctAppender() {
        LogFileAppender fileAppender = new LogFileAppender();
        fileAppender.setName("fileAppender");
        fileAppender.setCurrentLogFileName("jbb.log");
        fileAppender.setRotationFileNamePattern("jbb_%d{yyyy-MM-dd}_%i.log");
        fileAppender.setMaxFileSize(LogFileAppender.FileSize.valueOf("100 MB"));
        fileAppender.setMaxHistory(7);
        fileAppender.setFilter(new LogLevelFilter(LogLevel.INFO));
        fileAppender.setPattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --&gt; [%c] %m%n");
        return fileAppender;
    }
}
