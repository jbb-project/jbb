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

import org.assertj.core.util.Sets;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.security.api.lockout.LockoutSettingsService;
import org.jbb.security.api.lockout.MemberLockoutException;
import org.jbb.security.api.lockout.MemberLockoutSettings;
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

public class MemberLockoutSettingsResourceIT extends BaseIT {

    @Autowired
    private LockoutSettingsService lockoutSettingsServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(lockoutSettingsServiceMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void gettingLockoutSettings_shouldBePossibleForAdministrators() {
        // given
        given(lockoutSettingsServiceMock.getLockoutSettings()).willReturn(exampleLockoutSettings());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/member-lockout-settings");

        // then
        response.then().statusCode(200);
        MemberLockoutSettingsDto resultBody = response.then().extract().body().as(MemberLockoutSettingsDto.class);
        assertThat(resultBody.getLockingEnabled()).isTrue();
        assertThat(resultBody.getLockoutDurationMinutes()).isEqualTo(120L);
        assertThat(resultBody.getFailedAttemptsThreshold()).isEqualTo(5);
        assertThat(resultBody.getFailedSignInAttemptsExpirationMinutes()).isEqualTo(10L);
    }

    @Test
    @WithMockUser(username = "member")
    public void gettingLockoutSettings_shouldBeNotPossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/member-lockout-settings");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void gettingLockoutSettings_shouldBeNotPossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/member-lockout-settings");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingLockoutSettings_shouldBePossibleForAdministrators() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validMemberLockoutSettingsDto())
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/member-lockout-settings");

        // then
        response.then().statusCode(200);
        MemberLockoutSettingsDto resultBody = response.then().extract().body().as(MemberLockoutSettingsDto.class);
        assertThat(resultBody.getLockingEnabled()).isTrue();
        assertThat(resultBody.getLockoutDurationMinutes()).isEqualTo(120L);
        assertThat(resultBody.getFailedAttemptsThreshold()).isEqualTo(5);
        assertThat(resultBody.getFailedSignInAttemptsExpirationMinutes()).isEqualTo(10L);
    }

    @Test
    @WithMockUser(username = "member")
    public void updatingLockoutSettings_shouldBeNotPossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validMemberLockoutSettingsDto())
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/member-lockout-settings");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void updatingLockoutSettings_shouldBeNotPossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(validMemberLockoutSettingsDto())
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/member-lockout-settings");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingLockoutSettings_shouldFailed_whenInvalidSettingsPassed() {
        // given
        MemberLockoutSettingsDto lockoutSettingsDto = validMemberLockoutSettingsDto();
        lockoutSettingsDto.setFailedAttemptsThreshold(-1);

        BDDMockito.willThrow(new MemberLockoutException(Sets.newHashSet())).given(lockoutSettingsServiceMock)
                .setLockoutSettings(any());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(lockoutSettingsDto)
                .contentType(ContentType.JSON);
        MockMvcResponse response = request.when().put("/api/v1/member-lockout-settings");

        // then
        assertError(response, 400, ErrorInfo.INVALID_LOCKOUT_SETTINGS);
    }

    private MemberLockoutSettings exampleLockoutSettings() {
        return MemberLockoutSettings.builder()
                .lockingEnabled(true)
                .lockoutDurationMinutes(120L)
                .failedAttemptsThreshold(5)
                .failedSignInAttemptsExpirationMinutes(10L)
                .build();
    }

    private MemberLockoutSettingsDto validMemberLockoutSettingsDto() {
        return MemberLockoutSettingsDto.builder()
                .lockingEnabled(true)
                .lockoutDurationMinutes(120L)
                .failedAttemptsThreshold(5)
                .failedSignInAttemptsExpirationMinutes(10L)
                .build();
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }

}