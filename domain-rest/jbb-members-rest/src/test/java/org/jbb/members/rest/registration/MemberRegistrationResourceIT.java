/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import com.google.common.collect.Sets;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import java.util.Optional;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.RegistrationException;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.rest.BaseIT;
import org.jbb.members.rest.base.MemberPublicDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class MemberRegistrationResourceIT extends BaseIT {

    @Autowired
    MemberService memberServiceMock;
    @Autowired
    RegistrationService registrationServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(memberServiceMock, registrationServiceMock);
    }

    @Test
    public void shouldRegisterMember() throws Exception {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;

        RegistrationRequestDto registrationRequest = RegistrationRequestDto.builder()
            .username(username)
            .displayedName(displayedName)
            .email(email)
            .password("password")
            .build();

        Member createdMember = mock(Member.class);
        given(createdMember.getId()).willReturn(id);
        given(createdMember.getUsername()).willReturn(Username.builder().value(username).build());
        given(createdMember.getEmail()).willReturn(Email.builder().value(email).build());
        given(createdMember.getDisplayedName())
            .willReturn(DisplayedName.builder().value(displayedName).build());
        given(memberServiceMock.getMemberWithUsername(any()))
            .willReturn(Optional.of(createdMember));
        given(registrationServiceMock.getRegistrationMetaData(eq(id))).willReturn(mock(
            RegistrationMetaData.class));

        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(registrationRequest);

        // when
        MockMvcResponse response = request.when().post("/api/v1/members");

        // then
        response.then().statusCode(201);
        MemberPublicDto resultBody = response.then().extract().body().as(MemberPublicDto.class);

        assertThat(resultBody.getId()).isEqualTo(id);
        assertThat(resultBody.getDisplayedName()).isEqualTo(displayedName);
    }

    @Test
    public void shouldHandleRegistrationException() throws Exception {
        // given
        String username = "jacob";
        String email = "jacob@example.com";
        String displayedName = "Jacob";
        Long id = 200L;

        RegistrationRequestDto registrationRequest = RegistrationRequestDto.builder()
            .username(username)
            .displayedName(displayedName)
            .email(email)
            .password("password")
            .build();

        Member createdMember = mock(Member.class);
        given(createdMember.getId()).willReturn(id);
        given(createdMember.getUsername()).willReturn(Username.builder().value(username).build());
        given(createdMember.getEmail()).willReturn(Email.builder().value(email).build());
        given(createdMember.getDisplayedName())
            .willReturn(DisplayedName.builder().value(displayedName).build());
        doThrow(new RegistrationException(Sets.newHashSet())).when(registrationServiceMock)
            .register(any());

        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(registrationRequest);

        // when
        MockMvcResponse response = request.when().post("/api/v1/members");

        // then
        response.then().statusCode(400);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);

        assertThat(resultBody.getCode()).isEqualTo(ErrorInfo.REGISTRATION_FAILED.getCode());
    }
}