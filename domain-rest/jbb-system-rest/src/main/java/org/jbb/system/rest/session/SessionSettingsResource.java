/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.session;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.system.api.session.SessionService;
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

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_SESSION_SETTINGS_READ_SCOPE;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_SESSION_SETTINGS_READ_WRITE_SCOPE;
import static org.jbb.system.rest.SystemRestConstants.SESSION_SETTINGS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + SESSION_SETTINGS)
@RequestMapping(value = API_V1 + SESSION_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionSettingsResource {

    private final SessionService sessionService;

    @GetMapping
    @ApiOperation("Gets session settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_SESSION_SETTINGS_READ_SCOPE)
    public SessionSettingsDto settingsGet() {
        return SessionSettingsDto.builder()
                .maxInactiveSessionInterval(sessionService.getMaxInactiveSessionInterval())
                .build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates session settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_SESSION_SETTINGS_READ_WRITE_SCOPE)
    public SessionSettingsDto settingsPut(@RequestBody @Validated SessionSettingsDto sessionSettingsDto) {
        sessionService.setMaxInactiveSessionInterval(sessionSettingsDto.getMaxInactiveSessionInterval());
        return SessionSettingsDto.builder()
                .maxInactiveSessionInterval(sessionService.getMaxInactiveSessionInterval())
                .build();
    }

}
