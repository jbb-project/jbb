/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.registration;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.members.api.registration.RegistrationService;
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
import static org.jbb.members.rest.MembersRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_REGISTRATION_SETTINGS_READ_SCOPE;
import static org.jbb.members.rest.MembersRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_REGISTRATION_SETTINGS_READ_WRITE_SCOPE;
import static org.jbb.members.rest.MembersRestConstants.REGISTRATION_SETTINGS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + REGISTRATION_SETTINGS)
@RequestMapping(value = API_V1 + REGISTRATION_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationSettingsResource {

    private final RegistrationService registrationService;

    private final RegistrationSettingsTranslator registrationSettingsTranslator;

    @GetMapping
    @ApiOperation("Gets registration settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_REGISTRATION_SETTINGS_READ_SCOPE)
    public RegistrationSettingsDto settingsGet() {
        boolean duplicationAllowed = registrationService.isEmailDuplicationAllowed();
        return registrationSettingsTranslator.toDto(duplicationAllowed);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates registration settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_REGISTRATION_SETTINGS_READ_WRITE_SCOPE)
    public RegistrationSettingsDto settingsPut(
            @RequestBody @Validated RegistrationSettingsDto registrationSettingsDto) {
        registrationService
                .allowEmailDuplication(registrationSettingsDto.getEmailDuplicationAllowed());
        return registrationSettingsDto;
    }

}
