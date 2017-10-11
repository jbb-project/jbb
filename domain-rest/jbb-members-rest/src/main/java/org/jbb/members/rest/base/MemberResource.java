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
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.members.rest.MembersRestConstants.MEMBERS;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID_VAR;
import static org.jbb.permissions.api.permission.domain.AdministratorPermissions.CAN_DELETE_MEMBERS;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorDetail;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.permissions.api.annotation.AdministratorPermissionRequired;
import org.jbb.permissions.api.exceptions.PermissionRequiredException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    private final MemberTranslator memberTranslator;
    private final MemberCriteriaTranslator memberCriteriaTranslator;

    @GetMapping(MEMBER_ID)
    @ApiOperation("Finds member by id")
    public MemberDto memberGetSingle(@PathVariable(MEMBER_ID_VAR) Long memberId)
        throws MemberNotFoundRestException {
        Member member = memberService.getMemberWithId(memberId)
            .orElseThrow(MemberNotFoundRestException::new);
        return memberTranslator.toDto(member);
    }

    @GetMapping
    @ApiOperation("Finds members by criteria")
    public Page<MemberPublicDto> memberGet(@ModelAttribute MemberCriteriaDto criteriaDto)
        throws MemberNotFoundRestException {
        MemberSearchCriteria criteria = memberCriteriaTranslator.toModel(criteriaDto);
        Page<MemberRegistrationAware> matchedMembers = memberService
            .getAllMembersWithCriteria(criteria);
        return matchedMembers.map(memberTranslator::toPublicDto);
    }

    @DeleteMapping(MEMBER_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes member by id")
    @AdministratorPermissionRequired(CAN_DELETE_MEMBERS)
    public void memberDelete(@PathVariable(MEMBER_ID_VAR) Long memberId)
        throws MemberNotFoundRestException {
        Member member = memberService.getMemberWithId(memberId)
            .orElseThrow(MemberNotFoundRestException::new);
        memberService.removeMember(member.getId());
    }

    @ExceptionHandler(MemberNotFoundRestException.class)
    public ResponseEntity<ErrorResponse> handle(MemberNotFoundRestException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.MEMBER_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(PermissionRequiredException.class) //FIXME - move to jbb-permission-web/rest?
    public ResponseEntity<ErrorResponse> handle(PermissionRequiredException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.MISSING_PERMISSION);
        errorResponse.getDetails().add(new ErrorDetail("missingPermission", ex.getMessage()));
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
