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

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.CoreConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.logging.LoggingConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.system.api.exception.LoggingConfigException;
import org.jbb.system.api.exception.LoggingConfigurationException;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.jbb.system.api.model.logging.LogLevel;
import org.jbb.system.api.model.logging.LogLevelFilter;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.jbb.system.api.service.LoggingSettingsService;
import org.jbb.system.impl.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CoreConfig.class, SystemConfig.class, LoggingConfig.class, EventBusConfig.class, PropertiesConfig.class, DbConfig.class, CoreConfigMocks.class})
public class LoggingSettingsServiceForLoggersIT {
    @Autowired
    private LoggingSettingsService loggingSettingsService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenTryToAddNullLogger() throws Exception {
        // when
        loggingSettingsService.addLogger(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithNullName() throws Exception {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName(null);

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithEmptyName() throws Exception {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName(StringUtils.EMPTY);

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithNameWhichIsNotPackageOrClassName() throws Exception {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName("Incorrect logger name");

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithExistingName() throws Exception {
        // given
        AppLogger appLogger = correctAppLogger();

        // when
        loggingSettingsService.addLogger(appLogger);
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithNullLevel() throws Exception {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setLevel(null);

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddLoggerWithNullAppenderList() throws Exception {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setAppenders(null);

        // when
        loggingSettingsService.addLogger(appLogger);

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldAddAppLogger_withPackageAsName() throws Exception {
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
    public void shouldAddAppLogger_withClassAsName() throws Exception {
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
    public void shouldUpgradeLogger() throws Exception {
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
    public void shouldRemoveNotRootAppLogger() throws Exception {
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
    public void shouldThrowLoggingConfigurationException_whenTryToRemoveNotExistingAppLogger() throws Exception {
        // given
        AppLogger appLogger = correctAppLogger();
        appLogger.setName("org.jbb.NotExistingLogger");

        // when
        loggingSettingsService.deleteLogger(appLogger);

        // then
        // throw LoggingConfigException
    }

    @Test(expected = LoggingConfigException.class)
    public void shouldThrowLoggingConfigurationException_whenTryToRemoveRootAppLogger() throws Exception {
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
        fileAppender.setName(RandomStringUtils.randomAlphanumeric(20));
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
