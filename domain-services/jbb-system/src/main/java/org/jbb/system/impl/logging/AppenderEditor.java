/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.logging;

import org.jbb.lib.logging.ConfigurationRepository;
import org.jbb.lib.logging.jaxb.Appender;
import org.jbb.lib.logging.jaxb.AppenderRef;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.lib.logging.jaxb.Logger;
import org.jbb.lib.logging.jaxb.Root;
import org.jbb.system.api.logging.LoggingConfigException;
import org.jbb.system.api.logging.model.LogAppender;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AppenderEditor {
    private final ConfigurationRepository configRepository;
    private final XmlAppenderBuilder appenderBuilder;


    public void add(LogAppender appender) {
        Configuration configuration = configRepository.getConfiguration();
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();
        boolean alreadyExists = confElements.stream().anyMatch(appenderWithName(appender.getName()));
        if (alreadyExists) {
            throw new LoggingConfigException(String.format("Appender with name '%s' exists yet", appender.getName()));
        }
        Appender xmlAppender = appenderBuilder.buildXml(appender);
        confElements.add(firstLoggerIndex(confElements), xmlAppender);
        configRepository.persistNewConfiguration(configuration);
    }

    private int firstLoggerIndex(List<Object> confElements) {
        Optional<Object> firstLogger = confElements.stream()
                .filter(rootLogger().or(notRootLogger()))
                .findFirst();

        return firstLogger.isPresent() ? confElements.indexOf(firstLogger.get()) : -1;
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
            removeAppenderRefFromLoggers(appender, configuration);
            configRepository.persistNewConfiguration(configuration);
        } else {
            throw new LoggingConfigException(String.format("Appender with name '%s' doesn't exist", appender.getName()));
        }
    }

    private void removeAppenderRefFromLoggers(LogAppender appender, Configuration configuration) {
        List<Object> confElements = configuration.getShutdownHookOrStatusListenerOrContextListener();

        // remove from root logger
        Optional<Object> rootLogger = confElements.stream()
                .filter(rootLogger())
                .findFirst();

        if (rootLogger.isPresent()) {
            Root root = (Root) ((JAXBElement) rootLogger.get()).getValue();
            removeAppenderRefs(appender, root.getAppenderRef());
        }

        // remove from another loggers
        List<Logger> loggers = confElements.stream()
                .filter(notRootLogger())
                .map(o -> (Logger) ((JAXBElement) o).getValue())
                .collect(Collectors.toList());

        loggers.forEach(logger -> removeAppenderRefs(appender, logger.getAppenderRef()));
    }


    private void removeAppenderRefs(LogAppender appender, List<AppenderRef> appenderRefs) {
        List<AppenderRef> toRemove = appenderRefs.stream()
                .filter(appenderRef -> appenderRef.getRef().equals(appender.getName()))
                .collect(Collectors.toList());
        appenderRefs.removeAll(toRemove);
    }

    private Predicate<? super Object> rootLogger() {
        return o -> o instanceof JAXBElement && ((JAXBElement) o).getDeclaredType().equals(Root.class);
    }

    private Predicate<? super Object> notRootLogger() {
        return o -> o instanceof JAXBElement && ((JAXBElement) o).getDeclaredType().equals(Logger.class);
    }

    private Predicate<Object> appenderWithName(String name) {
        return o -> o instanceof JAXBElement && ((JAXBElement) o).getDeclaredType().equals(Appender.class)
                && ((Appender) (((JAXBElement) o).getValue())).getName().equals(name);
    }
}
