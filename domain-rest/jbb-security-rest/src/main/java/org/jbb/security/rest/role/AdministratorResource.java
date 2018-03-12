/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.role;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.role.RoleService;
import org.jbb.security.rest.role.exception.AlreadyNotAdministrator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_A_ADMINISTRATOR_ALREADY;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestConstants.ADMINISTRATORS;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_ID_PARAM;

@RestController
@RequiredArgsConstructor
@PreAuthorize(IS_AN_ADMINISTRATOR)
@Api(tags = API_V1 + ADMINISTRATORS)
@RequestMapping(value = API_V1 + ADMINISTRATORS, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministratorResource {

    private final MemberService memberService;
    private final RoleService roleService;

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Adds administrator privileges to a given member")
    @ErrorInfoCodes({MEMBER_NOT_FOUND, UNAUTHORIZED, FORBIDDEN})
    public AdministratorPrivilegesDto privilegesPut(@RequestBody @Validated AdministratorPrivilegesDto administratorPrivilegesDto) throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(administratorPrivilegesDto.getMemberId());
        roleService.addAdministratorRole(member.getId());
        return administratorPrivilegesDto;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes administrator privileges for a given member")
    @ErrorInfoCodes({MEMBER_NOT_FOUND, MEMBER_NOT_A_ADMINISTRATOR_ALREADY, UNAUTHORIZED, FORBIDDEN})
    public void privilegesDelete(@RequestParam(MEMBER_ID_PARAM) Long memberId) throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        boolean removed = roleService.removeAdministratorRole(member.getId());
        if (!removed) {
            throw new AlreadyNotAdministrator();
        }
    }

    @ExceptionHandler(AlreadyNotAdministrator.class)
    public ResponseEntity<ErrorResponse> handle(AlreadyNotAdministrator ex) {
        return ErrorResponse.getErrorResponseEntity(MEMBER_NOT_A_ADMINISTRATOR_ALREADY);
    }
}
