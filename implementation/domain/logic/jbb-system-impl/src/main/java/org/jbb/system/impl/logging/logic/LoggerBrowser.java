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

import org.apache.commons.lang.StringUtils;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoggerBrowser {
    public Optional<AppLogger> searchForLoggerWithName(LoggingConfiguration loggingConfiguration, String name) {
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }

        return loggingConfiguration.getLoggers().stream()
                .filter(logger -> name.equals(logger.getName()))
                .findFirst();
    }
}
