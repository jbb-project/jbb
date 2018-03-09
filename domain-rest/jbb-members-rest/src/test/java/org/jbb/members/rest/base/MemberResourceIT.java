/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.base;

import org.assertj.core.util.Lists;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberNotFoundException;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.rest.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class MemberResourceIT extends BaseIT {

    @Autowired
    MemberService memberServiceMock;

    @Test
    public void shouldReturnMembersByCriteria() {
        // given
        MemberRegistrationAware foundMember = getMemberRegistrationAwareMock(
                200L, "jacob", "Jacob", "jacob@jacob.com"
        );
        PageImpl<MemberRegistrationAware> page = new PageImpl<>(Lists.newArrayList(foundMember));
        given(memberServiceMock.getAllMembersWithCriteria(any())).willReturn(page);

        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .param("displayedName", "Jacob")
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        MockMvcResponse response = request.when().get("/api/v1/members");

        // then
        response.then().statusCode(200);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void deleteNotExistingMember() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any()))
                .willThrow(new MemberNotFoundException());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().delete("/api/v1/members/200");

        // then
        assertError(response, 404, ErrorInfo.MEMBER_NOT_FOUND);
    }

    @Test
    public void deleteAnyMember_shouldNotBePossibleForGuests() {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        MockMvcResponse response = request.when().delete("/api/v1/members/200");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void deleteAnyMember_shouldNotBePossibleForRegularMembers() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(mock(Member.class));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().delete("/api/v1/members/200");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void deleteAnyMember_shouldBePossibleForAdministrators() throws MemberNotFoundException {
        // given
        given(memberServiceMock.getMemberWithIdChecked(any())).willReturn(mock(Member.class));

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().delete("/api/v1/members/200");

        // then
        assertThat(response.statusCode()).isEqualTo(204);
    }

    private MemberRegistrationAware getMemberRegistrationAwareMock(Long id, String username, String displayedName, String email) {
        MemberRegistrationAware member = mock(MemberRegistrationAware.class);
        given(member.getId()).willReturn(id);
        given(member.getUsername()).willReturn(Username.builder().value(username).build());
        given(member.getEmail()).willReturn(Email.builder().value(email).build());
        given(member.getDisplayedName())
                .willReturn(DisplayedName.builder().value(displayedName).build());
        given(member.getRegistrationMetaData()).willReturn(new RegistrationMetaData() {
            @Override
            public LocalDateTime getJoinDateTime() {
                return LocalDateTime.now();
            }

            @Override
            public IPAddress getIpAddress() {
                return IPAddress.builder()
                        .value("127.0.0.1").build();
            }
        });
        return member;
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }
}