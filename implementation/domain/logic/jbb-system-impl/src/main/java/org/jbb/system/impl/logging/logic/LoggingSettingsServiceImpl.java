/*
 * Copyright (C) 2016 the original author or authors.
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
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.jbb.system.api.service.LoggingSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoggingSettingsServiceImpl implements LoggingSettingsService {
    private final ConfigurationRepository configRepository;
    private final LoggingConfigMapper configBuilder;
    private final AppenderEditor appenderEditor;
    private final LoggerEditor loggerEditor;

    @Autowired
    public LoggingSettingsServiceImpl(ConfigurationRepository configRepository,
                                      LoggingConfigMapper configBuilder,
                                      AppenderEditor appenderEditor,
                                      LoggerEditor loggerEditor) {
        this.configRepository = configRepository;
        this.configBuilder = configBuilder;
        this.appenderEditor = appenderEditor;
        this.loggerEditor = loggerEditor;
    }

    @Override
    public LoggingConfiguration getLoggingConfiguration() {
        return configBuilder.buildConfiguration(configRepository.getConfiguration());
    }

    @Override
    public void addAppender(LogAppender appender) {
        Validate.notNull(appender);
        appenderEditor.add(appender);
    }

    @Override
    public void updateAppender(LogAppender appender) {
        Validate.notNull(appender);
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
        loggerEditor.add(logger);
    }

    @Override
    public void updateLogger(AppLogger logger) {
        Validate.notNull(logger);
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
}
