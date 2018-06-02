/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.logging;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.system.rest.SystemRestConstants.LOGGERS;
import static org.jbb.system.rest.SystemRestConstants.LOGGER_NAME;
import static org.jbb.system.rest.SystemRestConstants.LOGGER_NAME_VAR;
import static org.jbb.system.rest.SystemRestConstants.LOGGING_SETTINGS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + LOGGING_SETTINGS + LOGGERS)
@RequestMapping(value = API_V1 + LOGGING_SETTINGS + LOGGERS, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoggerResource {

    @GetMapping
    @ApiOperation("Gets loggers")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public LoggersDto loggersGet() {
        return LoggersDto.builder().build();
    }

    @GetMapping(LOGGER_NAME)
    @ApiOperation("Gets logger by name")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public LoggerDto loggerGet(@PathVariable(LOGGER_NAME_VAR) String loggerName) {
        return LoggerDto.builder().build();
    }

    @PostMapping
    @ApiOperation("Creates logger")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public LoggerDto loggerPost(@RequestBody LoggerDto loggerDto) {
        return LoggerDto.builder().build();
    }

    @PutMapping(LOGGER_NAME)
    @ApiOperation("Updates logger by name")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public LoggerDto loggerPut(@PathVariable(LOGGER_NAME_VAR) String loggerName, @RequestBody EditLoggerDto editLoggerDto) {
        return LoggerDto.builder().build();
    }

    @DeleteMapping(LOGGER_NAME)
    @ApiOperation("Removes logger by name")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void loggerDelete(@PathVariable(LOGGER_NAME_VAR) String loggerName) {
    }
}
