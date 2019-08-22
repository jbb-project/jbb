/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.signin;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestAuthorize.PERMIT_ALL;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.security.rest.SecurityRestConstants.SIGN_OUT;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + SIGN_OUT)
@RequestMapping(value = API_V1 + SIGN_OUT, produces = MediaType.APPLICATION_JSON_VALUE)
public class SignOutResource {

    @PostMapping
    @ApiOperation("Signs out current member from system")
    @ErrorInfoCodes({})
    @PreAuthorize(PERMIT_ALL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signOut() {
        throw new IllegalStateException();
    }

}
