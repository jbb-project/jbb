/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.password;

import org.assertj.core.util.Sets;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.security.api.password.PasswordException;
import org.jbb.security.api.password.PasswordPolicy;
import org.jbb.security.api.password.PasswordService;
import org.jbb.security.rest.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class PasswordPolicyResourceIT extends BaseIT {

    @Autowired
    private PasswordService passwordServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(passwordServiceMock);
    }

    @Test
    public void gettingPasswordPolicy_shouldBePossibleForEveryone() {
        // given
        given(passwordServiceMock.currentPolicy()).willReturn(examplePasswordPolicy());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/password-policy");

        // then
        response.then().statusCode(200);
        PasswordPolicyDto resultBody = response.then().extract().body().as(PasswordPolicyDto.class);
        assertThat(resultBody.getMinimumLength()).isEqualTo(4);
        assertThat(resultBody.getMaximumLength()).isEqualTo(20);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingPasswordPolicy_shouldBePossibleForAdministrators() {
        // given
        given(passwordServiceMock.currentPolicy()).willReturn(examplePasswordPolicy());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validPasswordPolicyDto())
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/password-policy");

        // then
        response.then().statusCode(200);
        PasswordPolicyDto resultBody = response.then().extract().body().as(PasswordPolicyDto.class);
        assertThat(resultBody.getMinimumLength()).isEqualTo(4);
        assertThat(resultBody.getMaximumLength()).isEqualTo(20);
    }

    @Test
    @WithMockUser(username = "member")
    public void updatingPasswordPolicy_shouldBeNotPossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validPasswordPolicyDto())
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/password-policy");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void updatingPasswordPolicy_shouldBeNotPossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validPasswordPolicyDto())
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/password-policy");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingPasswordPolicy_shouldFail_whenNewPolicyIsInvalid() {
        // given
        BDDMockito.willThrow(new PasswordException(Sets.newHashSet())).given(passwordServiceMock)
                .updatePolicy(any());

        PasswordPolicyDto passwordPolicyDto = validPasswordPolicyDto();
        passwordPolicyDto.setMinimumLength(6);
        passwordPolicyDto.setMaximumLength(3);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(passwordPolicyDto)
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/password-policy");

        // then
        assertError(response, 400, ErrorInfo.INVALID_PASSWORD_POLICY);
    }

    private PasswordPolicy examplePasswordPolicy() {
        return PasswordPolicy.builder()
                .minimumLength(4)
                .maximumLength(20)
                .build();
    }

    private PasswordPolicyDto validPasswordPolicyDto() {
        return PasswordPolicyDto.builder()
                .minimumLength(4)
                .maximumLength(20)
                .build();
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }

}