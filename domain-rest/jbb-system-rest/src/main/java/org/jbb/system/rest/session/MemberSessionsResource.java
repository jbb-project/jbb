/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.session;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_SESSION_READ_DELETE_SCOPE;
import static org.jbb.system.rest.SystemRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_SESSION_READ_SCOPE;
import static org.jbb.system.rest.SystemRestConstants.MEMBER_SESSIONS;
import static org.jbb.system.rest.SystemRestConstants.SESSION_ID;
import static org.jbb.system.rest.SystemRestConstants.SESSION_ID_VAR;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBER_SESSIONS)
@RequestMapping(value = API_V1 + MEMBER_SESSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberSessionsResource {

    @GetMapping
    @ApiOperation("Gets member sessions")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_SESSION_READ_SCOPE)
    public MemberSessionsDto sessionsGet() {
        return MemberSessionsDto.builder().build();
    }

    @DeleteMapping(SESSION_ID)
    @ApiOperation("Removes member sessions")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_SESSION_READ_DELETE_SCOPE)
    public void sessionDelete(@PathVariable(SESSION_ID_VAR) String sessionId) {

    }

}
