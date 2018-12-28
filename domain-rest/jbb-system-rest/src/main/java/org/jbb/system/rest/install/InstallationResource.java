/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.install;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.system.api.install.InstallationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestAuthorize.PERMIT_ALL;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.system.rest.SystemRestConstants.INSTALLATION;
import static org.jbb.system.rest.SystemRestConstants.STATUS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + INSTALLATION)
@RequestMapping(value = API_V1 + INSTALLATION, produces = MediaType.APPLICATION_JSON_VALUE)
public class InstallationResource {

    private final InstallationService installationService;

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
    @ErrorInfoCodes({})
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize(PERMIT_ALL)
    public InstallationStatusDto install(@RequestBody InstallationRequestDto installationRequest) {
        return InstallationStatusDto.builder()
                .installed(installationService.isInstalled())
                .build();
    }
}
