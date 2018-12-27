/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.errorcode;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.jbb.e2e.serenity.rest.RestUtils;

import io.restassured.response.Response;

import static net.serenitybdd.rest.SerenityRest.then;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiErrorCodeResourceSteps extends ScenarioSteps {

    public static final String V1_API_ERROR_CODES = "api/v1/api-error-codes";

    @Step
    public Response get_api_error_codes() {
        return RestUtils.prepareApiRequest()
                .basePath(V1_API_ERROR_CODES)
                .when()
                .get()
                .andReturn();
    }

    @Step
    public void assert_response_contains_valid_error_codes() {
        Response response = then().extract().response();
        ErrorCodesDto errorCodesDto = response.as(ErrorCodesDto.class);

        assertThat(errorCodesDto.getErrorCodes()).isNotEmpty();
        assertThat(errorCodesDto.getErrorCodes()).allSatisfy(code -> {
            assertThat(code.getErrorName()).isNotBlank();
            assertThat(code.getHttpStatus()).isPositive();
            assertThat(code.getHttpStatusName()).isNotBlank();
            assertThat(code.getErrorCode().startsWith("JBB-"));
            assertThat(code.getErrorMessage()).isNotBlank();
        });
    }
}
