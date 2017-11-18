/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.base;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.MISSING_PERMISSION;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.members.rest.MembersRestConstants.MEMBERS;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID_VAR;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_DELETE_MEMBERS;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.permissions.api.annotation.AdministratorPermissionRequired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBERS, description = SPACE)
@RequestMapping(value = API_V1 + MEMBERS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberResource {

    private final MemberService memberService;

    private final MemberPublicTranslator memberPublicTranslator;
    private final MemberCriteriaTranslator memberCriteriaTranslator;

    @GetMapping
    @ApiOperation("Gets members by criteria")
    public Page<MemberPublicDto> memberGet(
        @Validated @ModelAttribute MemberCriteriaDto criteriaDto) {
        MemberSearchCriteria criteria = memberCriteriaTranslator.toModel(criteriaDto);
        Page<MemberRegistrationAware> matchedMembers = memberService
            .getAllMembersWithCriteria(criteria);
        return matchedMembers.map(memberPublicTranslator::toDto);
    }

    @DeleteMapping(MEMBER_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes member by id")
    @ErrorInfoCodes({MEMBER_NOT_FOUND, MISSING_PERMISSION, UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @AdministratorPermissionRequired(CAN_DELETE_MEMBERS)
    public void memberDelete(@PathVariable(MEMBER_ID_VAR) Long memberId)
        throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        memberService.removeMember(member.getId());
    }

}
