/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.lockout;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.security.api.lockout.LockoutSettingsService;
import org.jbb.security.api.lockout.MemberLockoutException;
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

import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_LOCKOUT_SETTINGS;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_LOCKOUT_SETTINGS;

@RestController
@RequiredArgsConstructor
@PreAuthorize(IS_AN_ADMINISTRATOR)
@Api(tags = API_V1 + MEMBER_LOCKOUT_SETTINGS)
@RequestMapping(value = API_V1 + MEMBER_LOCKOUT_SETTINGS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberLockoutSettingsResource {

    private final LockoutSettingsService lockoutSettingsService;

    private final MemberLockoutSettingsTranslator translator;
    private final DefaultRestExceptionMapper exceptionMapper;

    @GetMapping
    @ApiOperation("Gets member lockout settings")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    public MemberLockoutSettingsDto memberLockoutSettingsGet() {
        return translator.toDto(lockoutSettingsService.getLockoutSettings());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates member lockout settings")
    @ErrorInfoCodes({INVALID_LOCKOUT_SETTINGS, UNAUTHORIZED, FORBIDDEN})
    public MemberLockoutSettingsDto memberLockoutSettingsPut(@RequestBody MemberLockoutSettingsDto memberLockoutSettingsDto) {
        lockoutSettingsService.setLockoutSettings(translator.toModel(memberLockoutSettingsDto));
        return memberLockoutSettingsDto;
    }

    @ExceptionHandler(MemberLockoutException.class)
    ResponseEntity<ErrorResponse> handle(MemberLockoutException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_LOCKOUT_SETTINGS);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
