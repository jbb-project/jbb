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

import org.assertj.core.util.Lists;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.lib.restful.paging.PageDto;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.security.api.lockout.MemberLock;
import org.jbb.security.api.lockout.MemberLockoutService;
import org.jbb.security.rest.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.Optional;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MemberLockResourceIT extends BaseIT {

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
    public void gettingLocks_shouldBePossibleForAdministrators() {
        // given
        given(memberLockoutServiceMock.getLocksWithCriteria(any())).willReturn(exampleLockPage());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/member-locks");

        // then
        response.then().statusCode(200);
        PageDto<MemberLockDto> resultBody = response.then().extract().body().as(PageDto.class);
        assertThat(resultBody.getContent()).hasSize(1);
    }

    @Test
    @WithMockUser(username = "member")
    public void gettingLocks_shouldBeNotPossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/member-locks");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void gettingLocks_shouldBeNotPossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/member-locks");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void gettingLocksForSpecifiedExistingMember() {
        // given
        Long memberId = 120L;
        given(memberServiceMock.getMemberWithId(eq(memberId))).willReturn(Optional.of(mock(Member.class)));
        given(memberLockoutServiceMock.getLocksWithCriteria(any())).willReturn(exampleLockPage());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .param("memberId", "120");
        MockMvcResponse response = request.when().get("/api/v1/member-locks");

        // then
        response.then().statusCode(200);
        PageDto<MemberLockDto> resultBody = response.then().extract().body().as(PageDto.class);
        assertThat(resultBody.getContent()).hasSize(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void gettingLocksForNotExistingMember_shouldReturnEmptyPage() {
        // given
        Long memberId = 120L;
        given(memberServiceMock.getMemberWithId(eq(memberId))).willReturn(Optional.empty());
        given(memberLockoutServiceMock.getLocksWithCriteria(any())).willReturn(exampleLockPage());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .param("memberId", "120");
        MockMvcResponse response = request.when().get("/api/v1/member-locks");

        // then
        response.then().statusCode(200);
        PageDto<MemberLockDto> resultBody = response.then().extract().body().as(PageDto.class);
        assertThat(resultBody.getContent()).hasSize(0);
        verify(memberLockoutServiceMock, times(0)).getLocksWithCriteria(any());
    }

    private Page<MemberLock> exampleLockPage() {
        return new PageImpl<>(Lists.newArrayList(MemberLock.builder()
                .memberId(120L)
                .active(false)
                .createDateTime(LocalDateTime.of(2017, 6, 30, 11, 0))
                .expirationDateTime(LocalDateTime.of(2017, 6, 30, 11, 30))
                .deactivationDateTime(LocalDateTime.of(2017, 6, 30, 11, 5))
                .build()
        ), PageRequest.of(0, 1), 1L);
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }
}