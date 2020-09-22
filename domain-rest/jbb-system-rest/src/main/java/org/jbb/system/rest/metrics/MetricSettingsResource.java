/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.metrics;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.system.api.metrics.MetricSettings;
import org.jbb.system.api.metrics.MetricSettingsService;
import org.jbb.system.api.metrics.MetricsConfigException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import javax.validation.ConstraintViolation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_METRIC_SETTINGS;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_METRICS_SETTINGS_READ_SCOPE;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_METRICS_SETTINGS_READ_WRITE_SCOPE;
import static org.jbb.system.rest.SystemRestConstants.METRIC_SETTINGS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + METRIC_SETTINGS)
@RequestMapping(value = API_V1 + METRIC_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MetricSettingsResource {

    private final MetricSettingsService metricSettingsService;

    private final MetricSettingsTranslator metricSettingsTranslator;
    private final DefaultRestExceptionMapper exceptionMapper;

    @GetMapping
    @ApiOperation("Gets metric settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_METRICS_SETTINGS_READ_SCOPE)
    public MetricSettingsDto settingsGet() {
        return metricSettingsTranslator.toDto(metricSettingsService.getMetricSettings());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates metric settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, INVALID_METRIC_SETTINGS})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_METRICS_SETTINGS_READ_WRITE_SCOPE)
    public MetricSettingsDto settingsPut(@RequestBody @Validated MetricSettingsDto metricSettingsDto) {
        MetricSettings metricSettings = metricSettingsTranslator.toModel(metricSettingsDto);
        metricSettingsService.setMetricSettings(metricSettings);
        return metricSettingsTranslator.toDto(metricSettings);
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

}
