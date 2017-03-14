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

import org.apache.commons.lang3.Validate;
import org.jbb.lib.logging.ConfigurationRepository;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.system.api.exception.LoggingConfigurationException;
import org.jbb.system.api.model.logging.AddingModeGroup;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.jbb.system.api.service.LoggingSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

@Service
public class LoggingSettingsServiceImpl implements LoggingSettingsService {
    private final ConfigurationRepository configRepository;
    private final LoggingConfigMapper configMapper;
    private final AppenderEditor appenderEditor;
    private final LoggerEditor loggerEditor;
    private final AppenderBrowser appenderBrowser;
    private final LoggerBrowser loggerBrowser;
    private final Validator validator;

    @Autowired
    public LoggingSettingsServiceImpl(ConfigurationRepository configRepository,
                                      LoggingConfigMapper configMapper,
                                      AppenderEditor appenderEditor,
                                      LoggerEditor loggerEditor,
                                      AppenderBrowser appenderBrowser,
                                      LoggerBrowser loggerBrowser,
                                      Validator validator) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
        this.appenderEditor = appenderEditor;
        this.loggerEditor = loggerEditor;
        this.appenderBrowser = appenderBrowser;
        this.loggerBrowser = loggerBrowser;
        this.validator = validator;
    }

    @Override
    public LoggingConfiguration getLoggingConfiguration() {
        return configMapper.buildConfiguration(configRepository.getConfiguration());
    }

    @Override
    public void addAppender(LogAppender appender) {
        Validate.notNull(appender);
        Set<ConstraintViolation<LogAppender>> validationResult = validator.validate(appender, Default.class, AddingModeGroup.class);
        if (!validationResult.isEmpty()) {
            throw new LoggingConfigurationException(validationResult);
        }
        appenderEditor.add(appender);
    }

    @Override
    public void updateAppender(LogAppender appender) {
        Validate.notNull(appender);
        Set<ConstraintViolation<LogAppender>> validationResult = validator.validate(appender);
        if (!validationResult.isEmpty()) {
            throw new LoggingConfigurationException(validationResult);
        }
        appenderEditor.update(appender);
    }

    @Override
    public void deleteAppender(LogAppender appender) {
        Validate.notNull(appender);
        appenderEditor.delete(appender);
    }

    @Override
    public void addLogger(AppLogger logger) {
        Validate.notNull(logger);
        Set<ConstraintViolation<AppLogger>> validationResult = validator.validate(logger, Default.class, AddingModeGroup.class);
        if (!validationResult.isEmpty()) {
            throw new LoggingConfigurationException(validationResult);
        }
        loggerEditor.add(logger);
    }

    @Override
    public void updateLogger(AppLogger logger) {
        Validate.notNull(logger);
        Set<ConstraintViolation<AppLogger>> validationResult = validator.validate(logger);
        if (!validationResult.isEmpty()) {
            throw new LoggingConfigurationException(validationResult);
        }
        loggerEditor.update(logger);
    }

    @Override
    public void deleteLogger(AppLogger logger) {
        Validate.notNull(logger);
        loggerEditor.delete(logger);
    }

    @Override
    public void enableDebugLoggingFrameworkMode(boolean enable) {
        Configuration configuration = configRepository.getConfiguration();
        configuration.setDebug(enable);
        configRepository.persistNewConfiguration(configuration);
    }

    @Override
    public void showPackagingData(boolean showPackagingData) {
        Configuration configuration = configRepository.getConfiguration();
        configuration.setPackagingData(showPackagingData);
        configRepository.persistNewConfiguration(configuration);
    }

    @Override
    public Optional<LogAppender> getAppender(String appenderName) {
        return appenderBrowser.searchForAppenderWithName(getLoggingConfiguration(), appenderName);
    }

    @Override
    public Optional<AppLogger> getLogger(String loggerName) {
        return loggerBrowser.searchForLoggerWithName(getLoggingConfiguration(), loggerName);
    }
}
