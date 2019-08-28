/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.install;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.system.api.install.AlreadyInstalledException;
import org.jbb.system.api.install.InstallationDataException;
import org.jbb.system.api.install.InstallationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import javax.validation.ConstraintViolation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestAuthorize.PERMIT_ALL;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.ALREADY_INSTALLED;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_INSTALLATION_SETTINGS;
import static org.jbb.system.rest.SystemRestConstants.INSTALLATION;
import static org.jbb.system.rest.SystemRestConstants.STATUS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + INSTALLATION)
@RequestMapping(value = API_V1 + INSTALLATION, produces = MediaType.APPLICATION_JSON_VALUE)
public class InstallationResource {

    private final InstallationService installationService;

    private final InstallationRequestTranslator translator;
    private final DefaultRestExceptionMapper exceptionMapper;

    @GetMapping(STATUS)
    @ErrorInfoCodes({})
    @ApiOperation("Gets installation status")
    @PreAuthorize(PERMIT_ALL)
    public InstallationStatusDto getInstallationStatus() {
        return InstallationStatusDto.builder()
                .installed(installationService.isInstalled())
                .build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Performs the installation")
    @ErrorInfoCodes({INVALID_INSTALLATION_SETTINGS, ALREADY_INSTALLED})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(PERMIT_ALL)
    public InstallationStatusDto install(@RequestBody InstallationRequestDto installationRequestDto) {
        if (installationService.isInstalled()) {
            throw new AlreadyInstalledException();
        }
        installationService.install(translator.toModel(installationRequestDto));
        return InstallationStatusDto.builder()
                .installed(installationService.isInstalled())
                .build();
    }

    @ExceptionHandler(InstallationDataException.class)
    ResponseEntity<ErrorResponse> handle(InstallationDataException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_INSTALLATION_SETTINGS);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(AlreadyInstalledException.class)
    ResponseEntity<ErrorResponse> handle(AlreadyInstalledException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ALREADY_INSTALLED);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
