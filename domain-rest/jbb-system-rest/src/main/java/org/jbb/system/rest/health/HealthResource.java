/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.health;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.system.api.health.HealthCheckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.system.rest.SystemRestAuthorize.PERMIT_ALL_OR_OAUTH_HEALTH_READ_SCOPE;
import static org.jbb.system.rest.SystemRestConstants.HEALTH;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + HEALTH)
@RequestMapping(value = API_V1 + HEALTH, produces = MediaType.APPLICATION_JSON_VALUE)
public class HealthResource {

    private final HealthCheckService healthCheckService;
    private final HealthTranslator healthTranslator;

    @GetMapping
    @ErrorInfoCodes({})
    @ApiOperation("Gets application health status")
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_HEALTH_READ_SCOPE)
    public ResponseEntity<HealthDto> getHealth() {
        HealthDto result = healthTranslator.toDto(healthCheckService.getHealth());
        if (result.getStatus().isHealthy()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(result);
        }
    }

}
