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
import org.apache.commons.lang3.Validate;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AppenderBrowser {
    public Optional<LogAppender> searchForAppenderWithName(LoggingConfiguration loggingConfiguration, String name) {
        Validate.notNull(loggingConfiguration);

        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }

        List<LogConsoleAppender> consoleAppenders = loggingConfiguration.getConsoleAppenders();
        Optional<LogConsoleAppender> matchedConsoleAppender = consoleAppenders.stream()
                .filter(consoleAppender -> consoleAppender.getName().equals(name))
                .findFirst();
        if (matchedConsoleAppender.isPresent()) {
            return Optional.of(matchedConsoleAppender.get());
        }

        List<LogFileAppender> fileAppenders = loggingConfiguration.getFileAppenders();
        Optional<LogFileAppender> matchedFileAppender = fileAppenders.stream()
                .filter(fileAppender -> fileAppender.getName().equals(name))
                .findFirst();
        return Optional.ofNullable(matchedFileAppender.orElse(null));
    }
}