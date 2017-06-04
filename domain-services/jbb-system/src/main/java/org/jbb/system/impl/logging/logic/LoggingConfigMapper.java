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

import org.jbb.lib.logging.jaxb.Appender;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.lib.logging.jaxb.Logger;
import org.jbb.lib.logging.jaxb.Root;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

@Component
public class LoggingConfigMapper {
    private final XmlAppenderBuilder appenderBuilder;
    private final XmlLoggerBuilder loggerBuilder;

    public LoggingConfigMapper(XmlAppenderBuilder appenderBuilder,
                               XmlLoggerBuilder loggerBuilder) {
        this.appenderBuilder = appenderBuilder;
        this.loggerBuilder = loggerBuilder;
    }

    public LoggingConfiguration buildConfiguration(Configuration xmlConfiguration) {
        LoggingConfiguration configuration = new LoggingConfiguration();

        List<Object> xmlElements = xmlConfiguration.getShutdownHookOrStatusListenerOrContextListener();

        List<LogAppender> appenders = getAppenders(xmlElements);

        List<LogConsoleAppender> consoleAppenders = appenders.stream()
                .filter(appender -> appender instanceof LogConsoleAppender)
                .map(appender -> (LogConsoleAppender) appender)
                .collect(Collectors.toList());
        configuration.setConsoleAppenders(consoleAppenders);

        List<LogFileAppender> fileAppenders = appenders.stream()
                .filter(appender -> appender instanceof LogFileAppender)
                .map(appender -> (LogFileAppender) appender)
                .collect(Collectors.toList());
        configuration.setFileAppenders(fileAppenders);

        List<AppLogger> loggers = getLoggers(xmlElements);
        configuration.setLoggers(loggers);

        configuration.setDebugLoggingFrameworkMode(xmlConfiguration.isDebug());
        configuration.setShowPackagingData(xmlConfiguration.isPackagingData());

        return configuration;
    }

    private List<LogAppender> getAppenders(List<Object> xmlElements) {
        return xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(Appender.class))
                .map(a -> appenderBuilder.build((Appender) ((JAXBElement) a).getValue()))
                .collect(Collectors.toList());
    }

    private List<AppLogger> getLoggers(List<Object> xmlElements) {
        List<Appender> appenders = xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(Appender.class))
                .map(a -> (Appender) ((JAXBElement) a).getValue())
                .collect(Collectors.toList());

        List<AppLogger> loggers = new ArrayList<>();
        xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(Root.class))
                .map(a -> (Root) ((JAXBElement) a).getValue())
                .findFirst()
                .ifPresent(root -> loggers.add(loggerBuilder.build(root, appenders)));

        xmlElements.stream()
                .filter(o -> ((JAXBElement) o).getDeclaredType().equals(Logger.class))
                .map(a -> loggerBuilder.build((Logger) ((JAXBElement) a).getValue(), appenders))
                .forEach(loggers::add);

        return loggers;
    }
}
