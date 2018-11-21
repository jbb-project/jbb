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

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.privilege.PrivilegeService;
import org.jbb.security.rest.BaseIT;
import org.jbb.security.rest.MemberImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AdministratorResourceIT extends BaseIT {
    @Autowired
    private MemberService memberServiceMock;

    @Autowired
    private PrivilegeService privilegeServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(memberServiceMock);
        Mockito.reset(privilegeServiceMock);
    }

    @Test
    public void gettingAdministratorPrivileges_shouldBePossibleForEveryone() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        given(privilegeServiceMock.hasAdministratorPrivilege(any())).willReturn(true);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/members/123/administrator-privileges");

        // then
        response.then().statusCode(200);
        PrivilegesDto resultBody = response.then().extract().body().as(PrivilegesDto.class);
        assertThat(resultBody.getMemberId()).isEqualTo(123L);
        assertThat(resultBody.getAdministratorPrivileges()).isEqualTo(true);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void addingAdministratorPrivileges_shouldBePossibleForAdministrators() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validUpdatePrivilegesDto())
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/members/123/administrator-privileges");

        // then
        response.then().statusCode(200);
        PrivilegesDto resultBody = response.then().extract().body().as(PrivilegesDto.class);
        assertThat(resultBody.getMemberId()).isEqualTo(123L);
        assertThat(resultBody.getAdministratorPrivileges()).isEqualTo(true);

        verify(privilegeServiceMock, times(1)).addAdministratorPrivilege(eq(123L));
        verifyNoMoreInteractions(privilegeServiceMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void removingAdministratorPrivileges_shouldBePossibleForAdministrators() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        UpdatePrivilegesDto privilegesDto = validUpdatePrivilegesDto();
        privilegesDto.setAdministratorPrivileges(false);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(privilegesDto)
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/members/123/administrator-privileges");

        // then
        response.then().statusCode(200);
        PrivilegesDto resultBody = response.then().extract().body().as(PrivilegesDto.class);
        assertThat(resultBody.getMemberId()).isEqualTo(123L);
        assertThat(resultBody.getAdministratorPrivileges()).isEqualTo(false);

        verify(privilegeServiceMock, times(1)).removeAdministratorPrivilege(eq(123L));
        verifyNoMoreInteractions(privilegeServiceMock);
    }

    @Test
    @WithMockUser(username = "member")
    public void updatingAdministratorPrivileges_shouldBeNotPossibleForRegularMembers() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validUpdatePrivilegesDto())
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/members/123/administrator-privileges");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void updatingAdministratorPrivileges_shouldBeNotPossibleForGuests() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validUpdatePrivilegesDto())
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/members/123/administrator-privileges");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingAdministratorPrivileges_shouldFail_whenNullPrivilegePassed() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        UpdatePrivilegesDto privilegesDto = validUpdatePrivilegesDto();
        privilegesDto.setAdministratorPrivileges(null);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(privilegesDto)
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/members/123/administrator-privileges");

        // then
        assertError(response, 400, ErrorInfo.VALIDATION_ERROR);
    }


    private Member exampleMember() {
        return MemberImpl.builder()
                .id(123L)
                .username(Username.of("john"))
                .displayedName(DisplayedName.of("John"))
                .email(Email.of("john@john.com"))
                .build();
    }

    private UpdatePrivilegesDto validUpdatePrivilegesDto() {
        return UpdatePrivilegesDto.builder()
                .administratorPrivileges(true).build();
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }

}