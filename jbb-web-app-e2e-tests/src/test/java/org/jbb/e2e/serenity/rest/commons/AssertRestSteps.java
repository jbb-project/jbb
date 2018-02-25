/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.commons;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.springframework.http.HttpStatus;

import java.util.List;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class AssertRestSteps extends ScenarioSteps {

    @Step
    public ErrorDto assert_response_error_info(ErrorInfo errorInfo) {
        Response response = then().extract().response();
        ErrorDto errorDto = response.as(ErrorDto.class);
        assertThat(errorDto.getCode()).isEqualTo(errorInfo.getCode());
        assertThat(errorDto.getStatus()).isEqualTo(errorInfo.getStatus().name());
        assertThat(errorDto.getMessage()).isEqualTo(errorInfo.getMessage());
        then().statusCode(errorInfo.getStatus().value());
        return errorDto;
    }

    @Step
    public void assert_response_error_detail_exists(ErrorDetailDto expectedDetail) {
        Response response = then().extract().response();
        ErrorDto errorDto = response.as(ErrorDto.class);

        List<ErrorDetailDto> details = errorDto.getDetails();

        assertThat(details).anySatisfy(detail -> {
            assertThat(detail.getName()).isEqualTo(expectedDetail.getName());
            assertThat(detail.getMessage()).isEqualTo(expectedDetail.getMessage());
        });
    }

    @Step
    public void assert_response_status(HttpStatus status) {
        then().statusCode(status.value());
    }

    @Step
    public void assert_json_content_type() {
        then().contentType(ContentType.JSON);
    }
}
