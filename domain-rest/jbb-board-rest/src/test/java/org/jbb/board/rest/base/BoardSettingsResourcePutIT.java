/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.rest.base;


import com.google.common.collect.Sets;

import org.jbb.board.api.base.BoardException;
import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.board.rest.BaseIT;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

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

public class BoardSettingsResourcePutIT extends BaseIT {

    @Autowired
    private BoardSettingsService boardSettingsServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(boardSettingsServiceMock);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void puttingCorrectBoardSettings_asAdministrator() {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(boardSettingsDto());

        // when
        MockMvcResponse response = request.when().put("/api/v1/board-settings");

        // then
        response.then().statusCode(200);
        BoardSettingsDto resultBody = response.then().extract().body().as(BoardSettingsDto.class);
        assertThat(resultBody.getBoardName()).isEqualTo("new board name");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMINISTRATOR"})
    public void puttingBoardSettings_whenInvalidDataProvided() {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(boardSettingsDto());

        Mockito.doThrow(new BoardException(violation())).when(boardSettingsServiceMock).setBoardSettings(any());

        // when
        MockMvcResponse response = request.when().put("/api/v1/board-settings");

        // then
        assertError(response, 400, ErrorInfo.INVALID_BOARD_SETTINGS);
    }

    @Test
    public void updateBoardSettings_shouldNotBePossibleForGuests() {
        // given
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(boardSettingsDto());

        // when
        MockMvcResponse response = request.when().put("/api/v1/board-settings");

        // then
        assertError(response, 401, ErrorInfo.UNAUTHORIZED);
    }

    @Test
    @WithMockUser(username = "member", roles = {})
    public void updatingBoardSettings_shouldNotBePossibleForRegularMembers() {
        // given

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(boardSettingsDto());
        MockMvcResponse response = request.when().put("/api/v1/board-settings");

        // then
        assertError(response, 403, ErrorInfo.FORBIDDEN);
    }

    private Set<? extends ConstraintViolation<?>> violation() {
        Path propertyPath = mock(Path.class);
        given(propertyPath.toString()).willReturn("boardName");
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        given(violation.getPropertyPath()).willReturn(propertyPath);
        given(violation.getMessage()).willReturn("must be not blank");
        return Sets.newHashSet(violation);
    }

    private BoardSettingsDto boardSettingsDto() {
        return BoardSettingsDto.builder()
                .boardName("new board name")
                .build();
    }

    private void assertError(MockMvcResponse response, int httpStatus, ErrorInfo errorInfo) {
        response.then().statusCode(httpStatus);
        ErrorResponse resultBody = response.then().extract().body().as(ErrorResponse.class);
        assertThat(resultBody.getCode()).isEqualTo(errorInfo.getCode());

    }

}