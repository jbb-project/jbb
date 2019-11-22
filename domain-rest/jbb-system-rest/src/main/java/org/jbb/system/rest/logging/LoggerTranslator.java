/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.logging;

import org.jbb.system.api.logging.model.AppLogger;
import org.jbb.system.api.logging.model.LogAppender;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class LoggerTranslator {

    private final ModelMapper modelMapper = new ModelMapper();

    public LoggerDto toDto(AppLogger logger) {
        LoggerDto dto = modelMapper.map(logger, LoggerDto.class);
        dto.setAppenderNames(logger.getAppenders().stream()
                .map(LogAppender::getName).collect(Collectors.toList()));
        return dto;
    }

    public AppLogger toModel(LoggerDto loggerDto) {
        return modelMapper.map(loggerDto, AppLogger.class);
    }

    public AppLogger toModel(String loggerName, EditLoggerDto editLoggerDto) {
        AppLogger logger = modelMapper.map(editLoggerDto, AppLogger.class);
        logger.setName(loggerName);
        return logger;
    }
}
