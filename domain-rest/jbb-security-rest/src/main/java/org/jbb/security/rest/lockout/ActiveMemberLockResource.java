/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.lockout;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.MemberLock;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.rest.lockout.exception.MemberLockNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.ACTIVE_MEMBER_LOCK_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_DELETE_SCOPE;
import static org.jbb.security.rest.SecurityRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_SCOPE;
import static org.jbb.security.rest.SecurityRestConstants.ACTIVE_LOCK;
import static org.jbb.security.rest.SecurityRestConstants.MEMBERS;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_ID;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_ID_VAR;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBERS + MEMBER_ID + ACTIVE_LOCK)
@RequestMapping(value = API_V1 + MEMBERS + MEMBER_ID
    + ACTIVE_LOCK, produces = MediaType.APPLICATION_JSON_VALUE)
public class ActiveMemberLockResource {

    private final MemberService memberService;
    private final MemberLockoutService memberLockoutService;

    private final MemberLockTranslator translator;

    @GetMapping
    @ErrorInfoCodes({MEMBER_NOT_FOUND, ACTIVE_MEMBER_LOCK_NOT_FOUND, UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets active lock for given member")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_SCOPE)
    public MemberLockDto activeLockGet(@PathVariable(MEMBER_ID_VAR) Long memberId)
        throws MemberNotFoundException {
        MemberLock lock = getMemberLock(memberId);
        return translator.toDto(lock);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ErrorInfoCodes({MEMBER_NOT_FOUND, ACTIVE_MEMBER_LOCK_NOT_FOUND, UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Deactivates active lock for given member")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_DELETE_SCOPE)
    public void activeLockDelete(@PathVariable(MEMBER_ID_VAR) Long memberId)
        throws MemberNotFoundException {
        getMemberLock(memberId);
        memberLockoutService.releaseMemberLock(memberId);
    }

    private MemberLock getMemberLock(Long memberId) throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        return memberLockoutService.getMemberActiveLock(member.getId())
            .orElseThrow(MemberLockNotFound::new);
    }

    @ExceptionHandler(MemberLockNotFound.class)
    ResponseEntity<ErrorResponse> handle(MemberLockNotFound ex) {
        return ErrorResponse.getErrorResponseEntity(ACTIVE_MEMBER_LOCK_NOT_FOUND);
    }
}
