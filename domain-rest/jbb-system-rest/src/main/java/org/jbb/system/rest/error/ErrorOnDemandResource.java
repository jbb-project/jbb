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

import static org.jbb.lib.restful.RestConstants.API;
import static org.jbb.system.rest.SystemRestConstants.ERR;

import lombok.RequiredArgsConstructor;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
@ApiIgnore
@RequestMapping(value = API + ERR, produces = MediaType.APPLICATION_JSON_VALUE)
public class ErrorOnDemandResource {

    @GetMapping
    @ErrorInfoCodes({})
    public Object getError() {
        throw new IllegalArgumentException();
    }

}
