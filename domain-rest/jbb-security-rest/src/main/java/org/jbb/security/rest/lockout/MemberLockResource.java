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

import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_LOCKS;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize(IS_AN_ADMINISTRATOR)
@Api(tags = API_V1 + MEMBER_LOCKS)
@RequestMapping(value = API_V1 + MEMBER_LOCKS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberLockResource {

    private final MemberService memberService;
    private final MemberLockoutService memberLockoutService;

    private final MemberLockTranslator lockTranslator;
    private final LockCriteriaTranslator criteriaTranslator;

    @GetMapping
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets member locks")
    public Page<MemberLockDto> activeLockGet(
        @Validated @ModelAttribute LockCriteriaDto lockCriteria) {
        return memberLockoutService.getLocksWithCriteria(criteriaTranslator.toModel(lockCriteria))
            .map(lockTranslator::toDto);
    }

}
