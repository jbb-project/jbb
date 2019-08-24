/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.signin;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.security.api.signin.SignInSettingsException;
import org.jbb.security.api.signin.SignInSettingsService;
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
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_SIGN_IN_SETTINGS;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_SIGN_IN_SETTINGS_READ_WRITE_SCOPE;
import static org.jbb.security.rest.SecurityRestAuthorize.PERMIT_ALL_OR_OAUTH_SIGN_IN_SETTINGS_READ_SCOPE;
import static org.jbb.security.rest.SecurityRestConstants.SIGN_IN_SETTINGS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + SIGN_IN_SETTINGS)
@RequestMapping(value = API_V1 + SIGN_IN_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class SignInSettingsResource {

    private final SignInSettingsService signInSettingsService;

    private final SignInSettingsTranslator translator;
    private final DefaultRestExceptionMapper exceptionMapper;

    @GetMapping
    @ApiOperation("Gets sign in settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_SIGN_IN_SETTINGS_READ_SCOPE)
    public SignInSettingsDto signInSettingsGet() {
        return translator.toDto(signInSettingsService.getSignInSettings());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates sign in settings")
    @ErrorInfoCodes({INVALID_SIGN_IN_SETTINGS, UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_SIGN_IN_SETTINGS_READ_WRITE_SCOPE)
    public SignInSettingsDto signInSettingsPut(@RequestBody SignInSettingsDto signInSettingsDto) {
        signInSettingsService.setSignInSettings(translator.toModel(signInSettingsDto));
        return signInSettingsDto;
    }

    @ExceptionHandler(SignInSettingsException.class)
    ResponseEntity<ErrorResponse> handle(SignInSettingsException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_SIGN_IN_SETTINGS);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
