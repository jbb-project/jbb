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


import org.jbb.board.api.base.BoardSettings;
import org.jbb.board.api.base.BoardSettingsService;
import org.jbb.board.rest.BaseIT;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class BoardSettingsResourceGetIT extends BaseIT {

    @Autowired
    private BoardSettingsService boardSettingsServiceMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Mockito.reset(boardSettingsServiceMock);
    }

    @Test
    public void gettingBoardSettings() {
        BoardSettings boardSettings = BoardSettings.builder()
                .boardName("board testing")
                .build();
        given(boardSettingsServiceMock.getBoardSettings()).willReturn(boardSettings);

        // when
        MockMvcRequestSpecification request = RestAssuredMockMvc.given();
        MockMvcResponse response = request.when().get("/api/v1/board-settings");

        // then
        response.then().statusCode(200);
        BoardSettingsDto resultBody = response.then().extract().body().as(BoardSettingsDto.class);
        assertThat(resultBody.getBoardName()).isEqualTo("board testing");
    }


}