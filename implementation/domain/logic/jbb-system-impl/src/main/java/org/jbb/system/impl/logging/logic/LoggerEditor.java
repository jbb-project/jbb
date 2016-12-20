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

import org.jbb.lib.logging.ConfigurationRepository;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.lib.logging.jaxb.Logger;
import org.jbb.lib.logging.jaxb.Root;
import org.jbb.system.api.exception.LoggingConfigException;
import org.jbb.system.api.model.logging.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import javax.xml.bind.JAXBElement;

@Component
public class LoggerEditor {
    private final ConfigurationRepository configRepository;
    private final XmlLoggerBuilder loggerBuilder;

    @Autowired
    public LoggerEditor(ConfigurationRepository configRepository,
                        XmlLoggerBuilder loggerBuilder) {
        this.configRepository = configRepository;
        this.loggerBuilder = loggerBuilder;
    }

    public void add(AppLogger logger) {
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        boolean alreadyExists = confElements.stream().anyMatch(loggerWithName(logger));
        if (alreadyExists) {
            throw new LoggingConfigException(String.format("Logger with name '%s' exists yet", logger.getName()));
        }
        Object xmlLogger = loggerBuilder.buildXml(logger);
        confElements.add(xmlLogger);
        configRepository.persistNewConfiguration(configuration);
    }

    public void update(AppLogger logger) {
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        Optional<Object> xmlLogger = confElements.stream()
                .filter(loggerWithName(logger))
                .findFirst();
        if (xmlLogger.isPresent()) {
            confElements.set(confElements.indexOf(xmlLogger.get()), loggerBuilder.buildXml(logger));
            configRepository.persistNewConfiguration(configuration);
        } else {
            throw new LoggingConfigException(String.format("Logger with name '%s' doesn't exist", logger.getName()));
        }
    }

    public void delete(AppLogger logger) {
        assertNotRoot(logger);
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        Optional<Object> xmlLogger = confElements.stream()
                .filter(loggerWithName(logger))
                .findFirst();
        if (xmlLogger.isPresent()) {
            confElements.remove(xmlLogger.get());
            configRepository.persistNewConfiguration(configuration);
        } else {
            throw new LoggingConfigException(String.format("Logger with name '%s' doesn't exist", logger.getName()));
        }
    }

    private void assertNotRoot(AppLogger logger) {
        if (logger.isRootLogger()) {
            throw new LoggingConfigException("You can't remove root logger");
        }
    }

    private Predicate<? super Object> loggerWithName(AppLogger logger) {
        return o -> o instanceof JAXBElement
                && (isNonRootLogger(logger.getName(), (JAXBElement) o) || isRootLogger(logger, (JAXBElement) o));
    }

    private boolean isNonRootLogger(String name, JAXBElement o) {
        return o.getDeclaredType().equals(Logger.class)
                && ((Logger) o.getValue()).getName().equals(name);
    }

    private boolean isRootLogger(AppLogger logger, JAXBElement o) {
        return o.getDeclaredType().equals(Root.class)
                && logger.isRootLogger();
    }
}
