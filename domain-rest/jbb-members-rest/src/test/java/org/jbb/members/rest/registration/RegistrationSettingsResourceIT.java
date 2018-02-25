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


import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.rest.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class RegistrationSettingsResourceIT extends BaseIT {

    @Autowired
    RegistrationService registrationServiceMock;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldReturnRegistrationSettings_whenAuthenticatedAsAdministrator()
            throws Exception {
        // given
        given(registrationServiceMock.isEmailDuplicationAllowed()).willReturn(true);
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        MockMvcResponse response = request.when().get("/api/v1/registration-settings");

        // then
        response.then().statusCode(200);
        RegistrationSettingsDto resultBody = response.then().extract().body()
                .as(RegistrationSettingsDto.class);

        assertThat(resultBody.getEmailDuplicationAllowed()).isTrue();
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void getRegistrationSettings_shouldNotBeAvailableForRegularMembers() throws Exception {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        MockMvcResponse response = request.when().get("/api/v1/registration-settings");

        // then
        response.then().statusCode(403);
    }

    @Test
    public void getRegistrationSettings_shouldNotBeAvailableForGuests() throws Exception {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        // when
        MockMvcResponse response = request.when().get("/api/v1/registration-settings");

        // then
        response.then().statusCode(401);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void shouldPutRegistrationSettings_whenAuthenticatedAsAdministrator() throws Exception {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RegistrationSettingsDto.builder().emailDuplicationAllowed(false).build());

        // when
        MockMvcResponse response = request.when().put("/api/v1/registration-settings");

        // then
        response.then().statusCode(200);
        RegistrationSettingsDto resultBody = response.then().extract().body()
                .as(RegistrationSettingsDto.class);

        assertThat(resultBody.getEmailDuplicationAllowed()).isFalse();
        verify(registrationServiceMock).allowEmailDuplication(eq(false));
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void putRegistrationSettings_shouldNotBeAvailableForRegularMembers() throws Exception {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RegistrationSettingsDto.builder().emailDuplicationAllowed(false).build());

        // when
        MockMvcResponse response = request.when().put("/api/v1/registration-settings");

        // then
        response.then().statusCode(403);
    }

    @Test
    public void putRegistrationSettings_shouldNotBeAvailableForGuests() throws Exception {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(RegistrationSettingsDto.builder().emailDuplicationAllowed(false).build());

        // when
        MockMvcResponse response = request.when().put("/api/v1/registration-settings");

        // then
        response.then().statusCode(401);
    }


}