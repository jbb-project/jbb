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

import org.jbb.lib.logging.jaxb.Appender;
import org.jbb.lib.logging.jaxb.AppenderRef;
import org.jbb.lib.logging.jaxb.Logger;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class XmlLoggerBuilder {
    private final XmlAppenderBuilder appenderBuilder;

    @Autowired
    public XmlLoggerBuilder(XmlAppenderBuilder appenderBuilder) {
        this.appenderBuilder = appenderBuilder;
    }

    public Logger buildXml(AppLogger logger) {
        Logger xmlLogger = new Logger();

        xmlLogger.setName(logger.getName());
        xmlLogger.setLevel(logger.getLevel().toString().toUpperCase());
        xmlLogger.setAdditivity(logger.isAddivity());
        xmlLogger.getAppenderRefOrAny().addAll(createAppenderRefs(logger));

        return xmlLogger;
    }

    private Collection<?> createAppenderRefs(AppLogger logger) {
        return logger.getAppenders().stream()
                .map(appender -> {
                    AppenderRef appenderRef = new AppenderRef();
                    appenderRef.setRef(appender.getName());
                    return appenderRef;
                })
                .collect(Collectors.toList());
    }

    public AppLogger build(Logger logger, List<Appender> xmlAppenders) {
        AppLogger appLogger = new AppLogger();

        appLogger.setName(logger.getName());
        appLogger.setLevel(LogLevel.valueOf(logger.getLevel().toUpperCase()));
        appLogger.setAddivity(logger.isAdditivity());

        List<LogAppender> logAppenders = logger.getAppenderRefOrAny().stream()
                .filter(o -> o instanceof AppenderRef)
                .map(o -> (AppenderRef) o)
                .map(ref ->
                        xmlAppenders.stream()
                                .filter(a -> a.getName().equals(ref.getRef()))
                                .findFirst().get()
                )
                .map(xmlAppender -> appenderBuilder.build(xmlAppender))
                .collect(Collectors.toList());
        appLogger.setAppenders(logAppenders);

        return appLogger;
    }
}
