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
import static org.jbb.system.rest.SystemRestConstants.APPENDER_NAME;
import static org.jbb.system.rest.SystemRestConstants.APPENDER_NAME_VAR;
import static org.jbb.system.rest.SystemRestConstants.CONSOLE_APPENDERS;
import static org.jbb.system.rest.SystemRestConstants.LOGGING_SETTINGS;
import static org.jbb.system.rest.SystemRestConstants.LOG_APPENDERS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + LOGGING_SETTINGS + LOG_APPENDERS)
@RequestMapping(value = API_V1 + LOGGING_SETTINGS + LOG_APPENDERS + CONSOLE_APPENDERS, produces = MediaType.APPLICATION_JSON_VALUE)
public class LogConsoleAppenderResource {

    @GetMapping
    @ApiOperation("Gets console log appenders")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public LogConsoleAppendersDto consoleAppendersGet() {
        return LogConsoleAppendersDto.builder().build();
    }

    @GetMapping(APPENDER_NAME)
    @ApiOperation("Gets console log appender by name")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public LogConsoleAppenderDto consoleAppenderGet(@PathVariable(APPENDER_NAME_VAR) String appenderName) {
        return LogConsoleAppenderDto.builder().build();
    }

    @PostMapping
    @ApiOperation("Creates console log appender")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public LogConsoleAppenderDto consoleAppenderPost(@RequestBody LogConsoleAppenderDto logConsoleAppenderDto) {
        return LogConsoleAppenderDto.builder().build();
    }

    @PutMapping(APPENDER_NAME)
    @ApiOperation("Updates console log appender by name")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public LogConsoleAppenderDto consoleAppenderPut(@PathVariable(APPENDER_NAME_VAR) String appenderName,
                                                    @RequestBody EditLogConsoleAppenderDto editLogConsoleAppenderDto) {
        return LogConsoleAppenderDto.builder().build();
    }

    @DeleteMapping(APPENDER_NAME)
    @ApiOperation("Removes console log appender by name")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void consoleAppenderDelete(@PathVariable(APPENDER_NAME_VAR) String appenderName) {
    }
}
