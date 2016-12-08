/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.data;

import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogLevel;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoggerRow {
    private String name;
    private String level;
    private String addivity;
    private List<String> appenders;

    public LoggerRow(String name, LogLevel level, boolean addivity, List<LogAppender> appenders) {
        this.name = name;
        this.level = level.toString().toLowerCase();
        if (AppLogger.ROOT_LOGGER_NAME.equalsIgnoreCase(name)) {
            this.addivity = "N/A";
        } else {
            this.addivity = Boolean.valueOf(addivity).toString();
        }
        this.appenders = appenders.stream()
                .map(appender -> appender.getName())
                .collect(Collectors.toList());
    }
}
