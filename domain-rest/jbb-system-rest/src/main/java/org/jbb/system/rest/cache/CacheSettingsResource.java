/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.cache;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.system.api.cache.CacheConfigException;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.CacheSettingsService;
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

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_CACHE_SETTINGS;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_CACHE_SETTINGS_READ_SCOPE;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_CACHE_SETTINGS_READ_WRITE_SCOPE;
import static org.jbb.system.rest.SystemRestConstants.CACHE_SETTINGS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + CACHE_SETTINGS)
@RequestMapping(value = API_V1 + CACHE_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class CacheSettingsResource {

    private final CacheSettingsService cacheSettingsService;

    private final CacheSettingsTranslator cacheSettingsTranslator;
    private final DefaultRestExceptionMapper exceptionMapper;

    @GetMapping
    @ApiOperation("Gets cache settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_CACHE_SETTINGS_READ_SCOPE)
    public CacheSettingsDto settingsGet() {
        return cacheSettingsTranslator.toDto(cacheSettingsService.getCacheSettings());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates cache settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN, INVALID_CACHE_SETTINGS})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_CACHE_SETTINGS_READ_WRITE_SCOPE)
    public CacheSettingsDto settingsPut(@RequestBody EditCacheSettingsDto editCacheSettingsDto) {
        CacheSettings newSettings = cacheSettingsTranslator.toModel(editCacheSettingsDto);
        cacheSettingsService.setCacheSettings(newSettings);
        return cacheSettingsTranslator.toDto(newSettings);
    }

    @ExceptionHandler(CacheConfigException.class)
    ResponseEntity<ErrorResponse> handle(CacheConfigException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_CACHE_SETTINGS);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
