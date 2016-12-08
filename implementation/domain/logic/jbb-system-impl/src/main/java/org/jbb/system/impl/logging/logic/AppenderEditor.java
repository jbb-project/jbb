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
    private final LoggingConfigMapper configMapper;

    @Autowired
    public AppenderEditor(ConfigurationRepository configRepository,
                          LoggingConfigMapper configMapper) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
    }

    public void add(LogAppender appender) {
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        boolean alreadyExists = confElements.stream().anyMatch(appenderWithName(appender.getName()));
        if (alreadyExists) {
            throw new LoggingConfigException(String.format("Appender with name '%d' exists yet", appender.getName()));
        }
        Appender jaxbAppender = configMapper.buildJaxb(appender);
        confElements.add(jaxbAppender);
        configRepository.persistNewConfiguration(configuration);
    }


    public void update(LogAppender appender) {
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        Optional<Object> jaxbAppender = confElements.stream()
                .filter(appenderWithName(appender.getName()))
                .findFirst();
        if (jaxbAppender.isPresent()) {
            confElements.set(confElements.indexOf(jaxbAppender.get()), configMapper.buildJaxb(appender));
            configRepository.persistNewConfiguration(configuration);
        } else {
            throw new LoggingConfigException(String.format("Appender with name '%d' doesn't exist", appender.getName()));
        }
    }

    public void delete(LogAppender appender) {
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        Optional<Object> jaxbAppender = confElements.stream()
                .filter(appenderWithName(appender.getName()))
                .findFirst();
        if (jaxbAppender.isPresent()) {
            confElements.remove(jaxbAppender.get());
            configRepository.persistNewConfiguration(configuration);
        } else {
            throw new LoggingConfigException(String.format("Appender with name '%d' doesn't exist", appender.getName()));
        }
    }

    private Predicate<Object> appenderWithName(String name) {
        return o -> o instanceof Appender && ((Appender) o).getName().equals(name);
    }
}
