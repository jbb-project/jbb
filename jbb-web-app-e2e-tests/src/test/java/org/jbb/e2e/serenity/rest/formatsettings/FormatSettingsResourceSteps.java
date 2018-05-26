/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.formatsettings;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;
import org.jbb.e2e.serenity.rest.commons.AssertRestSteps;
import org.jbb.e2e.serenity.rest.commons.ErrorDetailDto;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class FormatSettingsResourceSteps extends ScenarioSteps {

    public static final String V1_FORMAT_SETTINGS = "api/v1/format-settings";

    @Steps
    AssertRestSteps assertRestSteps;

    @Step
    public Response get_format_settings() {
        return RestUtils.prepareApiRequest()
                .basePath(V1_FORMAT_SETTINGS)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public Response put_format_settings(FormatSettingsDto formatSettingsDto) {
        return RestUtils.prepareApiRequest()
                .basePath(V1_FORMAT_SETTINGS)
                .when()
                .body(formatSettingsDto)
                .put()
                .andReturn();
    }

    @Step
    public void should_contains_format_settings() {
        FormatSettingsDto formatSettingsDto = then().extract().response().as(FormatSettingsDto.class);
        assertThat(formatSettingsDto.getDateFormat()).isNotNull();
        assertThat(formatSettingsDto.getDurationFormat()).isNotNull();
    }

    @Step
    public void should_contain_error_detail_about_empty_date_format() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("dateFormat")
                        .message("must not be blank").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_invalid_date_format() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("dateFormat")
                        .message("Incorrect date format").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_empty_duration_format() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("durationFormat")
                        .message("must not be blank").build()
        );
    }

    @Step
    public void should_contain_error_detail_about_invalid_duration_format() {
        assertRestSteps.assert_response_error_detail_exists(
                ErrorDetailDto.builder()
                        .name("durationFormat")
                        .message("Incorrect duration format").build()
        );
    }
}
