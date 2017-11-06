/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.registration;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.members.rest.MembersRestConstants.REGISTRATION_SETTINGS;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@PreAuthorize(IS_AN_ADMINISTRATOR)
@Api(tags = API_V1 + REGISTRATION_SETTINGS, description = SPACE)
@RequestMapping(value = API_V1 + REGISTRATION_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationSettingsResource {

    private final RegistrationService registrationService;

    private final RegistrationSettingsTranslator registrationSettingsTranslator;

    @GetMapping
    @ApiOperation("Gets registration settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public RegistrationSettingsDto settingsGet() {
        boolean duplicationAllowed = registrationService.isEmailDuplicationAllowed();
        return registrationSettingsTranslator.toDto(duplicationAllowed);
    }

    @PutMapping
    @ApiOperation("Updates registration settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public RegistrationSettingsDto settingsPut(
        @RequestBody @Validated RegistrationSettingsDto registrationSettingsDto) {
        registrationService
            .allowEmailDuplication(registrationSettingsDto.getEmailDuplicationAllowed());
        return registrationSettingsDto;
    }

}
