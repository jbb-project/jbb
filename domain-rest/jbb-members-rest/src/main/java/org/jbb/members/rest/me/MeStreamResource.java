/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.me;

import com.google.common.collect.Sets;

import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.eventbus.JbbEvent;
import org.jbb.lib.eventbus.MemberAwareEvent;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.sse.BaseStreamResource;
import org.jbb.security.event.SignInFailedEvent;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.RestConstants.SSE_STREAM;
import static org.jbb.members.rest.MembersRestAuthorize.IS_AUTHENTICATED_OR_OAUTH_MEMBER_SSE_STREAM_READ_SCOPE_AND_NOT_CLIENT_ONLY;
import static org.jbb.members.rest.MembersRestConstants.ME;

@RestController
@Api(tags = API_V1 + ME)
@RequestMapping(value = API_V1 + ME + SSE_STREAM, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public class MeStreamResource extends BaseStreamResource {

    public MeStreamResource(UserDetailsSource userDetailsSource) {
        super(userDetailsSource);
    }

    @Override
    public AffectedMembers affectedMembers(JbbEvent jbbEvent) {
        if (jbbEvent instanceof MemberAwareEvent) {
            Set<Long> ids = Sets.newHashSet(((MemberAwareEvent) jbbEvent).getMemberId());
            return AffectedMembers.onlyMembers(ids);
        } else if (jbbEvent instanceof SignInFailedEvent) {
            Optional<Long> optionalMemberId = ((SignInFailedEvent) jbbEvent).getMemberId();
            Set<Long> ids = optionalMemberId.map(Sets::newHashSet).orElse(new HashSet<>());
            return AffectedMembers.onlyMembers(ids);
        }
        return AffectedMembers.noneMember();
    }

    @GetMapping
    @ErrorInfoCodes({})
    @ApiOperation("Gets SSE stream with events related to current member")
    @PreAuthorize(IS_AUTHENTICATED_OR_OAUTH_MEMBER_SSE_STREAM_READ_SCOPE_AND_NOT_CLIENT_ONLY)
    public SseEmitter getEventStream(@RequestParam(name = "timeout", defaultValue = "30000") Long timeout,
                                     HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        return super.getEventStream(timeout);
    }

}
