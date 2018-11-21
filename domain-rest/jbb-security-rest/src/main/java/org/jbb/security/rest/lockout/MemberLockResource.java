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

import com.google.common.collect.Lists;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.paging.PageDto;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.LockSearchCriteria;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.security.rest.SecurityRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_SCOPE;
import static org.jbb.security.rest.SecurityRestConstants.MEMBER_LOCKS;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBER_LOCKS)
@RequestMapping(value = API_V1 + MEMBER_LOCKS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberLockResource {

    private final MemberService memberService;
    private final MemberLockoutService memberLockoutService;

    private final MemberLockTranslator lockTranslator;
    private final LockCriteriaTranslator criteriaTranslator;

    @GetMapping
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets member locks")//FIXME
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_SCOPE)
    public PageDto<MemberLockDto> locksGet(
        @Validated @ModelAttribute LockCriteriaDto lockCriteria) {
        LockSearchCriteria criteria = criteriaTranslator.toModel(lockCriteria);
        if (!isMemberIdValid(criteria)) {
            return PageDto
                .getDto(new PageImpl<>(Lists.newArrayList(), criteria.getPageRequest(), 0L));
        }
        return PageDto
            .getDto(memberLockoutService.getLocksWithCriteria(criteria).map(lockTranslator::toDto));
    }

    private boolean isMemberIdValid(LockSearchCriteria criteria) {
        if (criteria.getMemberId() != null) {
            return memberService.getMemberWithId(criteria.getMemberId()).isPresent();
        }
        return true;
    }

}
