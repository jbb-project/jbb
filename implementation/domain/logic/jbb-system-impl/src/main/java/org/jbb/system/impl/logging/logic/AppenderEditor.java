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
import org.jbb.lib.logging.jaxb.Appender;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.system.api.exception.LoggingConfigException;
import org.jbb.system.api.model.logging.LogAppender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Component
public class AppenderEditor {
    private final ConfigurationRepository configRepository;
    private final XmlAppenderBuilder appenderBuilder;

    @Autowired
    public AppenderEditor(ConfigurationRepository configRepository,
                          XmlAppenderBuilder appenderBuilder) {
        this.configRepository = configRepository;
        this.appenderBuilder = appenderBuilder;
    }

    public void add(LogAppender appender) {
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        boolean alreadyExists = confElements.stream().anyMatch(appenderWithName(appender.getName()));
        if (alreadyExists) {
            throw new LoggingConfigException(String.format("Appender with name '%s' exists yet", appender.getName()));
        }
        Appender xmlAppender = appenderBuilder.buildXml(appender);
        confElements.add(xmlAppender);
        configRepository.persistNewConfiguration(configuration);
    }


    public void update(LogAppender appender) {
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        Optional<Object> xmlAppender = confElements.stream()
                .filter(appenderWithName(appender.getName()))
                .findFirst();
        if (xmlAppender.isPresent()) {
            confElements.set(confElements.indexOf(xmlAppender.get()), appenderBuilder.buildXml(appender));
            configRepository.persistNewConfiguration(configuration);
        } else {
            throw new LoggingConfigException(String.format("Appender with name '%s' doesn't exist", appender.getName()));
        }
    }

    public void delete(LogAppender appender) {
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        Optional<Object> xmlAppender = confElements.stream()
                .filter(appenderWithName(appender.getName()))
                .findFirst();
        if (xmlAppender.isPresent()) {
            confElements.remove(xmlAppender.get());
            configRepository.persistNewConfiguration(configuration);
        } else {
            throw new LoggingConfigException(String.format("Appender with name '%s' doesn't exist", appender.getName()));
        }
    }

    private Predicate<Object> appenderWithName(String name) {
        return o -> o instanceof Appender && ((Appender) o).getName().equals(name);
    }
}
