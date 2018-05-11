/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.privilege;

import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_HAS_NOT_ADMIN_PRIVILEGES;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestConstants.ADMINISTRATOR_PRIVILEGES;
import static org.jbb.security.rest.SecurityRestConstants.MEMBERS;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_ID;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_ID_VAR;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.privilege.PrivilegeService;
import org.jbb.security.rest.privilege.exception.AdministratorPrivilegesNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBERS + MEMBER_ID + ADMINISTRATOR_PRIVILEGES)
@RequestMapping(value = API_V1 + MEMBERS + MEMBER_ID + ADMINISTRATOR_PRIVILEGES, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministratorResource {

    private final MemberService memberService;
    private final PrivilegeService privilegeService;

    @PutMapping
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ApiOperation("Adds administrator privileges to a given member")
    @ErrorInfoCodes({MEMBER_NOT_FOUND, UNAUTHORIZED, FORBIDDEN})
    public AdministratorPrivilegesDto privilegesPut(@PathVariable(MEMBER_ID_VAR) Long memberId) throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        privilegeService.addAdministratorPrivilege(member.getId());
        return new AdministratorPrivilegesDto(member.getId(), true);
    }

    @GetMapping
    @ApiOperation("Checks if given member has administrator privileges")
    @ErrorInfoCodes({MEMBER_NOT_FOUND, UNAUTHORIZED, FORBIDDEN})
    public AdministratorPrivilegesDto privilegesGet(@PathVariable(MEMBER_ID_VAR) Long memberId) throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        boolean hasPrivileges = privilegeService.hasAdministratorPrivilege(member.getId());
        return new AdministratorPrivilegesDto(member.getId(), hasPrivileges);
    }

    @DeleteMapping
    @PreAuthorize(IS_AN_ADMINISTRATOR)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes administrator privileges for a given member")
    @ErrorInfoCodes({MEMBER_NOT_FOUND, MEMBER_HAS_NOT_ADMIN_PRIVILEGES, UNAUTHORIZED, FORBIDDEN})
    public void privilegesDelete(@PathVariable(MEMBER_ID_VAR) Long memberId) throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        boolean removed = privilegeService.removeAdministratorPrivilege(member.getId());
        if (!removed) {
            throw new AdministratorPrivilegesNotFound();
        }
    }

    @ExceptionHandler(AdministratorPrivilegesNotFound.class)
    ResponseEntity<ErrorResponse> handle(AdministratorPrivilegesNotFound ex) {
        return ErrorResponse.getErrorResponseEntity(MEMBER_HAS_NOT_ADMIN_PRIVILEGES);
    }
}
