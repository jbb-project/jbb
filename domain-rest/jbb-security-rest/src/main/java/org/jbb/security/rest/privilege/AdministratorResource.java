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

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.privilege.PrivilegeService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_ADMINISTRATOR_PRIVILEGE_READ_WRITE_SCOPE;
import static org.jbb.security.rest.SecurityRestAuthorize.PERMIT_ALL_OR_OAUTH_ADMINISTRATOR_PRIVILEGE_READ_SCOPE;
import static org.jbb.security.rest.SecurityRestConstants.ADMINISTRATOR_PRIVILEGES;
import static org.jbb.security.rest.SecurityRestConstants.MEMBERS;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_ID;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_ID_VAR;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBERS + MEMBER_ID + ADMINISTRATOR_PRIVILEGES)
@RequestMapping(value = API_V1 + MEMBERS + MEMBER_ID + ADMINISTRATOR_PRIVILEGES, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministratorResource {

    private final MemberService memberService;
    private final PrivilegeService privilegeService;

    @GetMapping
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_ADMINISTRATOR_PRIVILEGE_READ_SCOPE)
    @ApiOperation("Checks if given member has administrator privileges")
    @ErrorInfoCodes({MEMBER_NOT_FOUND})
    public PrivilegesDto privilegesGet(@PathVariable(MEMBER_ID_VAR) Long memberId) throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        boolean hasPrivileges = privilegeService.hasAdministratorPrivilege(member.getId());
        return new PrivilegesDto(member.getId(), hasPrivileges);
    }

    @PutMapping
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_ADMINISTRATOR_PRIVILEGE_READ_WRITE_SCOPE)
    @ApiOperation("Adds or removes administrator privileges for a given member")
    @ErrorInfoCodes({MEMBER_NOT_FOUND, UNAUTHORIZED, FORBIDDEN})
    public PrivilegesDto privilegesPut(@PathVariable(MEMBER_ID_VAR) Long memberId,
                                       @Validated @RequestBody UpdatePrivilegesDto updatePrivilegesDto) throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        Boolean addPrivileges = updatePrivilegesDto.getAdministratorPrivileges();
        if (addPrivileges) {
            privilegeService.addAdministratorPrivilege(member.getId());
        } else {
            privilegeService.removeAdministratorPrivilege(member.getId());
        }
        return new PrivilegesDto(member.getId(), addPrivileges);
    }

}
