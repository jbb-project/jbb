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

import org.jbb.lib.logging.jaxb.AppenderRef;
import org.jbb.lib.logging.jaxb.Logger;
import org.jbb.system.api.model.logging.AppLogger;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class XmlLoggerBuilder {

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
}
