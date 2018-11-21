/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.rest.format;

import com.google.common.collect.Sets;

import org.jbb.frontend.api.format.FormatException;
import org.jbb.frontend.api.format.FormatSettings;
import org.jbb.frontend.api.format.FormatSettingsService;
import org.jbb.frontend.rest.BaseIT;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public class FormatSettingsResourceIT extends BaseIT {

    @Autowired
    private FormatSettingsService formatSettingsServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(formatSettingsServiceMock);
    }

    @Test
    public void gettingFormatSettings_success() {
        // given
        given(formatSettingsServiceMock.getFormatSettings()).willReturn(exampleFormatSettings());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/format-settings");

        // then
        response.then().statusCode(200);
        FormatSettingsDto resultBody = response.then().extract().body().as(FormatSettingsDto.class);
        assertThat(resultBody.getDateFormat()).isEqualTo("dd/MM/yyyy HH:mm:ss");
        assertThat(resultBody.getDurationFormat()).isEqualTo("HH:mm:ss");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingFormatSettings_shouldBePossibleForAdministrators() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(FormatSettingsDto.builder()
                        .dateFormat("dd/MM/yyyy HH:mm:ss")
                        .durationFormat("HH:mm:ss")
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/format-settings");

        // then
        response.then().statusCode(200);
        FormatSettingsDto resultBody = response.then().extract().body().as(FormatSettingsDto.class);
        assertThat(resultBody.getDateFormat()).isEqualTo("dd/MM/yyyy HH:mm:ss");
        assertThat(resultBody.getDurationFormat()).isEqualTo("HH:mm:ss");
    }

    @Test
    @WithMockUser(username = "member")
    public void updatingFormatSettings_shouldBeNotPossibleForRegularMembers() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(FormatSettingsDto.builder()
                        .dateFormat("dd/MM/yyyy HH:mm:ss")
                        .durationFormat("HH:mm:ss")
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/format-settings");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    @Test
    public void updatingFormatSettings_shouldBeNotPossibleForGuests() {
        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(FormatSettingsDto.builder()
                        .dateFormat("dd/MM/yyyy HH:mm:ss")
                        .durationFormat("HH:mm:ss")
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/format-settings");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void updatingFormatSettings_withInvalidSettings_shouldFailed() {
        // given
        ConstraintViolation<?> violation = Mockito.mock(ConstraintViolation.class);
        Path path = Mockito.mock(Path.class);
        given(violation.getPropertyPath()).willReturn(path);
        given(path.toString()).willReturn("dateFormat");
        BDDMockito.willThrow(new FormatException(Sets.newHashSet(violation))).given(formatSettingsServiceMock)
                .setFormatSettings(any());

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .body(FormatSettingsDto.builder()
                        .dateFormat("dd/MM/yyyy HxxH:mm:ss")
                        .durationFormat("HH:mm:ss")
                        .build()
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        MockMvcResponse response = request.when().put("/api/v1/format-settings");

        // then
        assertError(response, 400, ErrorInfo.INVALID_FORMAT_SETTINGS);
    }

    private FormatSettings exampleFormatSettings() {
        return FormatSettings.builder()
                .dateFormat("dd/MM/yyyy HH:mm:ss")
                .durationFormat("HH:mm:ss")
                .build();
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());
    }


}