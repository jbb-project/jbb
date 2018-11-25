/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.boardsettings;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.ErrorDetailDto;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class BoardSettingsResourceSteps extends ScenarioSteps {

    public static final String V1_BOARD_SETTINGS = "api/v1/board-settings";

    @Steps
    AssertRestSteps assertRestSteps;

    @Step
    public Response get_board_settings() {
        return RestUtils.prepareApiRequest()
                .basePath(V1_BOARD_SETTINGS)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public Response put_board_settings(BoardSettingsDto boardSettingsDto) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_BOARD_SETTINGS)
                .when()
                .body(boardSettingsDto)
                .put()
                .andReturn();
    }

    @Step
    public void should_contain_error_detail_about_blank_board_name() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("boardName")
                        .message("must not be blank").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_invalid_length_of_board_name() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("boardName")
                        .message("length must be between 1 and 60").build()
        );
    }

    @Step
    public void should_contains_board_settings() {
        BoardSettingsDto boardSettingsDto = then().extract().response().as(BoardSettingsDto.class);
        assertThat(boardSettingsDto.getBoardName()).isNotNull();
    }
}
