/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.me;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestAuthorize.PERMIT_ALL;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.members.rest.MembersRestConstants.ME;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + ME)
@RequestMapping(value = API_V1 + ME, produces = MediaType.APPLICATION_JSON_VALUE)
public class MeResource {

    private final MeDataProvider meDataProvider;

    @GetMapping
    @ErrorInfoCodes({})
    @ApiOperation("Gets info about current member and/or client")
    @PreAuthorize(PERMIT_ALL)
    public MeDataDto memberGet() {
        return meDataProvider.getMeData();
    }

}
