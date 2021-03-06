/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.profile;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.api.registration.RegistrationService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;
import static org.jbb.members.rest.MembersRestAuthorize.PERMIT_ALL_OR_OAUTH_MEMBER_READ_SCOPE;
import static org.jbb.members.rest.MembersRestConstants.MEMBERS;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID_VAR;
import static org.jbb.members.rest.MembersRestConstants.PUBLIC_PROFILE;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBERS + MEMBER_ID + PUBLIC_PROFILE)
@RequestMapping(value = API_V1 + MEMBERS + MEMBER_ID + PUBLIC_PROFILE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicProfileResource {

    private final MemberService memberService;
    private final RegistrationService registrationService;

    private final ProfileTranslator profileTranslator;

    @GetMapping
    @ErrorInfoCodes({MEMBER_NOT_FOUND})
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_MEMBER_READ_SCOPE)
    @ApiOperation("Gets member public profile by member id")
    public ProfilePublicDto publicProfileGet(@PathVariable(MEMBER_ID_VAR) Long memberId)
            throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        RegistrationMetaData registrationMetaData = registrationService
                .getRegistrationMetaData(memberId);
        return profileTranslator.toPublicDto(member, registrationMetaData);
    }

}
