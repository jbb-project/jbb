/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.board;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class BoardResourceSteps extends ScenarioSteps {

    public static final String V1_BOARD = "api/v1/board";

    @Steps
    AssertRestSteps assertRestSteps;

    @Step
    public Response get_board() {
        return RestUtils.prepareApiRequest()
                .basePath(V1_BOARD)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public void should_contains_not_empty_board() {
        BoardDto boardDto = then().extract().response().as(BoardDto.class);
        assertThat(boardDto.getForumCategories()).isNotEmpty();
        assertThat(boardDto.getForumCategories().get(0).getId()).isNotNull();
        assertThat(boardDto.getForumCategories().get(0).getName()).isNotBlank();
        assertThat(boardDto.getForumCategories().get(0).getPosition()).isNotNegative();
        assertThat(boardDto.getForumCategories().get(0).getForums()).isNotEmpty();
        assertThat(boardDto.getForumCategories().get(0).getForums().get(0).getId()).isNotNull();
        assertThat(boardDto.getForumCategories().get(0).getForums().get(0).getName()).isNotBlank();
        assertThat(boardDto.getForumCategories().get(0).getForums().get(0).getPosition()).isNotNegative();
        assertThat(boardDto.getForumCategories().get(0).getForums().get(0).getClosed()).isNotNull();
    }
}
