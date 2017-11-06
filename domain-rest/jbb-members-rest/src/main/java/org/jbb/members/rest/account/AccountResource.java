/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.account;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.jbb.lib.restful.RestAuthorize.IS_AUTHENTICATED;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.lib.restful.domain.ErrorInfo.UPDATE_ACCOUNT_FAILED;
import static org.jbb.lib.restful.domain.ErrorInfo.UPDATE_NOT_OWN_ACCOUNT;
import static org.jbb.members.rest.MembersRestConstants.ACCOUNT;
import static org.jbb.members.rest.MembersRestConstants.MEMBERS;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID_VAR;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorDetail;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.AccountDataToChange;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.password.PasswordService;
import org.jbb.security.api.role.RoleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBERS + MEMBER_ID + ACCOUNT, description = SPACE)
@RequestMapping(value = API_V1 + MEMBERS + MEMBER_ID
    + ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountResource {

    private final MemberService memberService;
    private final RoleService roleService;
    private final PasswordService passwordService;

    private final AccountTranslator accountTranslator;

    @GetMapping
    @ErrorInfoCodes({MEMBER_NOT_FOUND, UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets member account by member id")
    public AccountDto accountGet(@PathVariable(MEMBER_ID_VAR) Long memberId) {
        Member member = memberService.getMemberWithId(memberId)
            .orElseThrow(() -> new UsernameNotFoundException(memberId.toString()));
        return accountTranslator.toDto(member);
    }

    @PutMapping
    @PreAuthorize(IS_AUTHENTICATED)
    @ApiOperation("Updates member account by member id")
    @ErrorInfoCodes({MEMBER_NOT_FOUND, UPDATE_ACCOUNT_FAILED,
        BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT, UNAUTHORIZED, FORBIDDEN})
    public AccountDto accountPut(@PathVariable(MEMBER_ID_VAR) Long memberId,
        @RequestBody UpdateAccountDto updateAccountDto, Authentication authentication) {
        Member currentMember = getCurrentMember(authentication);
        boolean requestorHasAdminRole = roleService.hasAdministratorRole(currentMember.getId());
        // administrator need to provide current password only if wants update own account
        if (currentMember.getId().equals(memberId)) {
            if (currentPasswordIsIncorrect(memberId, updateAccountDto.getCurrentPassword())) {
                throw new BadCredentialsException();
            }
        } else if (!requestorHasAdminRole) {
            throw new UpdateNotOwnAccountException();
        }

        AccountDataToChange accountDataToChange = accountTranslator.toModel(updateAccountDto);
        memberService.updateAccount(memberId, accountDataToChange);
        return accountGet(memberId);
    }

    private Member getCurrentMember(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Optional<Member> member = memberService.getMemberWithUsername(
            Username.builder().value(currentUser.getUsername()).build());
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new UsernameNotFoundException(currentUser.getUsername());
        }
    }

    private boolean currentPasswordIsIncorrect(Long memberId, String currentPassword) {
        return !passwordService.verifyFor(memberId,
            Password.builder().value(currentPassword.toCharArray()).build());
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handle(AccountException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(UPDATE_ACCOUNT_FAILED);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
            .map(violation -> new ErrorDetail(violation.getPropertyPath().toString(),
                violation.getMessage()))
            .forEach(errorDetail -> errorResponse.getDetails().add(errorDetail));

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handle(BadCredentialsException ex) {
        return ErrorResponse.getErrorResponseEntity(BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT);
    }

    @ExceptionHandler(UpdateNotOwnAccountException.class)
    public ResponseEntity<ErrorResponse> handle(UpdateNotOwnAccountException ex) {
        return ErrorResponse.getErrorResponseEntity(UPDATE_NOT_OWN_ACCOUNT);
    }

}
