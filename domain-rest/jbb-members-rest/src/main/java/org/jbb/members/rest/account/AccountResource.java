/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.account;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Password;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.AccountDataToChange;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.rest.account.exception.BadCredentials;
import org.jbb.members.rest.account.exception.GetNotOwnAccount;
import org.jbb.members.rest.account.exception.UpdateNotOwnAccount;
import org.jbb.members.rest.base.MemberExceptionMapper;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.security.api.password.PasswordService;
import org.jbb.security.api.privilege.PrivilegeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT;
import static org.jbb.lib.restful.domain.ErrorInfo.GET_NOT_OWN_ACCOUNT;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.MISSING_PERMISSION;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.lib.restful.domain.ErrorInfo.UPDATE_ACCOUNT_FAILED;
import static org.jbb.lib.restful.domain.ErrorInfo.UPDATE_NOT_OWN_ACCOUNT;
import static org.jbb.members.rest.MembersRestAuthorize.IS_AUTHENTICATED_OR_OAUTH_MEMBER_ACCOUNT_READ_SCOPE;
import static org.jbb.members.rest.MembersRestAuthorize.IS_AUTHENTICATED_OR_OAUTH_MEMBER_ACCOUNT_READ_WRITE_SCOPE;
import static org.jbb.members.rest.MembersRestConstants.ACCOUNT;
import static org.jbb.members.rest.MembersRestConstants.MEMBERS;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID_VAR;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBERS + MEMBER_ID + ACCOUNT)
@RequestMapping(value = API_V1 + MEMBERS + MEMBER_ID
        + ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountResource {

    private final MemberService memberService;
    private final PrivilegeService privilegeService;
    private final PasswordService passwordService;
    private final PermissionService permissionService;

    private final AccountTranslator accountTranslator;

    private final MemberExceptionMapper memberExceptionMapper;

    @GetMapping
    @ErrorInfoCodes({MEMBER_NOT_FOUND, GET_NOT_OWN_ACCOUNT, UNAUTHORIZED})
    @ApiOperation("Gets member account by member id")
    @PreAuthorize(IS_AUTHENTICATED_OR_OAUTH_MEMBER_ACCOUNT_READ_SCOPE)
    public AccountDto accountGet(@PathVariable(MEMBER_ID_VAR) Long memberId)
            throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);
        Optional<Member> currentMember = memberService.getCurrentMember();
        Boolean requestorHasPrivilege = currentMember
                .map(cm -> privilegeService.hasAdministratorPrivilege(cm.getId()))
                .orElse(true);
        Boolean currentEqualTarget = currentMember.map(cm -> cm.getId().equals(memberId))
                .orElse(true);
        if (!currentEqualTarget && !requestorHasPrivilege) {
            throw new GetNotOwnAccount();
        }
        return accountTranslator.toDto(member);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Updates member account by member id")
    @ErrorInfoCodes({MEMBER_NOT_FOUND, UPDATE_ACCOUNT_FAILED, UPDATE_NOT_OWN_ACCOUNT,
            BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT, UNAUTHORIZED, MISSING_PERMISSION})
    @PreAuthorize(IS_AUTHENTICATED_OR_OAUTH_MEMBER_ACCOUNT_READ_WRITE_SCOPE)
    public AccountDto accountPut(@PathVariable(MEMBER_ID_VAR) Long memberId,
                                 @RequestBody UpdateAccountDto updateAccountDto) throws MemberNotFoundException {
        Member member = memberService.getMemberWithIdChecked(memberId);

        Optional<Member> currentMember = memberService.getCurrentMember();
        Boolean requestorHasPrivilege = currentMember
                .map(cm -> privilegeService.hasAdministratorPrivilege(cm.getId()))
                .orElse(true);

        // administrator needs to provide current password only if wants update own account
        if (currentMember.map(cm -> cm.getId().equals(member.getId())).orElse(false)) {
            if (currentPasswordIsIncorrect(member.getId(), updateAccountDto.getCurrentPassword())) {
                throw new BadCredentials();
            }
        } else if (requestorHasPrivilege) {
            permissionService.assertPermission(AdministratorPermissions.CAN_MANAGE_MEMBERS);
        } else {
            throw new UpdateNotOwnAccount();
        }

        AccountDataToChange accountDataToChange = accountTranslator.toModel(updateAccountDto);
        assertChangeEmailPossible(member, accountDataToChange);
        memberService.updateAccount(memberId, accountDataToChange);
        return accountGet(memberId);
    }

    private void assertChangeEmailPossible(Member member, AccountDataToChange accountDataToChange) {
        Optional<Email> newEmail = accountDataToChange.getEmail();
        if (newEmail.isPresent() && !member.getEmail().equals(newEmail.get())) {
            permissionService.assertPermission(MemberPermissions.CAN_CHANGE_EMAIL);
        }
    }

    private boolean currentPasswordIsIncorrect(Long memberId, String currentPassword) {
        String nullSafePassword = StringUtils.defaultIfEmpty(currentPassword, StringUtils.EMPTY);
        return !passwordService.verifyFor(memberId,
                Password.builder().value(nullSafePassword.toCharArray()).build());
    }

    @ExceptionHandler(AccountException.class)
    ResponseEntity<ErrorResponse> handle(AccountException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(UPDATE_ACCOUNT_FAILED);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
                .map(memberExceptionMapper::mapToErrorDetail)
                .forEach(errorResponse.getDetails()::add);

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(BadCredentials.class)
    ResponseEntity<ErrorResponse> handle(BadCredentials ex) {
        return ErrorResponse.getErrorResponseEntity(BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT);
    }

    @ExceptionHandler(UpdateNotOwnAccount.class)
    ResponseEntity<ErrorResponse> handle(UpdateNotOwnAccount ex) {
        return ErrorResponse.getErrorResponseEntity(UPDATE_NOT_OWN_ACCOUNT);
    }

    @ExceptionHandler(GetNotOwnAccount.class)
    ResponseEntity<ErrorResponse> handle(GetNotOwnAccount ex) {
        return ErrorResponse.getErrorResponseEntity(GET_NOT_OWN_ACCOUNT);
    }

}
