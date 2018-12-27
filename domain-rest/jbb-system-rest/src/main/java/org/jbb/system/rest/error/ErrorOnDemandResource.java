/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.error;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import static org.jbb.lib.restful.RestAuthorize.PERMIT_ALL;
import static org.jbb.lib.restful.RestConstants.API;
import static org.jbb.system.rest.SystemRestConstants.ERR;

@RestController
@RequiredArgsConstructor
@ApiIgnore
@RequestMapping(value = API + ERR, produces = MediaType.APPLICATION_JSON_VALUE)
public class ErrorOnDemandResource {

    @GetMapping
    @ErrorInfoCodes({})
    @ApiOperation("Gets internal api error (on demand)")
    @PreAuthorize(PERMIT_ALL)
    public Object getError() {
        throw new IllegalArgumentException();
    }

}
