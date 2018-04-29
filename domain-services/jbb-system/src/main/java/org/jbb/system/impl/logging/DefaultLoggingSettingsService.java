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

import org.apache.commons.lang3.Validate;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.logging.ConfigurationRepository;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.system.api.logging.LoggingConfigurationException;
import org.jbb.system.api.logging.LoggingSettingsService;
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
import org.jbb.system.event.LoggingSettingsChangedEvent;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultLoggingSettingsService implements LoggingSettingsService {

    private final ConfigurationRepository configRepository;
    private final LoggingConfigMapper configMapper;
    private final AppenderEditor appenderEditor;
    private final LoggerEditor loggerEditor;
    private final AppenderBrowser appenderBrowser;
    private final LoggerBrowser loggerBrowser;
    private final Validator validator;
    private final JbbEventBus eventBus;

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
        eventBus.post(new LogAppenderCreatedEvent(appender.getName()));
    }

    @Override
    public void updateAppender(LogAppender appender) {
        Validate.notNull(appender);
        Set<ConstraintViolation<LogAppender>> validationResult = validator.validate(appender);
        if (!validationResult.isEmpty()) {
            throw new LoggingConfigurationException(validationResult);
        }
        appenderEditor.update(appender);
        eventBus.post(new LogAppenderUpdatedEvent(appender.getName()));
    }

    @Override
    public void deleteAppender(LogAppender appender) {
        Validate.notNull(appender);
        appenderEditor.delete(appender);
        eventBus.post(new LogAppenderRemovedEvent(appender.getName()));
    }

    @Override
    public void addLogger(AppLogger logger) {
        Validate.notNull(logger);
        Set<ConstraintViolation<AppLogger>> validationResult = validator.validate(logger, Default.class, AddingModeGroup.class);
        if (!validationResult.isEmpty()) {
            throw new LoggingConfigurationException(validationResult);
        }
        loggerEditor.add(logger);
        eventBus.post(new LoggerCreatedEvent(logger.getName()));
    }

    @Override
    public void updateLogger(AppLogger logger) {
        Validate.notNull(logger);
        Set<ConstraintViolation<AppLogger>> validationResult = validator.validate(logger);
        if (!validationResult.isEmpty()) {
            throw new LoggingConfigurationException(validationResult);
        }
        loggerEditor.update(logger);
        eventBus.post(new LoggerUpdatedEvent(logger.getName()));
    }

    @Override
    public void deleteLogger(AppLogger logger) {
        Validate.notNull(logger);
        loggerEditor.delete(logger);
        eventBus.post(new LoggerRemovedEvent(logger.getName()));
    }

    @Override
    public void enableDebugLoggingFrameworkMode(boolean enable) {
        Configuration configuration = configRepository.getConfiguration();
        configuration.setDebug(enable);
        configRepository.persistNewConfiguration(configuration);
        eventBus.post(new LoggingSettingsChangedEvent());
    }

    @Override
    public void showPackagingData(boolean showPackagingData) {
        Configuration configuration = configRepository.getConfiguration();
        configuration.setPackagingData(showPackagingData);
        configRepository.persistNewConfiguration(configuration);
        eventBus.post(new LoggingSettingsChangedEvent());
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
