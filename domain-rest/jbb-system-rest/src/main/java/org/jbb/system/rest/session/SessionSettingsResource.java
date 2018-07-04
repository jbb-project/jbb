/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.session;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.system.rest.SystemRestConstants.SESSION_SETTINGS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + SESSION_SETTINGS)
@RequestMapping(value = API_V1 + SESSION_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionSettingsResource {

    @GetMapping
    @ApiOperation("Gets session settings")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public SessionSettingsDto settingsGet() {
        return SessionSettingsDto.builder().build();
    }

    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates session settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public SessionSettingsDto settingsPut(@RequestBody @Validated SessionSettingsDto sessionSettingsDto) {
        return SessionSettingsDto.builder().build();
    }

}
