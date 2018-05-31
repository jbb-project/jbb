/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.rest.format;

import org.jbb.frontend.api.format.FormatException;
import org.jbb.frontend.api.format.FormatSettings;
import org.jbb.frontend.api.format.FormatSettingsService;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static org.jbb.frontend.rest.FrontendRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_FORMAT_SETTINGS_READ_WRITE_SCOPE;
import static org.jbb.frontend.rest.FrontendRestAuthorize.PERMIT_ALL_OR_OAUTH_FORMAT_SETTINGS_READ_SCOPE;
import static org.jbb.frontend.rest.FrontendRestConstants.FORMAT_SETTINGS;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_FORMAT_SETTINGS;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + FORMAT_SETTINGS)
@RequestMapping(value = API_V1 + FORMAT_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class FormatSettingsResource {

    private final FormatSettingsService formatSettingsService;

    private final FormatSettingsTranslator formatSettingsTranslator;
    private final FormatSettingsExceptionMapper formatSettingsExceptionMapper;

    @GetMapping
    @ErrorInfoCodes({})
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_FORMAT_SETTINGS_READ_SCOPE)
    @ApiOperation("Gets format settings")
    public FormatSettingsDto settingsGet() {
        FormatSettings formatSettings = formatSettingsService.getFormatSettings();
        return formatSettingsTranslator.toDto(formatSettings);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_FORMAT_SETTINGS_READ_WRITE_SCOPE)
    @ApiOperation("Updates format settings")
    @ErrorInfoCodes({INVALID_FORMAT_SETTINGS, UNAUTHORIZED, FORBIDDEN})
    public FormatSettingsDto settingsPut(@RequestBody FormatSettingsDto formatSettingsDto) {
        formatSettingsService.setFormatSettings(formatSettingsTranslator.toModel(formatSettingsDto));
        return formatSettingsDto;
    }

    @ExceptionHandler(FormatException.class)
    ResponseEntity<ErrorResponse> handle(FormatException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_FORMAT_SETTINGS);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(formatSettingsExceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
