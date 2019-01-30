/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest.webhooks;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import static org.jbb.integration.rest.IntegrationRestConstants.LOOPBACK;
import static org.jbb.lib.restful.RestAuthorize.PERMIT_ALL;
import static org.jbb.lib.restful.RestConstants.API;

@RestController
@RequiredArgsConstructor
@ApiIgnore
@RequestMapping(value = API + LOOPBACK, produces = MediaType.TEXT_PLAIN_VALUE)
public class LoopbackResource {

    @PostMapping
    @ErrorInfoCodes({})
    @ApiOperation("Post any content to loopback")
    @PreAuthorize(PERMIT_ALL)
    public String loopback(@RequestBody Object object) {
        return "OK";
    }

}
