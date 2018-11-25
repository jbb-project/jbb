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

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.MemberLock;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.rest.BaseIT;
import org.jbb.security.rest.MemberImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Optional;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class ActiveMemberLockResourceIT extends BaseIT {
    private static final LocalDateTime LOCK_CREATE_TIME = LocalDateTime.of(2017, 6, 30, 11, 0);
    private static final LocalDateTime LOCK_EXPIRATION_TIME = LocalDateTime.of(2017, 6, 30, 11, 30);

    @Autowired
    private MemberService memberServiceMock;

    @Autowired
    private MemberLockoutService memberLockoutServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(memberServiceMock);
        Mockito.reset(memberLockoutServiceMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void gettingActiveLock_shouldBePossibleForAdministrators() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        given(memberLockoutServiceMock.getMemberActiveLock(any())).willReturn(Optional.of(exampleLock()));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/members/123/active-lock");

        // then
        response.then().statusCode(200);
        MemberLockDto resultBody = response.then().extract().body().as(MemberLockDto.class);
        assertThat(resultBody.getMemberId()).isEqualTo(123L);
        assertThat(resultBody.getActive()).isTrue();
        assertThat(resultBody.getCreatedAt()).isEqualTo(LOCK_CREATE_TIME);
        assertThat(resultBody.getExpiresAt()).isEqualTo(LOCK_EXPIRATION_TIME);
        assertThat(resultBody.getDeactivatedAt()).isNull();
    }

    @Test
    @WithMockUser(username = "member")
    public void gettingActiveLock_shouldBeNotPossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/members/123/active-lock");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void gettingActiveLock_shouldBeNotPossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/members/123/active-lock");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void gettingActiveLock_whenActiveLockIsNotFound() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        given(memberLockoutServiceMock.getMemberActiveLock(any())).willReturn(Optional.empty());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/members/123/active-lock");

        // then
        assertError(response, 404, ErrorInfo.ACTIVE_MEMBER_LOCK_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void deletingActiveLock_shouldBePossibleForAdministrators() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        given(memberLockoutServiceMock.getMemberActiveLock(any())).willReturn(Optional.of(exampleLock()));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().delete("/api/v1/members/123/active-lock");

        // then
        response.then().statusCode(204);
    }

    @Test
    @WithMockUser(username = "member")
    public void deletingActiveLock_shouldBeNotPossibleForRegularMembers() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        given(memberLockoutServiceMock.getMemberActiveLock(any())).willReturn(Optional.of(exampleLock()));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().delete("/api/v1/members/123/active-lock");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void deletingActiveLock_shouldBeNotPossibleForGuests() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        given(memberLockoutServiceMock.getMemberActiveLock(any())).willReturn(Optional.of(exampleLock()));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().delete("/api/v1/members/123/active-lock");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void deletingActiveLock_whenActiveLockIsNotFound() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(exampleMember());
        given(memberLockoutServiceMock.getMemberActiveLock(any())).willReturn(Optional.empty());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().delete("/api/v1/members/123/active-lock");

        // then
        assertError(response, 404, ErrorInfo.ACTIVE_MEMBER_LOCK_NOT_FOUND);
    }

    private Member exampleMember() {
        return MemberImpl.builder()
                .id(123L)
                .username(Username.of("john"))
                .displayedName(DisplayedName.of("John"))
                .email(Email.of("john@john.com"))
                .build();
    }

    private MemberLock exampleLock() {
        return MemberLock.builder()
                .memberId(123L)
                .active(true)
                .createDateTime(LOCK_CREATE_TIME)
                .expirationDateTime(LOCK_EXPIRATION_TIME)
                .build();
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }

}