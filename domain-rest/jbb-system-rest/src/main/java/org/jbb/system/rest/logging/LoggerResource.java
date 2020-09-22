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

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.system.api.logging.LoggerNotFoundException;
import org.jbb.system.api.logging.LoggingSettingsService;
import org.jbb.system.api.logging.model.AppLogger;
import org.jbb.system.api.metrics.MetricsConfigException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static java.util.stream.Collectors.toList;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_METRIC_SETTINGS;
import static org.jbb.lib.restful.domain.ErrorInfo.LOGGER_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_LOGGING_SETTINGS_READ_SCOPE;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_LOGGING_SETTINGS_READ_WRITE_SCOPE;
import static org.jbb.system.rest.SystemRestConstants.LOGGERS;
import static org.jbb.system.rest.SystemRestConstants.LOGGER_NAME;
import static org.jbb.system.rest.SystemRestConstants.LOGGER_NAME_VAR;
import static org.jbb.system.rest.SystemRestConstants.LOGGING_SETTINGS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + LOGGING_SETTINGS + LOGGERS)
@RequestMapping(value = API_V1 + LOGGING_SETTINGS + LOGGERS, produces = MediaType.APPLICATION_JSON_VALUE)
public class LoggerResource {

    private final LoggingSettingsService loggingSettingsService;

    private final LoggerTranslator translator;
    private final DefaultRestExceptionMapper exceptionMapper;

    @GetMapping
    @ApiOperation("Gets loggers")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_LOGGING_SETTINGS_READ_SCOPE)
    public LoggersDto loggersGet() {
        List<AppLogger> loggers = loggingSettingsService.getLoggingConfiguration().getLoggers();
        return LoggersDto.builder()
                .loggers(loggers.stream()
                        .map(translator::toDto).collect(toList()))
                .build();
    }

    @GetMapping(LOGGER_NAME)
    @ApiOperation("Gets logger by name")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, LOGGER_NOT_FOUND})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_LOGGING_SETTINGS_READ_SCOPE)
    public LoggerDto loggerGet(@PathVariable(LOGGER_NAME_VAR) String loggerName) throws LoggerNotFoundException {
        AppLogger logger = loggingSettingsService.getLoggerChecked(loggerName);
        return translator.toDto(logger);
    }

    @PostMapping
    @ApiOperation("Creates logger")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_LOGGING_SETTINGS_READ_WRITE_SCOPE)
    public LoggerDto loggerPost(@RequestBody LoggerDto loggerDto) {
        AppLogger logger = translator.toModel(loggerDto);
        loggingSettingsService.addLogger(logger);
        return translator.toDto(logger);
    }

    @PutMapping(LOGGER_NAME)
    @ApiOperation("Updates logger by name")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, LOGGER_NOT_FOUND})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_LOGGING_SETTINGS_READ_WRITE_SCOPE)
    public LoggerDto loggerPut(@PathVariable(LOGGER_NAME_VAR) String loggerName, @RequestBody EditLoggerDto editLoggerDto) {
        AppLogger logger = translator.toModel(loggerName, editLoggerDto);
        loggingSettingsService.updateLogger(logger);
        return translator.toDto(logger);
    }

    @DeleteMapping(LOGGER_NAME)
    @ApiOperation("Removes logger by name")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, LOGGER_NOT_FOUND})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_LOGGING_SETTINGS_READ_WRITE_SCOPE)
    public void loggerDelete(@PathVariable(LOGGER_NAME_VAR) String loggerName) throws LoggerNotFoundException {
        AppLogger logger = loggingSettingsService.getLoggerChecked(loggerName);
        loggingSettingsService.deleteLogger(logger);
    }

    @ExceptionHandler(MetricsConfigException.class)
    ResponseEntity<ErrorResponse> handle(MetricsConfigException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_METRIC_SETTINGS);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(LoggerNotFoundException.class)
    ResponseEntity<ErrorResponse> handle(LoggerNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(LOGGER_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
