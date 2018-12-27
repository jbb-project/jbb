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

import com.google.common.collect.Sets;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.AccountException;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.rest.BaseIT;
import org.jbb.permissions.api.PermissionService;
import org.jbb.security.api.password.PasswordService;
import org.jbb.security.api.privilege.PrivilegeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


public class AccountResourcePutIT extends BaseIT {

    @Autowired
    MemberService memberServiceMock;

    @Autowired
    PasswordService passwordServiceMock;

    @Autowired
    PrivilegeService privilegeServiceMock;

    @Autowired
    PermissionService permissionServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(memberServiceMock, passwordServiceMock, privilegeServiceMock,
            permissionServiceMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updateAccountForNotExistingMember() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any()))
                .willThrow(new MemberNotFoundException());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateAccountDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/account");

        // then
        assertError(response, 404, ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    public void updateAnyAccount_shouldNotBePossibleForGuests() {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateAccountDto());

        // when
        MockMvcResponse response = request.when().put("/api/v1/members/200/account");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void updatingNotOwnAccount_shouldNotBePossibleForRegularMembers() throws MemberNotFoundException {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;
        Member targetMember = getMemberMock(id, username, displayedName, email);
        Member currentMember = getMemberMock(201L, "omc", "Arthur", "a@nsn.com");
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(targetMember);
        given(memberServiceMock.getCurrentMember()).willReturn(Optional.of(currentMember));
        given(privilegeServiceMock.hasAdministratorPrivilege(any())).willReturn(false);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateAccountDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/account");

        // then
        assertError(response, 403, ErrorInfo.UPDATE_NOT_OWN_ACCOUNT);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingNotOwnAccount_shouldBePossibleForAdministrators() throws MemberNotFoundException {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;
        Member targetMember = getMemberMock(id, username, displayedName, email);
        Member currentMember = getMemberMock(201L, "omc", "Arthur", "a@nsn.com");
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(targetMember);
        given(memberServiceMock.getCurrentMemberChecked()).willReturn(currentMember);
        given(privilegeServiceMock.hasAdministratorPrivilege(any())).willReturn(true);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateAccountDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/account");

        // then
        response.then().statusCode(200);
        AccountDto resultBody = response.then().extract().body().as(AccountDto.class);
        assertThat(resultBody.getEmail()).isEqualTo("jacob@example.com");
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void updatingOwnAccount_shouldBePossibleForRegularMembers() throws MemberNotFoundException {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;
        Member targetMember = getMemberMock(id, username, displayedName, email);
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(targetMember);
        given(memberServiceMock.getCurrentMemberChecked()).willReturn(targetMember);
        given(privilegeServiceMock.hasAdministratorPrivilege(any())).willReturn(false);
        given(passwordServiceMock.verifyFor(any(), any())).willReturn(true);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateAccountDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/account");

        // then
        response.then().statusCode(200);
        AccountDto resultBody = response.then().extract().body().as(AccountDto.class);
        assertThat(resultBody.getEmail()).isEqualTo("jacob@example.com");
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void updatingOwnAccount_whenInvalidDataProvided() throws MemberNotFoundException {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;
        Member targetMember = getMemberMock(id, username, displayedName, email);
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(targetMember);
        given(memberServiceMock.getCurrentMemberChecked()).willReturn(targetMember);
        given(privilegeServiceMock.hasAdministratorPrivilege(any())).willReturn(false);
        given(passwordServiceMock.verifyFor(any(), any())).willReturn(true);
        Mockito.doThrow(new AccountException(violation())).when(memberServiceMock).updateAccount(any(), any());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateAccountDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/account");

        // then
        assertError(response, 400, ErrorInfo.UPDATE_ACCOUNT_FAILED);
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void updatingOwnAccount_whenBadCredentialsProvided() throws MemberNotFoundException {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;
        Member targetMember = getMemberMock(id, username, displayedName, email);
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(targetMember);
        given(memberServiceMock.getCurrentMember()).willReturn(Optional.of(targetMember));
        given(privilegeServiceMock.hasAdministratorPrivilege(any())).willReturn(false);
        given(passwordServiceMock.verifyFor(any(), any())).willReturn(false);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateAccountDto());
        MockMvcResponse response = request.when().put("/api/v1/members/200/account");

        // then
        assertError(response, 400, ErrorInfo.BAD_CREDENTIALS_WHEN_UPDATE_ACCOUNT);
    }

    private Set<? extends ConstraintViolation<?>> violation() {
        Path propertyPath = mock(Path.class);
        given(propertyPath.toString()).willReturn("email");
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        given(violation.getPropertyPath()).willReturn(propertyPath);
        given(violation.getMessage()).willReturn("must be not blank");
        return Sets.newHashSet(violation);
    }

    private UpdateAccountDto updateAccountDto() {
        return UpdateAccountDto.builder()
                .currentPassword("aaa")
                .newPassword("aaa")
                .email("aa@aa.com")
                .build();
    }

    private Member getMemberMock(Long id, String username, String displayedName, String email) {
        Member member = mock(Member.class);
        given(member.getId()).willReturn(id);
        given(member.getUsername()).willReturn(Username.builder().value(username).build());
        given(member.getEmail()).willReturn(Email.builder().value(email).build());
        given(member.getDisplayedName())
                .willReturn(DisplayedName.builder().value(displayedName).build());
        return member;
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }

}