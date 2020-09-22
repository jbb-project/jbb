/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.rest.role;

import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.eventbus.MemberPermissionRoleAwareEvent;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.sse.BaseStreamResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.RestConstants.SSE_STREAM;
import static org.jbb.permissions.rest.PermissionsRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_MEMBER_ROLE_READ_SCOPE;
import static org.jbb.permissions.rest.PermissionsRestConstants.MEMBER_PERMISSIONS;
import static org.jbb.permissions.rest.PermissionsRestConstants.ROLES;

@RestController
@Api(tags = API_V1 + MEMBER_PERMISSIONS + ROLES)
@RequestMapping(value = API_V1 + MEMBER_PERMISSIONS + ROLES + SSE_STREAM, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public class MemberPermissionRoleStreamResource extends BaseStreamResource {

    public MemberPermissionRoleStreamResource(UserDetailsSource userDetailsSource) {
        super(userDetailsSource);
    }

    @Override
    protected AffectedMembers affectedMembers(JbbEvent jbbEvent) {
        if (jbbEvent instanceof MemberPermissionRoleAwareEvent) {
            return AffectedMembers.allAuthorizedToEndpointMembers();
        }
        return AffectedMembers.noneMember();
    }

    @GetMapping
    @ErrorInfoCodes({})
    @ApiOperation("Gets SSE stream with events related to member permission roles updates")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_MEMBER_ROLE_READ_SCOPE)
    public SseEmitter getEventStream(@RequestParam(name = "timeout", defaultValue = "30000") Long timeout,
                                     HttpServletResponse response) {
        return super.getEventStream(timeout);
    }

}
