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

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.logging.LoggingConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.CoreConfigMocks;
import org.jbb.system.api.exception.LoggingConfigException;
import org.jbb.system.api.exception.LoggingConfigurationException;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.jbb.system.api.model.logging.LogLevel;
import org.jbb.system.api.model.logging.LogLevelFilter;
import org.jbb.system.api.model.logging.LogThresholdFilter;
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
@ContextConfiguration(classes = {SystemConfig.class, LoggingConfig.class, EventBusConfig.class, PropertiesConfig.class, DbConfig.class, CoreConfigMocks.class})
public class LoggingSettingsServiceForAppendersIT {

    @Autowired
    private LoggingSettingsService loggingSettingsService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenTryToAddNullAppender() throws Exception {
        // when
        loggingSettingsService.addAppender(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddConsoleAppenderWithNullName() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();
        consoleAppender.setName(null);

        // when
        loggingSettingsService.addAppender(consoleAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddConsoleAppenderWithEmptyName() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();
        consoleAppender.setName(StringUtils.EMPTY);

        // when
        loggingSettingsService.addAppender(consoleAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddConsoleAppenderWithExistingName() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();
        consoleAppender.setName("correctConsoleAppender");

        // when
        loggingSettingsService.addAppender(consoleAppender);
        loggingSettingsService.addAppender(consoleAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddConsoleAppenderWithNullTarget() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();
        consoleAppender.setTarget(null);

        // when
        loggingSettingsService.addAppender(consoleAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddConsoleAppenderWithNullPattern() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();
        consoleAppender.setPattern(null);

        // when
        loggingSettingsService.addAppender(consoleAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddConsoleAppenderWithEmptyPattern() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();
        consoleAppender.setPattern(StringUtils.EMPTY);

        // when
        loggingSettingsService.addAppender(consoleAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithNullName() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setName(null);

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithEmptyName() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setName(null);

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithExistingName() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setName("correctFileAppender");

        // when
        loggingSettingsService.addAppender(fileAppender);
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithNullCurrentLogFileName() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setCurrentLogFileName(null);

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithEmptyCurrentLogFileName() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setCurrentLogFileName(StringUtils.EMPTY);

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithNullRotationFileNamePattern() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setRotationFileNamePattern(null);

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithEmptyRotationFileNamePattern() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setRotationFileNamePattern(StringUtils.EMPTY);

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithIncorrectMaxFileSize() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setMaxFileSize(LogFileAppender.FileSize.valueOf("1000 bb"));

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithIncorrectMaxHistory() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setMaxHistory(-1);

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithNullPattern() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setPattern(null);

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test(expected = LoggingConfigurationException.class)
    public void shouldThrowLoggingConfigurationException_whenAddFileAppenderWithEmptyPattern() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setPattern(StringUtils.EMPTY);

        // when
        loggingSettingsService.addAppender(fileAppender);

        // then
        // throw LoggingConfigurationException
    }

    @Test
    public void shouldAddConsoleAppender() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();

        // when
        loggingSettingsService.addAppender(consoleAppender);
        Optional<LogAppender> appender = loggingSettingsService.getAppender(consoleAppender.getName());
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        // then
        assertThat(appender).isPresent();
        assertThat(loggingConfiguration.getConsoleAppenders()).isNotEmpty();
    }

    @Test
    public void shouldAddFileAppender() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();

        // when
        loggingSettingsService.addAppender(fileAppender);
        Optional<LogAppender> appender = loggingSettingsService.getAppender(fileAppender.getName());
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        // then
        assertThat(appender).isPresent();
        assertThat(loggingConfiguration.getFileAppenders()).isNotEmpty();
    }

    @Test
    public void shouldRemoveConsoleAppender() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();
        consoleAppender.setName("consoleAppenderToRemove");

        // when
        loggingSettingsService.addAppender(consoleAppender);
        loggingSettingsService.deleteAppender(consoleAppender);
        Optional<AppLogger> appender = loggingSettingsService.getLogger(consoleAppender.getName());

        // then
        assertThat(appender).isNotPresent();
    }

    @Test
    public void shouldRemoveFileAppender() throws Exception {
        // given
        LogFileAppender fileAppender = correctFileAppender();
        fileAppender.setName("fileAppenderToRemove");

        // when
        loggingSettingsService.addAppender(fileAppender);
        loggingSettingsService.deleteAppender(fileAppender);
        Optional<LogAppender> appender = loggingSettingsService.getAppender(fileAppender.getName());

        // then
        assertThat(appender).isNotPresent();
    }

    @Test(expected = LoggingConfigException.class)
    public void shouldThrowLoggingConfigurationException_whenTryToRemoveNotExistingAppender() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();
        consoleAppender.setName("notExistingConsoleAppender");

        // when
        loggingSettingsService.deleteAppender(consoleAppender);

        // then
        // throw LoggingConfigException
    }

    @Test
    public void shouldUpgradeAppender() throws Exception {
        // given
        LogConsoleAppender consoleAppender = correctConsoleAppender();
        consoleAppender.setName("consoleAppenderForUpdate");

        // when
        loggingSettingsService.addAppender(consoleAppender);
        consoleAppender.setTarget(LogConsoleAppender.Target.SYSTEM_ERR);
        consoleAppender.setUseColor(true);
        loggingSettingsService.updateAppender(consoleAppender);
        Optional<LogAppender> appender = loggingSettingsService.getAppender(consoleAppender.getName());

        // then
        assertThat(appender).isPresent();
        assertThat(appender.get()).isInstanceOf(LogConsoleAppender.class);

        LogConsoleAppender resultAppender = (LogConsoleAppender) appender.get();
        assertThat(resultAppender.getTarget()).isEqualTo(LogConsoleAppender.Target.SYSTEM_ERR);
        assertThat(resultAppender.isUseColor()).isTrue();
    }

    private LogConsoleAppender correctConsoleAppender() {
        LogConsoleAppender consoleAppender = new LogConsoleAppender();
        consoleAppender.setName("consoleAppender");
        consoleAppender.setTarget(LogConsoleAppender.Target.SYSTEM_OUT);
        consoleAppender.setPattern("[%d{dd-MM-yyyy HH:mm:ss}] [%thread] %-5level --&gt; [%c] %m%n");
        consoleAppender.setFilter(new LogThresholdFilter(LogLevel.DEBUG));
        return consoleAppender;
    }

    private LogFileAppender correctFileAppender() {
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
