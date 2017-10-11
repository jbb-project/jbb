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
import static org.jbb.lib.restful.RestConstants.API_V1;
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
import org.jbb.lib.restful.domain.ErrorInfo;
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
@PreAuthorize("isAuthenticated()")
@Api(tags = API_V1 + MEMBERS + MEMBER_ID + ACCOUNT, description = SPACE)
@RequestMapping(value = API_V1 + MEMBERS + MEMBER_ID
    + ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountResource {

    private final MemberService memberService;
    private final RoleService roleService;
    private final PasswordService passwordService;

    private final AccountTranslator accountTranslator;

    @GetMapping
    @ApiOperation("Gets member account by member id")
    public AccountDto accountGet(@PathVariable(MEMBER_ID_VAR) Long memberId) {
        Member member = memberService.getMemberWithId(memberId)
            .orElseThrow(() -> new UsernameNotFoundException(memberId.toString()));
        return accountTranslator.toDto(member);
    }

    @PutMapping
    @ApiOperation("Updates member account by member id")
    public AccountDto accountPut(@PathVariable(MEMBER_ID_VAR) Long memberId,
        @RequestBody UpdateAccountDto updateAccountDto, Authentication authentication)
        throws BadCurrentPasswordRestException {
        Member currentMember = getCurrentMember(authentication);
        boolean requestorHasAdminRole = roleService.hasAdministratorRole(currentMember.getId());
        // administrator need to provide current password only if wants update own account
        if (!requestorHasAdminRole || currentMember.getId().equals(memberId)) {
            if (currentPasswordIsIncorrect(memberId, updateAccountDto.getCurrentPassword())) {
                throw new BadCurrentPasswordRestException();
            }
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

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(UsernameNotFoundException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.MEMBER_NOT_FOUND);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handle(AccountException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.UPDATE_ACCOUNT_FAILED);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
            .map(violation -> new ErrorDetail(violation.getPropertyPath().toString(),
                violation.getMessage()))
            .forEach(errorDetail -> errorResponse.getDetails().add(errorDetail));

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(BadCurrentPasswordRestException.class)
    public ResponseEntity<ErrorResponse> handle(BadCurrentPasswordRestException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.BAD_CREDENTIALS);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
