/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.password;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.DefaultRestExceptionMapper;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordService;
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
import static org.jbb.lib.restful.domain.ErrorInfo.INVALID_PASSWORD_POLICY;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestConstants.PSWD_POLICY;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + PSWD_POLICY)
@RequestMapping(value = API_V1 + PSWD_POLICY, produces = MediaType.APPLICATION_JSON_VALUE)
public class PasswordPolicyResource {

    private final PasswordService passwordService;

    private final PasswordPolicyTranslator passwordPolicyTranslator;
    private final DefaultRestExceptionMapper exceptionMapper;

    @GetMapping
    @ApiOperation("Gets password policy")
    @ErrorInfoCodes({})
    public PasswordPolicyDto policyGet() {
        return passwordPolicyTranslator.toDto(passwordService.currentPolicy());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates password policy")
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ErrorInfoCodes({INVALID_PASSWORD_POLICY, UNAUTHORIZED, FORBIDDEN})
    public PasswordPolicyDto policyPut(@RequestBody PasswordPolicyDto passwordPolicyDto) {
        passwordService.updatePolicy(passwordPolicyTranslator.toModel(passwordPolicyDto));
        return passwordPolicyDto;
    }

    @ExceptionHandler(PasswordException.class)
    ResponseEntity<ErrorResponse> handle(PasswordException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(INVALID_PASSWORD_POLICY);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(exceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }
}
